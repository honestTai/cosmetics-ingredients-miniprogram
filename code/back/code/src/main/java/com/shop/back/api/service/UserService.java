package com.shop.back.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.back.api.bean.dto.LoginDto;
import com.shop.back.api.bean.general.result.ResultException;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.bean.vto.LoginVto;
import com.shop.back.api.bean.vto.WxUserVto;
import com.shop.back.api.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author @author

 */
public interface UserService extends IService<User> {

    LoginDto login(LoginVto loginVto) throws ResultException;

    void register(User user) throws ResultException;

    Page<?> userList(ListVto listVto) throws ResultException;

    void operationUser(User user, Integer operationType) throws ResultException;

    LoginDto wxLogin(String code) throws ResultException;

    User wxUserUpdate(WxUserVto wxUserVto);

    void isLogin() throws ResultException;
}
