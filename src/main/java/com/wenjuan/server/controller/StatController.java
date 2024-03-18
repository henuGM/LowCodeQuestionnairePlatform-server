package com.wenjuan.server.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wenjuan.server.pojo.Result;
import com.wenjuan.server.pojo.Stat;
import com.wenjuan.server.service.StatService;
import com.wenjuan.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stat")
@Validated
public class StatController {

    @Autowired
    private StatService statService;



    @DeleteMapping("/{statId}")
    public Result<Void> deleteStat(@PathVariable("statId") Integer statId, @RequestHeader(name = "Authorization") String token) {
        Map<String, Object> map = JwtUtil.parseToken(token);
        // 进行必要的权限验证

        // 调用StatService删除统计信息，并检查受影响的行数
        int affectedRows = statService.deleteStatById(statId);

        if (affectedRows > 0) {
            return Result.success(null);
        } else {
            return Result.error("Stat not found or deletion failed");
        }
    }

    @GetMapping("/{questionId}")
    public Result<Map<String,Object>> getQuestionStatList(@PathVariable("questionId") Integer questionId,
                                                  @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) throws JsonProcessingException {
        List<Stat> stats = statService.getQuestionStatsByQuestionId(questionId, page, pageSize);
        int total=statService.getStatCountByQuestionId(questionId);
        Map<String,Object> res=new HashMap<String, Object>();
        res.put("total",total);

        List<Map<String, Object>> transformedStats = new ArrayList<>();
        for (Stat stat : stats) {
            Map<String, Object> singleStatMap = new HashMap<>();
            String propsString=stat.getProps();
            ObjectMapper objectMapper = new ObjectMapper();
            singleStatMap.put("_id", stat.getId());

            List<Map<String, String>> propList = objectMapper.readValue(propsString, new TypeReference<List<Map<String, String>>>(){});
            for (Map<String, String> prop : propList) {
                String componentIdValue = prop.get("componentId");
                String value = prop.get("value");

                singleStatMap.put(componentIdValue, value);
            }
            transformedStats.add(singleStatMap);
        }
        res.put("list", transformedStats);
        return Result.success(res);

    }

    public static class StatRequestBody {

        @JsonProperty("questionId")
        private String questionId;

        @JsonProperty("answerList")
        private List<Answer> answerList;

        // 添加getter和setter方法...

        public String getQuestionId() { return questionId; }
        public void setQuestionId(String questionId) { this.questionId = questionId; }

        public List<Answer> getAnswerList() { return answerList; }
        public void setAnswerList(List<Answer> answerList) { this.answerList = answerList; }
    }

    public static class Answer {
        @JsonProperty("componentId")
        private String componentId;

        @JsonProperty("value")
        private String value;

        // 添加getter和setter方法...

        public String getComponentId() { return componentId; }
        public void setComponentId(String componentId) { this.componentId = componentId; }

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }
}
