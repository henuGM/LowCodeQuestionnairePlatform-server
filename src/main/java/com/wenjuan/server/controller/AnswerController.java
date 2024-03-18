package com.wenjuan.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wenjuan.server.pojo.Result;
import com.wenjuan.server.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/answer")
@Validated
public class AnswerController {
    @Autowired
    private StatService statService;
    @PostMapping
    public Result<Integer> createStat(@RequestBody StatController.StatRequestBody requestBody) throws JsonProcessingException {
        // 提取请求体中的questionId和answerList
        String questionId = requestBody.getQuestionId();
        List<StatController.Answer> answers = requestBody.getAnswerList();
        // 根据新的Stat结构调用StatService创建统计信息
        int newStatId = statService.createStat(Integer.parseInt(questionId), answers);

        if (newStatId > 0) {
            return Result.success(newStatId);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create the statistic");
        }
    }
}
