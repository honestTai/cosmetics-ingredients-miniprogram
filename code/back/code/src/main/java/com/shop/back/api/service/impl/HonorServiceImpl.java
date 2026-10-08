package com.shop.back.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.entity.Forum;
import com.shop.back.api.entity.Honor;
import com.shop.back.api.entity.HonorGoods;
import com.shop.back.api.mapper.HonorGoodsMapper;
import com.shop.back.api.mapper.HonorMapper;
import com.shop.back.api.service.HonorService;
import com.shop.back.constant.operationConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class HonorServiceImpl extends ServiceImpl<HonorMapper, Honor> implements HonorService {

    @Autowired
    HonorGoodsMapper honorGoodsMapper;

    /**
     * 查询榜单的数据
     * @param listVto 查询参数
     * @return
     */
    public Page<?> honorList(ListVto listVto) {
        QueryWrapper<Honor> honorQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(listVto.getLikeName())) {
            honorQueryWrapper.like("name", listVto.getLikeName());
        }
        Page<Honor> forumPage = this.page(new Page<Honor>(listVto.getPage(), listVto.getPageSize()), honorQueryWrapper);
        return forumPage;
    }

    /**
     * 榜单数据的操作
     * @param honor
     * @param operationType
     */
    @Transactional
    public void operationHonor(Honor honor, Integer operationType) {
        if (Objects.equals(operationType, operationConstants.ADD)) {
            this.save(honor);
        } else if (Objects.equals(operationType, operationConstants.UPDATE)) {
            this.updateById(honor);
        } else if (Objects.equals(operationType, operationConstants.DELETE)) {
            this.removeById(honor.getId());
            //删除榜单下的数据
            honorGoodsMapper.delete(new QueryWrapper<HonorGoods>().eq("honor_id", honor.getId()));
        }
    }
}
