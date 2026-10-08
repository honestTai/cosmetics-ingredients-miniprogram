package com.shop.back.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.back.api.bean.dto.CommentDto;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.bean.vto.WxPageVto;
import com.shop.back.api.entity.Evaluation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @author

 */
public interface EvaluationService extends IService<Evaluation> {

    Page<?> evaluaList(ListVto listVto);

    void operationEvaluation(Evaluation evaluation,Integer operationType);

    Page<CommentDto> userCommentPage(WxPageVto wxPageVto);

    Page<CommentDto> goodsCommentPage(WxPageVto wxPageVto);
}
