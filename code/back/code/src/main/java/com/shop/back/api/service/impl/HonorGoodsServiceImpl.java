package com.shop.back.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.back.api.bean.general.result.ResultException;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.entity.Honor;
import com.shop.back.api.entity.HonorGoods;
import com.shop.back.api.mapper.ClassMapper;
import com.shop.back.api.mapper.GoodsMapper;
import com.shop.back.api.mapper.HonorGoodsMapper;
import com.shop.back.api.mapper.HonorMapper;
import com.shop.back.api.service.HonorGoodsService;
import com.shop.back.constant.operationConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.shop.back.api.bean.general.result.ResultStatus.WAIT_ADD_HONOR_GOODS;

@Service
public class HonorGoodsServiceImpl extends ServiceImpl<HonorGoodsMapper, HonorGoods> implements HonorGoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    HonorMapper honorMapper;

    @Autowired
    ClassMapper classMapper;

    @Override
    public void operationHonorGoods(HonorGoods honorGoods, Integer operationType) throws ResultException {
        if (Objects.equals(operationType, operationConstants.ADD)) {
            int count = this.count(new QueryWrapper<HonorGoods>().eq("honor_id", honorGoods.getHonorId()).eq("goods_id", honorGoods.getGoodsId()));
            if(count>0){
                throw new ResultException(WAIT_ADD_HONOR_GOODS);
            }
            this.save(honorGoods);
        } else if (Objects.equals(operationType, operationConstants.UPDATE)) {
            this.updateById(honorGoods);
        } else if (Objects.equals(operationType, operationConstants.DELETE)) {
            this.removeById(honorGoods.getId());
        }
    }

    @Override
    public Page<?> honorGoodsList(ListVto listVto) {
        QueryWrapper<HonorGoods> honorGoodsQueryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(listVto.getGoodsId())) {
            honorGoodsQueryWrapper.like("goods_id", listVto.getGoodsId());
        }
        Page<HonorGoods> honorGoodsPage = this.page(new Page<HonorGoods>(listVto.getPage(), listVto.getPageSize()), honorGoodsQueryWrapper);
        honorGoodsPage.getRecords().forEach(x->{
            x.setGoods(goodsMapper.selectById(x.getGoodsId()));
            x.setHonor(honorMapper.selectById(x.getHonorId()));
            x.setAClass(classMapper.selectById(x.getGoods().getClassId()));
        });
        return honorGoodsPage;
    }

    /**
     * 根据评分获取所有榜单下的商品
     *
     * @param id
     * @return
     */
    @Override
    public List<HonorGoods> listByGoodsScore(Integer id) {
        return goodsMapper.listByGoodsScore(id);
    }
}
