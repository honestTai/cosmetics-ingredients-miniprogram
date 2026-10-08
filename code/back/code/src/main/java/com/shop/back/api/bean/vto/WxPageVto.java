package com.shop.back.api.bean.vto;

import lombok.Data;

import java.util.List;

@Data
public class WxPageVto {

    private Integer current;

    private Integer size;

    private List<Integer> ids;

    private Integer goodsId;

    private String likeSearch;
}
