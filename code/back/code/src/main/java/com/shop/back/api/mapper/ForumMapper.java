package com.shop.back.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.back.api.entity.Forum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ForumMapper extends BaseMapper<Forum> {

    @Update("update forum set comment = comment + #{i} where id = #{goodsId}")
    void updateCommnet(Integer goodsId, int i);
}
