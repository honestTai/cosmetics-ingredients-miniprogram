package com.shop.back.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.entity.Evaluation;
import com.shop.back.api.entity.Forum;
import com.shop.back.api.entity.User;
import com.shop.back.api.mapper.EvaluationMapper;
import com.shop.back.api.mapper.ForumMapper;
import com.shop.back.api.mapper.UserMapper;
import com.shop.back.api.service.ForumService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ForumServiceImpl extends ServiceImpl<ForumMapper, Forum> implements ForumService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    EvaluationMapper evaluationMapper;

    @Autowired
    ForumMapper forumMapper;

    /**
     * 动态数据获取
     *
     * @param listVto 查询参数
     * @return
     */
    @Override
    public Page<?> forumList(ListVto listVto) {
        QueryWrapper<Forum> forumQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(listVto.getLikeName())) {
            forumQueryWrapper.like("info", listVto.getLikeName());
        }
        Page<Forum> forumPage = this.page(new Page<Forum>(listVto.getPage(), listVto.getPageSize()), forumQueryWrapper);
        forumPage.getRecords().forEach(forum -> {
            if (StringUtils.isNotBlank(forum.getPic())) {
                forum.setPicList(Arrays.asList(forum.getPic().split(",")));
            }
            User user = userMapper.selectById(forum.getUserId());
            forum.setUser(user);
        });
        return forumPage;
    }

    /**
     * 动态数据操作
     *
     * @param forum
     */
    @Override
    public void operationForum(Forum forum) {
        this.removeById(forum.getId());
        this.deleteComment(forum.getId());
    }

    @Override
    public void updateCommnet(Integer goodsId, int i) {
        forumMapper.updateCommnet(goodsId, i);
    }

    public void deleteComment(Integer id) {
        evaluationMapper.delete(new QueryWrapper<Evaluation>().eq("goods_id", id));
    }
}
