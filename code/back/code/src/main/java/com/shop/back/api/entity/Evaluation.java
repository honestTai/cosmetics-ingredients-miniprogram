package com.shop.back.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 评价实体
 * </p>
 *
 * @author @author

 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Evaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评价主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    /**
     * 评价人
     */
    private Integer userId;

    /**
     * 回复id
     */
    private Integer replyId;

    /**
     * 化妆id
     */
    private Integer goodsId;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 回复时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date replyTime;

    /**
     * 是否回复
     * 0未1回复
     */
    private Integer replyStatus;

    /**
     * 评分
     */
    private Double score;

    /**
     * 图片
     */
    private String pic;

    /**
     * 评论人
     */
    @TableField(exist = false)
    private User evUser;

    /**
     * 回复人
     */
    @TableField(exist = false)
    private User reUser;

    private Integer type;

    @TableField(exist = false)
    private Forum forum;

    @TableField(exist = false)
    private List<String> picList;
    @TableField(exist = false)
    private String titleName;

}
