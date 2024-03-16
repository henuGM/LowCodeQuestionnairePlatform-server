package com.wenjuan.server.service.impl;

import com.wenjuan.server.mapper.questionMapper;
import com.wenjuan.server.pojo.Question;
import com.wenjuan.server.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wenjuan.server.mapper.questionMapper;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private questionMapper questionMapper;

    @Override
    public Question findByTitle(String title) {
        Question q=  questionMapper.findByTitle(title);
        return  q;
    }

//    @Override
//    public Question createQuestion(Integer isPublished, Integer isStar, Integer answerCount, Integer isDeleted, Integer create_user, String title, String desc, String componentList, String js, String css) {
//        return questionMapper.add(isPublished, isStar, answerCount, isDeleted, create_user, title, desc, componentList, js, css);
//    }

    @Override
    public int createQuestion(Question question) {
        return questionMapper.add(question);
    }

    @Override
    public List<Question> findQuestionsByUserIdAndCondition(Integer userId, String keyword, Integer isStar, Integer isDeleted, Integer page, Integer pageSize) {
        // 计算offset
        int offset = (page - 1) * pageSize;

        return questionMapper.selectQuestionsByUserIdAndCondition(userId, keyword, isStar, isDeleted, pageSize, offset);
    }

    @Override
    public long countQuestionsByUserIdAndCondition(Integer userId, String keyword, Integer isStar, Integer isDeleted) {
        return questionMapper.countQuestionsByUserIdAndCondition(userId, keyword, isStar, isDeleted);
    }

    @Override
    public int updateQuestion(Question question) {
        return questionMapper.updateByStatement( question);
    }

    @Override
    public Question findById(Integer id) {
        return questionMapper.findById(id); // 调用mapper中的findById方法
    }
}
