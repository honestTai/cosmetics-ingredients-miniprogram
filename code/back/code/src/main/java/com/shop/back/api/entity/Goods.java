package com.shop.back.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.shop.back.api.bean.dto.GoodsSpecDto;
import com.shop.back.api.bean.vto.GoodsIntroduceVto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 化妆品实体
 * </p>
 *
 * @author @author

 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 介绍，富文本
     */
    private String introduce;

    /**
     * 4张图片
     */
    private String images;

    /**
     * 分类Id
     */
    private Integer classId;


    @TableField(exist = false)
    private List<String> goodsPhotoList;

    /**
     * 价格
     */
    private BigDecimal price;


    /**
     * 产品特色，成分等，json字符串
     */
    private String sortIntroduce;

    /**
     * List<JSON>
     */
    @TableField(exist = false)
    private List<GoodsIntroduceVto> sortIntroduces;

    @TableField(exist = false)
    private Integer orders;

    @TableField(exist = false)
    private String  className;

    @TableField(exist = false)
    private Double score;

    @TableField(exist = false)
    private String reason;


}
