package com.shop.back.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.entity.Forum;

public interface ForumService  extends IService<Forum> {
    /**
     * 动态数据获取
     * @param listVto 查询参数
     * @return
     */
    Page<?> forumList(ListVto listVto);

    /**
     * 动态数据操作
     * @param forum
     */
    void operationForum(Forum forum);

    void updateCommnet(Integer goodsId, int i);
}
