package com.shop.back.constant;

public class operationConstants {
    //定义操作类型的常量

    /**
     * 用户操作类型常量
     */
    public static final Integer USER = 1;

    /**
     * 分类操作类型常量
     */
    public static final Integer CLASS = 2;

    /**
     * 化妆品操作类型常量
     */
    public static final Integer GOODS = 3;

    /**
     * 评价操作类型常量
     */
    public static final Integer COMMENT = 5;

    /**
     * 动态操作常量
     */
    public static final Integer FORUM = 4;

    /**
     * 榜单操作常量
     */
    public static final Integer HONOR = 6;

    /**
     * 榜单商品操作常量
     */
    public static final Integer HONOR_GOODS = 7;

    //定义操作动词常量

    /**
     * 增加操作动词常量
     */
    public static final Integer ADD = 1;

    /**
     * 更新操作动词常量
     */
    public static final Integer UPDATE = 0;

    /**
     * 删除操作动词常量
     */
    public static final Integer DELETE = 2;

    //定义用户操作权限及状态常量

    /**
     * 禁止登录状态常量
     */
    public static final Integer USER_NO_LOGIN = 0;
    /**
     * 允许登录状态常量
     */
    public static final Integer USER_LOGIN = 2;
    /**
     * 重置密码操作状态常量
     */
    public static final Integer USER_PASSWORD = 1;
    /**
     * 修改信息操作状态常量
     */
    public static final Integer USER_UPDATE = 3;
    /**
     * 添加用户操作状态常量
     */
    public static final Integer USER_ADD = 4;

    /**
     * 删除用户操作状态常量
     */
    public static final Integer USER_DELETE = 5;
}

