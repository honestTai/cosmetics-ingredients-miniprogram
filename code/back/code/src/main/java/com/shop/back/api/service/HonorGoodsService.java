package com.shop.back.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.back.api.bean.general.result.ResultException;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.entity.Goods;
import com.shop.back.api.entity.Honor;
import com.shop.back.api.entity.HonorGoods;

import java.util.List;

public interface HonorGoodsService extends IService<HonorGoods> {
    void operationHonorGoods(HonorGoods honorGoods, Integer operationType) throws ResultException;

    Page<?> honorGoodsList(ListVto listVto);

    /**
     * 根据评分获取所有榜单下的商品
     * @param id
     * @return
     */
    List<HonorGoods> listByGoodsScore(Integer id);
}
