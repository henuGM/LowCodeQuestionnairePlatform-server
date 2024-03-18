package com.wenjuan.server.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wenjuan.server.controller.StatController;
import com.wenjuan.server.mapper.statMapper;
import com.wenjuan.server.pojo.Stat;
import com.wenjuan.server.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private statMapper statMapper;

@Override
public int createStat(int questionId, List<StatController.Answer> answers) throws JsonProcessingException {
    System.out.println(questionId);
    System.out.println(answers);

    ObjectMapper objectMapper = new ObjectMapper();
    String props = objectMapper.writeValueAsString(answers);

    Stat stat = new Stat();
    stat.setQuestionId(questionId);
    stat.setProps(props);

    // 假设statMapper.insert方法已经通过注解正确设置了自增主键映射
    int newStatId = statMapper.insert(stat);
    return newStatId;
}


    @Override
    public Stat getStatById(Integer statId) {
        return statMapper.selectStatById(statId);
    }

    // 由于不再使用userId，所以更新Stat的方法也需要进行相应的调整
    @Override
    public int updateStat(Stat stat) {
        // 移除对userId的处理
        return statMapper.updateStat(stat.getId(), stat.getQuestionId(), stat.getProps());
    }

    // 需要在StatMapper中定义一个不包含userId参数的updateStatWithoutUserId方法

    @Override
    public int deleteStatById(Integer statId) {
        return statMapper.deleteStatById(statId);
    }



    @Override
    public List<Stat> getQuestionStatsByQuestionId(Integer questionId, Integer offset, Integer limit) {
        int start = (offset - 1) * limit; // 转换为SQL中的起始行号
        return statMapper.selectStatsByQuestionId(questionId, start, limit);
    }

    @Override
    public int getStatCountByQuestionId(Integer questionId) {
        return statMapper.countByQuestionId(questionId);
    }


}
