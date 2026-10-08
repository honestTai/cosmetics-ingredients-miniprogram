package com.shop.back.api.bean.dto;

import lombok.Data;

@Data
public class WxLoginDto {

    private String openid;

    private String session_key;
}
