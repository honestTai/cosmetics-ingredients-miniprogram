package com.shop.back.api.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.back.api.bean.dto.GoodsDto;
import com.shop.back.api.bean.vto.GoodsPageWxVto;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.back.api.entity.HonorGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author @author

 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    IPage<GoodsDto> selectGoodsByPage(Page<GoodsDto> page, ListVto listVto);

    IPage<GoodsDto> selectGoodsByPageWx(Page<GoodsDto> page, GoodsPageWxVto goodsPageWxVto);

    GoodsDto selectGoodsByWxFromId(Integer goodsId);

    IPage<GoodsDto> goodsScorePage(Page<GoodsDto> page, GoodsPageWxVto goodsPageWxVto);

    List<HonorGoods> listByGoodsScore(Integer id);
}
