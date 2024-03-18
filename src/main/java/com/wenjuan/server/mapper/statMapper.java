package com.wenjuan.server.mapper;

import com.wenjuan.server.pojo.Stat;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface statMapper {

    // 插入新的统计信息
    @Insert("INSERT INTO stat(questionId, props) VALUES(#{entity.questionId}, #{entity.props})")
    @Options(useGeneratedKeys = true, keyProperty = "entity.id")
    int insert(@Param("entity") Stat stat);
    @Select("SELECT id, questionId, props FROM stat WHERE id = #{statId}")
    Stat selectStatById(@Param("statId") Integer statId);

    // 更新统计信息（已移除对userId的更新操作）
    @Update("UPDATE stat SET questionId=#{questionId}, props=#{props} WHERE id=#{id}")
    int updateStat(@Param("id") Integer id, @Param("questionId") Integer questionId, @Param("props") String props);

    // 根据ID删除统计信息
    @Delete("DELETE FROM stat WHERE id = #{statId}")
    int deleteStatById(@Param("statId") Integer statId);

    @Select("SELECT id, questionId, props FROM stat WHERE questionId = #{questionId} LIMIT #{start}, #{limit}")
    List<Stat> selectStatsByQuestionId(@Param("questionId") Integer questionId, @Param("start") Integer start, @Param("limit") Integer limit);

    @Select("SELECT COUNT(*) FROM stat WHERE questionId = #{questionId}")
    int countByQuestionId(@Param("questionId") Integer questionId);

}
