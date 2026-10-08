package com.shop.back.api.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author @author

 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    Page<User> selectUserByPage(Page<Object> objectPage, ListVto listVto);
}
