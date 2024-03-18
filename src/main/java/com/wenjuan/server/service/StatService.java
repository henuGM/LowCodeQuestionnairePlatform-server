package com.wenjuan.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wenjuan.server.controller.StatController;
import com.wenjuan.server.pojo.Stat;

import java.util.List;

public interface StatService {

    /**
     * 创建新的统计信息
     */
    int createStat(int questionId, List<StatController.Answer> answers) throws JsonProcessingException;

    /**
     * 根据ID获取单个统计信息
     */
    Stat getStatById(Integer statId);

    /**
     * 更新统计信息（已移除对userId的处理）
     */
    int updateStat(Stat statData);

    /**
     * 根据ID删除统计信息
     */
    int deleteStatById(Integer statId);
    List<Stat> getQuestionStatsByQuestionId(Integer questionId, Integer offset, Integer limit);
    int getStatCountByQuestionId(Integer questionId);

    // 可能存在的其他业务逻辑方法...
}
