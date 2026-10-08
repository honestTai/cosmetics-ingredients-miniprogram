package com.shop.back.api.bean.vto;

import lombok.Data;

/**
 * 化妆品成分介绍的Vto
 */
@Data
public class GoodsIntroduceVto {
    //名称
    private String key;
    //作用
    private String value;
    //风险
    private String exposures;
    //活性
    private String reactive;
    //致痘
    private String vaccination;
}
