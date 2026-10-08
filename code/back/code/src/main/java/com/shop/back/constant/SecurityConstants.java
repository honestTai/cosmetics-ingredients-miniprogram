package com.shop.back.constant;

/**
 * 权限去验证常量
 */
public class SecurityConstants {

    private SecurityConstants() {
        throw new IllegalStateException("Cannot create instance of static constant class");
    }

    /**
     * 用于登录的 url
     */
    public static final String AUTH_LOGIN_URL = "/back/login";

    /**
     * 小程序的登录URL
     */
    public static final String AUTH_WX_LOGIN_URL = "/wx/login";

    public static final String AUTH_MERCHANT_UPLOAD_HEAD="/back/upload";




    /**
     * JWT签名密钥，这里使用 HS512 算法的签名密钥
     */
    public static final String JWT_SECRET_KEY = "p2s5v8y/B?E(H+MbQeThVmYq3t6w9z$C&F)J@NcRfUjXnZr4u7x!A%D*G-KaPdS";


    /**
     * 一般是在请求头里加入 Authorization，并加上 Bearer 标注
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Authorization 请求头
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * token 类型
     */
    public static final String TOKEN_TYPE = "JWT";

    public static final String TOKEN_ROLE_CLAIM = "role";
    public static final String TOKEN_ISSUER = "security";
    public static final String TOKEN_AUDIENCE = "security-all";

    /**
     * 当 Remember 是 false 时，token 有效时间 2 小时
     */
    public static final long EXPIRATION_TIME = 60 * 60 * 2L;

    /**
     * 当 Remember 是 true 时，token 有效时间 7 天
     */
    public static final long EXPIRATION_REMEMBER_TIME = 60 * 60 * 24 * 7L;
}
