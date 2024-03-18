package com.wenjuan.server.mapper;

import com.wenjuan.server.pojo.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface questionMapper {

    @Select("select * from question where title=#{title}")
    Question findByTitle(String title);

    // 修改了Insert语句中的参数绑定方式以避免SQL注入风险
    @Insert("insert into question(isPublished, isStar, answerCount, isDeleted, create_user, title, `desc`, componentList, js, css)" +
            " values(#{isPublished}, #{isStar}, #{answerCount}, #{isDeleted}, #{create_user}, #{title}, #{desc}, #{componentList}, #{js}, #{css})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    int add(Question question);

@Select("SELECT * FROM question WHERE create_user = #{userId} AND " +
        "(#{keyword} IS NULL OR title LIKE CONCAT('%', #{keyword}, '%')) AND " +
        "((#{isStar} IS NULL AND #{isDeleted} = 1) OR (#{isStar} = 0 AND #{isDeleted} = 1) OR (isStar = #{isStar} AND isDeleted = #{isDeleted}))" +
        " ORDER BY id DESC LIMIT #{pageSize} OFFSET #{offset}")
List<Question> selectQuestionsByUserIdAndCondition(
        @Param("userId") Integer userId,
        @Param("keyword") String keyword,
        @Param("isStar") Integer isStar,
        @Param("isDeleted") Integer isDeleted,
        @Param("pageSize") Integer pageSize,
        @Param("offset") Integer offset);


 @Select("SELECT COUNT(*) FROM question WHERE create_user = #{userId} AND " +
        "(#{keyword} IS NULL OR title LIKE CONCAT('%', #{keyword}, '%')) AND " +
        "((#{isStar} IS NULL AND #{isDeleted} = 1) OR (#{isStar} = 0 AND #{isDeleted} = 1) OR (isStar = #{isStar} AND isDeleted = #{isDeleted}))")
long countQuestionsByUserIdAndCondition(
        @Param("userId") Integer userId,
        @Param("keyword") String keyword,
        @Param("isStar") Integer isStar,
        @Param("isDeleted") Integer isDeleted);

 @Update("UPDATE question SET isPublished=#{question.isPublished}, " +
            "isStar=#{question.isStar}, answerCount=#{question.answerCount}, " +
            "isDeleted=#{question.isDeleted}, create_user=#{question.create_user}, " +
            "title=#{question.title}, `desc`=#{question.desc}, componentList=#{question.componentList}, " +
            "js=#{question.js}, css=#{question.css} " +
            "WHERE id=#{question.id}")
int updateByStatement(@Param("question") Question question);



    @Select("SELECT * FROM question WHERE id = #{id}")
    Question findById(@Param("id") Integer id);

@Delete("DELETE FROM question WHERE id = #{id}")
int deleteById(@Param("id") Integer id);


}
