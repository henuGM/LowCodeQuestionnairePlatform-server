package com.wenjuan.server.service;

import com.wenjuan.server.pojo.Question;

import java.awt.print.Pageable;
import java.util.List;

public interface QuestionService {
    Question findByTitle(String title);

    int createQuestion(Question question);
    List<Question> findQuestionsByUserIdAndCondition(Integer userId, String keyword, Integer isStar, Integer isDeleted, Integer page, Integer pageSize);

    long countQuestionsByUserIdAndCondition(Integer userId, String keyword, Integer isStar, Integer isDeleted);

    int updateQuestion(Question originalQuestion);

    Question findById(Integer id);
}
