package com.shop.back.api.bean.vto;

import lombok.Data;

/**
 * 用户登录参数
 */
@Data
public class LoginVto {

    /**
     * 小程序用户使用
     */
    private String code;

    /**
     * 后台用户使用
     * number
     * password
     */
    private String number;

    private String password;

}
