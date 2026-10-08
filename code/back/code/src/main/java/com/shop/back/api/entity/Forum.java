package com.shop.back.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;


/**
 * 动态表
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Forum {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 动态标题
     */
    private String title;

    /**
     * info 动态内容
     */
    private String info;

    /**
     * pic 图片封面
     */
    private String pic;

    /**
     * 关联用户，表示发布人
     */
    private Integer userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    /**
     * 评论
     */
    private Integer comment;
    /**
     * 查看
     */
    private Integer view;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private List<String> picList;

    /**
     * 获取评论数据
     */
    @TableField(exist = false)
    private List<Evaluation> evaluations;

}
