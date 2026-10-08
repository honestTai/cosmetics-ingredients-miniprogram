package com.shop.back.api.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.back.api.bean.dto.CommentDto;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.entity.Evaluation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author @author

 */
@Mapper
public interface EvaluationMapper extends BaseMapper<Evaluation> {

    Page<CommentDto> selectCommentListByPage(Page<CommentDto> commentDtoPage, ListVto listVto);

    void addReplyContent(Evaluation evaluation);

    Page<CommentDto> userCommentPage(Page<CommentDto> commentDtoPage, Integer id);

    Page<CommentDto> goodsCommentPage(Page<CommentDto> commentDtoPage, Integer goodsId);
}
