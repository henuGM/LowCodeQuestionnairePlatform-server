package com.wenjuan.server.pojo;


import lombok.Data;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
@Data
public class Question implements Cloneable{

    @Override
    public Object clone() throws CloneNotSupportedException {
        return (Question) super.clone();
    }
    private Integer id;//主键ID
    private Integer isPublished;//是否发布
    private Integer isStar;//是否标星
    private Integer answerCount;//回答数量
    private Integer isDeleted;//是否删除
    private Integer create_user;//创建者
    private String title;//标题
    private String desc;//描述
    private String componentList;//组件列表
    private String js;
    private String css;
    private LocalDateTime createdAt;//创建时间
}
