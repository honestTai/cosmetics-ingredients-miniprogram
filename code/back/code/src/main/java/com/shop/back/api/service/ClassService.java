package com.shop.back.api.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.entity.Class;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @author

 */
public interface ClassService extends IService<Class> {

    Page<?> classList(ListVto listVto);

    void operationClass(Class aClass, Integer operationType);

    List<Class> classTree();
}
