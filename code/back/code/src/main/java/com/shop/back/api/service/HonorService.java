package com.shop.back.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.entity.Goods;
import com.shop.back.api.entity.Honor;

public interface HonorService  extends IService<Honor> {
    Page<?> honorList(ListVto listVto);

    void operationHonor(Honor honor, Integer operationType);
}
