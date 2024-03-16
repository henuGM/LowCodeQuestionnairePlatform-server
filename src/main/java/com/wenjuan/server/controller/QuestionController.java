package com.wenjuan.server.controller;

import com.wenjuan.server.pojo.Question;
import com.wenjuan.server.pojo.Result;
import com.wenjuan.server.service.QuestionService;
import com.wenjuan.server.service.UserService;
import com.wenjuan.server.utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.lang.reflect.Field;

import static org.assertj.core.util.DateUtil.now;

@RestController
@RequestMapping("/api/question")
@Validated
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String print(@PathVariable("id") String id) {
        return id;
    }

    @PostMapping
    public Result createQuestion(@RequestHeader(name = "Authorization") String token){
        Map<String,Object> map = JwtUtil.parseToken(token);
        Integer id=(Integer) map.get("id");
        Question newQuestion = new Question();
        newQuestion.setTitle("默认title");
        newQuestion.setIsPublished(0);
        newQuestion.setIsStar(0);
        newQuestion.setAnswerCount(0);
        newQuestion.setIsDeleted(0);
        newQuestion.setCreate_user(id);
        newQuestion.setDesc("默认描述");
        newQuestion.setComponentList("[]");
        newQuestion.setJs("1");
        newQuestion.setCss("2");
        questionService.createQuestion(newQuestion);
        return Result.success(newQuestion.getId());
    }

    @GetMapping
    public Map<String,Object> getQuestionsByUserId(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(name = "isStar", required = false, defaultValue = "0") Integer isStar,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "isDeleted", required = false, defaultValue = "0") Integer isDeleted,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Map<String,Object> map = JwtUtil.parseToken(token);
        Integer userId=(Integer) map.get("id");
        // 调用服务层方法获取问题列表
        List<Question> questions = questionService.findQuestionsByUserIdAndCondition(userId, keyword, isStar, isDeleted, page, pageSize);
        long total = questionService.countQuestionsByUserIdAndCondition(userId, keyword, isStar, isDeleted);
        System.out.println(userId);
        System.out.println( keyword);
        System.out.println(isStar);
        System.out.println(isDeleted);
        System.out.println(total);
        System.out.println(questions);
        // 构建返回结果
        Map<String, Object> temp = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        temp.put("list", questions);
        temp.put("total", total);
        result.put("errno", 0);
        result.put("data", temp);
        return result;
    }


    @PatchMapping("/{id}")
    public Result updateQuestion(@PathVariable("id") Integer id, @RequestBody Question question) throws CloneNotSupportedException {
        // 验证并获取用户ID（此处假
    // 从数据库获取原始问题对象
        Question originalQuestion = questionService.findById(id); // 假设是通过ID查询，实际请根据实际情况调整
        if (originalQuestion == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found");
        }
        Question tempQuestion = (Question) originalQuestion.clone();
        BeanUtils.copyProperties(question, originalQuestion, "id", "create_user", "createdAt"); // 不复制ID、创建者ID和创建时间字段
        restoreNonNullValues(originalQuestion, tempQuestion);
        int rowsAffected = questionService.updateQuestion(originalQuestion);

        if (rowsAffected > 0) {
            return Result.success(null);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update the question");
        }
    }


public void restoreNonNullValues(Question originalQuestion, Question tempQuestion) {
    Class<?> clazz = originalQuestion.getClass();

    while (clazz != null) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true); // 允许访问私有字段

            try {
                // 获取originalQuestion的字段值
                Object originalFieldValue = field.get(originalQuestion);
                if (originalFieldValue == null) {
                    // 如果originalQuestion的字段值为null，则从tempQuestion中获取该字段的值
                    Object tempFieldValue = field.get(tempQuestion);
                    if (tempFieldValue != null) {
                        field.set(originalQuestion, tempFieldValue);
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // 处理无法访问字段的异常
                System.err.println("Error accessing field: " + field.getName());
                e.printStackTrace();
            }
        }

        clazz = clazz.getSuperclass(); // 继续检查父类中的字段
    }
}

}


