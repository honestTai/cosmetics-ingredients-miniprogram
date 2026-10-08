package com.shop.back.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.back.api.bean.dto.GoodsDto;
import com.shop.back.api.bean.general.result.ResultException;
import com.shop.back.api.bean.general.result.ResultStatus;
import com.shop.back.api.bean.vto.GoodsIntroduceVto;
import com.shop.back.api.bean.vto.GoodsPageWxVto;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.entity.*;
import com.shop.back.api.mapper.EvaluationMapper;
import com.shop.back.api.mapper.GoodsMapper;
import com.shop.back.api.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.back.util.UserLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.shop.back.constant.operationConstants.*;
import static com.shop.back.util.BaseFunction.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author @author
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    EvaluationMapper evaluationMapper;

    /**
     * 分页查询
     * 化妆品名字模糊查询，公司id查询，分类查询
     * 判断角色如果是ADMIN,可以进行公司ID查询
     * jsonString 转 JSON
     * 手写SQL查询
     *
     * @param listVto
     * @return
     */
    @Override
    public Page<?> goodsList(ListVto listVto) {
        Page<GoodsDto> page = new Page<>(listVto.getPage(), listVto.getPageSize());
        IPage<GoodsDto> goodsDtoIPage = goodsMapper.selectGoodsByPage(page, listVto);
        //循环数据转List<Java>
        goodsDtoIPage.getRecords().forEach(goodsDto -> {
            if (goodsDto.getGoods().getImages() != null && !goodsDto.getGoods().getImages().isEmpty()) {
                goodsDto.getGoods().setGoodsPhotoList(stringToList(goodsDto.getGoods().getImages()));
            }
            if (goodsDto.getGoods().getSortIntroduce() != null && !goodsDto.getGoods().getSortIntroduce().isEmpty()) {
                goodsDto.getGoods().setSortIntroduces(JSON.parseArray(goodsDto.getGoods().getSortIntroduce(), GoodsIntroduceVto.class));
            }
        });
        return (Page<GoodsDto>) goodsDtoIPage;
    }

    @Override
    public Page<?> goodsPageByWx(GoodsPageWxVto goodsPageWxVto) {
        Page<GoodsDto> page = new Page<>(goodsPageWxVto.getCurrent(), goodsPageWxVto.getSize());
        IPage<GoodsDto> goodsDtoIPage = goodsMapper.selectGoodsByPageWx(page, goodsPageWxVto);
        goodsDtoIPage.getRecords().forEach(goodsDto -> {
            if (goodsDto.getGoods().getImages() != null && !goodsDto.getGoods().getImages().isEmpty()) {
                goodsDto.getGoods().setGoodsPhotoList(stringToList(goodsDto.getGoods().getImages()));
            }
            if (goodsDto.getGoods().getSortIntroduce() != null && !goodsDto.getGoods().getSortIntroduce().isEmpty()) {
                goodsDto.getGoods().setSortIntroduces(JSON.parseArray(goodsDto.getGoods().getSortIntroduce(), GoodsIntroduceVto.class));
            }
        });
        return (Page<?>) goodsDtoIPage;
    }

    @Override
    public GoodsDto goodsDetail(Integer goodsId) {
        GoodsDto goods = goodsMapper.selectGoodsByWxFromId(goodsId);
        if (goods.getGoods().getImages() != null && !goods.getGoods().getImages().isEmpty()) {
            goods.getGoods().setGoodsPhotoList(stringToList(goods.getGoods().getImages()));
        }
        if (goods.getGoods().getSortIntroduce() != null && !goods.getGoods().getSortIntroduce().isEmpty()) {
            goods.getGoods().setSortIntroduces(JSON.parseArray(goods.getGoods().getSortIntroduce(), GoodsIntroduceVto.class));
        }
        List<Evaluation> evaluationList = evaluationMapper.selectList(new QueryWrapper<Evaluation>().eq("goods_id",goodsId));
        //算出综合评分，平均评分，score
        goods.getGoods().setScore(evaluationList.stream().mapToDouble(Evaluation::getScore).average().orElse(0.0));
        goods.setCommentTotal(evaluationList.size());

        return goods;
    }

    @Override
    public Page<?> goodsScorePage(GoodsPageWxVto goodsPageWxVto) {
        Page<GoodsDto> page = new Page<>(goodsPageWxVto.getCurrent(), goodsPageWxVto.getSize());
        IPage<GoodsDto> goodsDtoIPage = goodsMapper.goodsScorePage(page, goodsPageWxVto);
        goodsDtoIPage.getRecords().forEach(goodsDto -> {
            if (goodsDto.getGoods().getImages() != null && !goodsDto.getGoods().getImages().isEmpty()) {
                goodsDto.getGoods().setGoodsPhotoList(stringToList(goodsDto.getGoods().getImages()));
            }
            if (goodsDto.getGoods().getSortIntroduce() != null && !goodsDto.getGoods().getSortIntroduce().isEmpty()) {
                goodsDto.getGoods().setSortIntroduces(JSON.parseArray(goodsDto.getGoods().getSortIntroduce(), GoodsIntroduceVto.class));
            }
        });
        return (Page<?>) goodsDtoIPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operationGoods(Goods goods, Integer operationType) {
        if (goods.getGoodsPhotoList() != null) {
            goods.setImages(!goods.getGoodsPhotoList().isEmpty() ? listToString(goods.getGoodsPhotoList()) : "");
        }
        //转JSON
        goods.setSortIntroduce(JSON.toJSONString(goods.getSortIntroduces()));
        if (Objects.equals(operationType, UPDATE)) {
            goodsMapper.updateById(goods);
        } else if (Objects.equals(operationType, DELETE)) {
            //化妆品删除
            goodsMapper.deleteById(goods);
        } else if (Objects.equals(operationType, ADD)) {
            goodsMapper.insert(goods);
        }
    }

}
