package com.shop.back.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.back.api.bean.dto.GoodsDto;
import com.shop.back.api.bean.general.result.ResultException;
import com.shop.back.api.bean.vto.GoodsPageWxVto;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @author

 */
public interface GoodsService extends IService<Goods> {

    Page<?> goodsList(ListVto listVto);

    void operationGoods(Goods goods, Integer operationType) throws ResultException;

    Page<?> goodsPageByWx(GoodsPageWxVto listVto);

    GoodsDto goodsDetail(Integer goodsId);

    Page<?> goodsScorePage(GoodsPageWxVto goodsPageWxVto);
}
