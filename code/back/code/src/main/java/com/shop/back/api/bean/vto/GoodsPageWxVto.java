package com.shop.back.api.bean.vto;

import lombok.Data;

@Data
public class GoodsPageWxVto {
    private Integer current;

    private Integer size;

    private String name;

    private Integer categorySecond;

    private Integer merchantId;

}
