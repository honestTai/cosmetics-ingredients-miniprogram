package com.shop.back.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 榜单表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HonorGoods {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 化妆品
     */
    private Integer goodsId;

    /**
     * 排序
     */
    private Integer orders;

    /**
     * 榜单id
     */
    private Integer honorId;

    /**
     * 理由
     */
    private String reason;

    @TableField(exist = false)
    private Goods goods;

    @TableField(exist = false)
    private Honor honor;

    @TableField(exist = false)
    private Class aClass;

    @TableField(exist = false)
    private Double score;
}
