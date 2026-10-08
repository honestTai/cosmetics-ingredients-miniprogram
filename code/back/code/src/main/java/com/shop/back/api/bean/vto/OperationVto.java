package com.shop.back.api.bean.vto;

import com.shop.back.api.entity.*;
import com.shop.back.api.entity.Class;
import lombok.Data;

/**
 * 统一操作实体类
 * 包含，化妆品，分类，用户
 * 操作类别，模块类别
 * 删除走另外接口
 */
@Data
public class OperationVto {

    //用户
    private User user;

    //化妆品
    private Goods goods;

    //分类
    private Class aClass;

    //评论
    private Evaluation evaluation;

    //操作类型，operationType->0修改1新增2删除
    private Integer operationType;


    private Integer type;

    private Forum forum;

    private Honor honor;

    private HonorGoods honorGoods;

}
