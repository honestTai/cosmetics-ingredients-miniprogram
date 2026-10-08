package com.shop.back.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.back.api.bean.dto.LoginDto;
import com.shop.back.api.bean.dto.WxLoginDto;
import com.shop.back.api.bean.general.result.ResultException;
import com.shop.back.api.bean.general.result.ResultStatus;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.bean.vto.LoginVto;
import com.shop.back.api.bean.vto.WxUserVto;
import com.shop.back.api.entity.User;
import com.shop.back.api.mapper.UserMapper;
import com.shop.back.api.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.back.constant.SecurityConstants;
import com.shop.back.util.BaseFunction;
import com.shop.back.util.JwtUtils;
import com.shop.back.util.RedisUtil;
import com.shop.back.util.UserLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.shop.back.constant.RoleConstants.*;
import static com.shop.back.constant.operationConstants.*;
import static com.shop.back.util.encryption.MD5Util.getMD5;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author @author
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 小程序id
     */
    private final static String WX_APPID = "wx52a4cadf3141aa2e";
    /**
     * 小程序密钥
     */
    private final static String WX_SECRET = "073bcbee0c8b5beb1f0b852952822995";
    /**
     * 授权类型
     */
    private final static String GRANT_TYPE = "authorization_code";

    @Override
    public LoginDto login(LoginVto loginVto) throws ResultException {
        // 用户登录认证
        return authLogin(loginVto);
    }

    @Override
    public void register(User user) throws ResultException {
        if (userMapper.selectOne(new QueryWrapper<User>().eq("number", user.getNumber())) != null) {
            throw new ResultException(ResultStatus.NUM);
        }
        user.setTime(new Date());
        user.setType(ADMIN);
        user.setPassword(bCryptPasswordEncoder.encode(getMD5(user.getPassword())));
        userMapper.insert(user);
    }

    /**
     * 用户登录认证
     *
     * @param loginVto 用户登录信息
     */
    private LoginDto authLogin(LoginVto loginVto) throws ResultException {
        String userName = loginVto.getNumber();
        String password = getMD5(loginVto.getPassword());

        // 根据登录账号获取用户信息
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("number", userName));
        if (user == null) {
            throw new ResultException(ResultStatus.ERROR_NUM_PWD);
        }
        // 验证登录密码是否正确。如果正确，则赋予用户相应权限并生成用户认证信息
        if (this.bCryptPasswordEncoder.matches(password, user.getPassword())) {
            if (user.getType().equals(NOLOGIN)) {
                throw new ResultException(ResultStatus.NOT_PASS_LOGIN);
            }
            List<String> roleList = new ArrayList<>();
            roleList.add("ROLE_USER");
            // 生成 token
            String token = JwtUtils.generateToken(userName, roleList, false);

            // 认证成功后，设置认证信息到 Spring Security 上下文中
            Authentication authentication = JwtUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //存入Redis,下载鉴权
            String key = bCryptPasswordEncoder.encode(getMD5(new Date().toString())).replaceAll("/", "");
            redisUtil.setCacheObject(key, user, 2, TimeUnit.HOURS);
            //注入当前用户的基本信息
            UserLocal.setUser(user);
            return new LoginDto(user, SecurityConstants.TOKEN_PREFIX + token);
        }
        throw new ResultException(ResultStatus.ERROR_NUM_PWD);
    }

    @Override
    public Page<?> userList(ListVto listVto) throws ResultException {
        return userMapper.selectUserByPage(new Page<>(listVto.getPage(), listVto.getPageSize()), listVto);
    }

    /**
     * @param user
     * @param operationType 0禁止登录1重置密码2允许登录3修改信息4添加用户
     * @throws ResultException
     */
    @Override
    @Transactional
    public void operationUser(User user, Integer operationType) throws ResultException {
        User dbUser = userMapper.selectById(user.getId());
        Integer status = 1;
        if (Objects.equals(operationType, USER_NO_LOGIN)) {
            user.setType(NOLOGIN);
            //修改状态
            userMapper.updateById(user);
        } else if (Objects.equals(operationType, USER_LOGIN)) {
            user.setType(CUSTOMER);
            userMapper.updateById(user);
        } else if (Objects.equals(operationType, USER_PASSWORD)) {
            user.setPassword(bCryptPasswordEncoder.encode(getMD5("123456")));
            userMapper.updateById(user);
        } else if (Objects.equals(operationType, USER_UPDATE)) {
            //防止请求工具
            user.setType(dbUser.getType());
            if (!user.getPassword().equals(dbUser.getPassword())) {
                //密码修改
                user.setPassword(bCryptPasswordEncoder.encode(getMD5(user.getPassword())));
                status = 2;
            }
            if (!user.getNumber().equals(dbUser.getNumber())) {
                if (userMapper.selectOne(new QueryWrapper<User>().eq("number", user.getNumber())) != null) {
                    throw new ResultException(ResultStatus.NUM);
                }
            }
            //直接修改
            userMapper.updateById(user);
            if (status == 2) {
                //强制退出，重新登录
                SecurityContextHolder.clearContext();
                throw new ResultException(ResultStatus.NO_LOGIN);
            }
        } else if (Objects.equals(operationType, USER_ADD)) {
            //新增走注册接口
            register(user);
        } else if (Objects.equals(operationType, USER_DELETE)) {
            userMapper.deleteById(user.getId());
        }
    }

    @Override
    public LoginDto wxLogin(String code) throws ResultException {
        //登录凭证不能为空
        if (code == null || code.isEmpty()) {
            throw new ResultException(ResultStatus.NO_WX_CODE);
        }
        //请求参数
        String params = "appid=" + WX_APPID + "&secret="CHANGE_ME_BEFORE_RUNNING"&js_code=" + code + "&grant_type=" + GRANT_TYPE;
        //发送请求
        String openId = BaseFunction.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        List<String> roleList = new ArrayList<>();
        roleList.add("ROLE_USER");
        User wxUser = new User();
        if (JSONObject.parseObject(openId, WxLoginDto.class).getOpenid() == null) {
            throw new ResultException(ResultStatus.NO_WX_CODE);
        } else {
            wxUser.setOpenId(JSONObject.parseObject(openId, WxLoginDto.class).getOpenid());
            //查看数据库中是否存在信息
            if (userMapper.selectCount(new QueryWrapper<User>().eq("openId", wxUser.getOpenId())).equals(0)) {
                //存入数据库中，当作登录成功，构造Token，并且存入本地缓存中
                //初始化构造用户默认信息
                wxUser.setNumber(wxUser.getOpenId());
                wxUser.setType(CUSTOMER);
                wxUser.setTime(new Date());
                wxUser.setSex(MAN);
                wxUser.setPhone(NEW_CUSTOMER_PHONE);
                wxUser.setName(NEW_CUSTOMER);
                userMapper.insert(wxUser);
            } else {
                wxUser = userMapper.selectOne(new QueryWrapper<User>().eq("openId", wxUser.getOpenId()));
                if (wxUser.getType().equals(3)) {
                    throw new ResultException(ResultStatus.NOT_PASS_LOGIN);
                }
            }
            // 生成 token
            String token = JwtUtils.generateToken(wxUser.getOpenId(), roleList, false);

            // 认证成功后，设置认证信息到 Spring Security 上下文中
            Authentication authentication = JwtUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserLocal.setUser(wxUser);
            return new LoginDto(wxUser, SecurityConstants.TOKEN_PREFIX + token);
        }
    }

    @Override
    public User wxUserUpdate(WxUserVto wxUserVto) {
        User user = UserLocal.getUser();
        user.setSex(wxUserVto.getGender());
        user.setHeadimgUrl(wxUserVto.getAvatarUrl());
        user.setName(wxUserVto.getNickName());
        userMapper.updateById(user);
        return user;
    }

    @Override
    public void isLogin() throws ResultException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new ResultException(ResultStatus.NO_LOGIN);
        }
    }

}
