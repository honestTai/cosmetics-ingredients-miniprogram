package com.shop.back.api.bean.dto;

import com.shop.back.api.entity.Class;
import com.shop.back.api.entity.Goods;
import lombok.Data;

@Data
public class GoodsDto {

    //分类
    private Class aClass;

    //化妆品
    private Goods goods;

    //评价数
    private Integer commentTotal;
}
