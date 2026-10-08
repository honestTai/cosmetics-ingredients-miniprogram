package com.shop.back.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.back.api.bean.dto.CommentDto;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.bean.vto.WxPageVto;
import com.shop.back.api.entity.Evaluation;
import com.shop.back.api.mapper.EvaluationMapper;
import com.shop.back.api.mapper.ForumMapper;
import com.shop.back.api.mapper.UserMapper;
import com.shop.back.api.service.EvaluationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.back.util.UserLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.shop.back.constant.operationConstants.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author @author
 */
@Service
public class EvaluationServiceImpl extends ServiceImpl<EvaluationMapper, Evaluation> implements EvaluationService {

    @Autowired
    EvaluationMapper evaluationMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ForumMapper forumMapper;

    /**
     * 根据化妆品查询评价
     * 需要评价内容，评价用户名称，评价id,评价时间
     * 每页5页进行分页
     *
     * @param listVto
     * @return
     */
    @Override
    public Page<?> evaluaList(ListVto listVto) {
        if (Objects.nonNull(listVto.getCommentType()) && listVto.getCommentType().equals(1)) {
            QueryWrapper<Evaluation> queryWrapper = new QueryWrapper<Evaluation>();
            if (StringUtils.isNotBlank(listVto.getLikeName())) {
                queryWrapper.like("content", listVto.getLikeName()).or().like("reply_content", listVto.getLikeName());
            }
            queryWrapper.eq("type", 1);
            Page<Evaluation> page = evaluationMapper.selectPage(new Page<Evaluation>(listVto.getPage(), listVto.getPageSize()), queryWrapper);
            //循环构造数据
            page.getRecords().forEach(x -> {
                x.setEvUser(userMapper.selectById(x.getUserId()));
                if (x.getReplyId() != null) {
                    x.setReUser(userMapper.selectById(x.getReplyId()));
                }
                x.setForum(forumMapper.selectById(x.getGoodsId()));
            });
            page.getRecords().stream().filter(x->x.getForum()!=null).collect(Collectors.toList());
            return page;
        } else {
            Page<CommentDto>  commentDtoPage = evaluationMapper.selectCommentListByPage(new Page<CommentDto>(listVto.getPage(), listVto.getPageSize()), listVto);
            commentDtoPage.getRecords().forEach(x->{
                if(StringUtils.isNotBlank(x.getPic())){
                    x.setPicList(Arrays.asList(x.getPic().split(",")));
                }
            });
            return commentDtoPage;
        }
    }

    /**
     * 修改评论
     * 添加评价回复内容
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operationEvaluation(Evaluation evaluation, Integer operationType) {
        //根据operationType 判断是修改（后台回复），还是删除
        if (Objects.equals(operationType, UPDATE)) {
            evaluation.setReplyId(UserLocal.getUser().getId());
            evaluationMapper.addReplyContent(evaluation);
        } else if (Objects.equals(operationType, DELETE)) {
            Evaluation evaluationDB = this.getById(evaluation.getId());
            if(evaluationDB.getType().equals(1)){
                forumMapper.updateCommnet(evaluation.getGoodsId(),-1);
            }
            evaluationMapper.deleteById(evaluation);
        }
    }

    @Override
    public Page<CommentDto> userCommentPage(WxPageVto wxPageVto) {
        return evaluationMapper.userCommentPage(new Page<CommentDto>(wxPageVto.getCurrent(), wxPageVto.getSize()), UserLocal.getUser().getId());
    }

    @Override
    public Page<CommentDto> goodsCommentPage(WxPageVto wxPageVto) {
        Page<CommentDto> commentDtoPage = evaluationMapper.goodsCommentPage(new Page<CommentDto>(wxPageVto.getCurrent(), wxPageVto.getSize()), wxPageVto.getGoodsId());
        commentDtoPage.getRecords().forEach(x -> {
            if (x.getPic() != null) {
                x.setPicList(Arrays.asList(x.getPic().split(",")));
            }
        });
        return commentDtoPage;
    }

}
