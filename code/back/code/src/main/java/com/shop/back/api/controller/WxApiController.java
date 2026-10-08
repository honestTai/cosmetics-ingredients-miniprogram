package com.shop.back.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.back.api.bean.dto.CommentDto;
import com.shop.back.api.bean.dto.GoodsDto;
import com.shop.back.api.bean.general.result.Result;
import com.shop.back.api.bean.general.result.ResultException;
import com.shop.back.api.bean.vto.*;
import com.shop.back.api.entity.*;
import com.shop.back.api.entity.Class;
import com.shop.back.api.service.*;
import com.shop.back.api.service.impl.ForumServiceImpl;
import com.shop.back.util.UserLocal;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.shop.back.util.BaseFunction.listToString;

/**
 * <p>
 * 微信小程序控制接口
 * </p>
 *
 * @author @author
 */
@RestController
@RequestMapping("/wx")
public class WxApiController extends BaseController{

    /**
     * 小程序登录
     *
     * @param wxLoginVto 小程序的Code
     * @return
     * @throws ResultException 自定义的错误
     */
    @PostMapping("/login")
    public Result login(@RequestBody WxLoginVto wxLoginVto) throws ResultException {
        return Result.success(userService.wxLogin(wxLoginVto.getCode()));
    }

    /**
     * 化妆品类接口
     * 新品
     * 热销化妆品
     * 分页查询
     */
    @PostMapping("/goodsPage")
    public Result goodsPage(@RequestBody GoodsPageWxVto goodsPageWxVto) {
        return Result.success(goodsService.goodsPageByWx(goodsPageWxVto));
    }

    /**
     * 化妆品分类接口获取
     */
    @GetMapping("/tree")
    public List<Class> classTree() {
        return classService.classTree();
    }

    /**
     * 化妆品详情查看接口
     */
    @GetMapping("/goodsDetail/{goodsId}")
    public GoodsDto goodsDetail(@PathVariable("goodsId") Integer goodsId) {
        return goodsService.goodsDetail(goodsId);
    }


    /**
     * 获取用户的登录信息
     */
    @GetMapping("/wxUser")
    public User user() {
        return UserLocal.getUser();
    }


    /**
     * 信息同步
     */
    @PostMapping("/wxUserUpdate")
    public User wxUserUpdate(@RequestBody WxUserVto wxUserVto) {
        return userService.wxUserUpdate(wxUserVto);
    }


    /**
     * 评价
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/orderCv")
    public void orderCv(@RequestBody Evaluation evaluation) {
        evaluation.setTime(new Date());
        evaluation.setUserId(UserLocal.getUser().getId());
        evaluationService.save(evaluation);
    }

    /**
     * 评价获取
     */
    @PostMapping("/userCommentPage")
    public Page<CommentDto> userCommentPage(@RequestBody WxPageVto wxPageVto) {
        return evaluationService.userCommentPage(wxPageVto);
    }

    /**
     * 单个化妆品评价查看
     */
    @PostMapping("/goodsCommentPage")
    public Page<CommentDto> goodsCommentPage(@RequestBody WxPageVto wxPageVto) {
        return evaluationService.goodsCommentPage(wxPageVto);
    }

    /**
     * 查看发布的动态内容
     */
    @PostMapping("/forumPage")
    public Page<Forum> dynamicPage(@RequestBody WxPageVto wxPageVto) {
        QueryWrapper<Forum> forumQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(wxPageVto.getLikeSearch())) {
            forumQueryWrapper.like("info", wxPageVto.getLikeSearch());
        }
        Page<Forum> forumPage = forumService.page(new Page<Forum>(wxPageVto.getCurrent(), wxPageVto.getSize()), forumQueryWrapper);
        forumPage.getRecords().forEach(forum -> {
            if (StringUtils.isNotBlank(forum.getPic())) {
                forum.setPicList(Arrays.asList(forum.getPic().split(",")));
            }
            User user = userService.getById(forum.getUserId());
            forum.setUser(user);
        });
        return forumPage;
    }

    /**
     * 发布动态内容
     */
    @PostMapping("/forum")
    public void forum(@RequestBody Forum forum) {
        forum.setUserId(Objects.requireNonNull(UserLocal.getUser()).getId());
        //如果没图片就转换写上去
        if (forum.getPicList() != null) {
            forum.setPic(!forum.getPicList().isEmpty() ? listToString(forum.getPicList()) : "");
        }
        forum.setTime(new Date());
        forum.setComment(0);
        forum.setView(0);
        forumService.save(forum);
    }

    /**
     * 获取单条发布的内容
     */
    @GetMapping("/forum/{id}")
    @Transactional
    public Forum forum(@PathVariable Integer id) {
        Forum forum = forumService.getById(id);
        forum.setView(forum.getView() + 1);
        forumService.updateById(forum);
        if (StringUtils.isNotBlank(forum.getPic())) {
            forum.setPicList(Arrays.asList(forum.getPic().split(",")));
        }
        User user = userService.getById(forum.getUserId());
        forum.setUser(user);
        List<Evaluation> evaluations = evaluationService.list(new QueryWrapper<Evaluation>().eq("type", 1).eq("goods_id", id));
        evaluations.forEach(x -> {
            x.setEvUser(userService.getById(x.getUserId()));
            if (x.getReplyId() != null) {
                x.setReUser(userService.getById(x.getReplyId()));
            }
        });
        forum.setEvaluations(evaluations);
        return forum;
    }

    /**
     * 动态的评论
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/comment")
    public void comment(@RequestBody Evaluation evaluation) {
        if (evaluation.getId().equals(0)) {
            forumService.updateCommnet(evaluation.getGoodsId(),1);
            evaluation.setTime(new Date());
            evaluation.setUserId(Objects.requireNonNull(UserLocal.getUser()).getId());
            evaluation.setType(1);
            evaluationService.save(evaluation);
        } else {
            Evaluation evaluationDb = evaluationService.getById(evaluation.getId());
            evaluationDb.setReplyId(Objects.requireNonNull(UserLocal.getUser()).getId());
            evaluationDb.setReplyContent(evaluation.getContent());
            evaluationDb.setReplyTime(new Date());
            this.evaluationService.updateById(evaluationDb);
        }
    }

    /**
     * 产品的评论
     */
    @PostMapping("/goodsComment")
    public void goodsComment(@RequestBody Evaluation evaluation) {
        evaluation.setType(0);
        evaluation.setTime(new Date());
        evaluation.setUserId(Objects.requireNonNull(UserLocal.getUser()).getId());
        evaluation.setPic(!evaluation.getPicList().isEmpty() ? listToString(evaluation.getPicList()) : null);
        evaluationService.save(evaluation);
    }

    /**
     * 小程序榜单列表获取
     */
    @GetMapping("/honorGet")
    public List<Honor> honorGet() {
        return honorService.list();
    }

    /**
     * 榜单下的化妆品数据获取
     *
     * @param id 榜单id
     * @return
     */
    @GetMapping("/honorGoods/{id}")
    public List<Goods> honorGoods(@PathVariable("id") Integer id) {
        List<HonorGoods> honorGoods = honorGoodsService.listByGoodsScore(id);
        return honorGoods.stream().map(x -> {
            Goods goods = goodsService.getById(x.getGoodsId());
            goods.setReason(x.getReason());
            goods.setGoodsPhotoList(Arrays.asList(goods.getImages().split(",")));
            goods.setOrders(x.getOrders());
            goods.setClassName(classService.getById(goods.getClassId()).getName());
            goods.setScore(x.getScore());
            return goods;
        }).collect(Collectors.toList());
    }

    /**
     * 评分排序
     */
    @PostMapping("/goodsScorePage")
    public Result goodsScorePage(@RequestBody GoodsPageWxVto goodsPageWxVto) {
        return Result.success(goodsService.goodsScorePage(goodsPageWxVto));
    }

    /**
     * 获取自己发布的内容
     */
    @GetMapping("/myForumGet")
    public List<Forum> myForumGet() {
        User user = UserLocal.getUser();
        QueryWrapper<Forum> forumQueryWrapper = new QueryWrapper<>();
        forumQueryWrapper.eq("user_id", user.getId());
        List<Forum> forums = forumService.list(forumQueryWrapper);
        forums.forEach(x -> {
            x.setUser(userService.getById(x.getUserId()));
        });
        return forums;
    }

    /**
     * 获取自己的评论内容
     */
    @GetMapping("/myComment")
    public List<Evaluation> myComment() {
        User user = UserLocal.getUser();
        QueryWrapper<Evaluation> evaluationQueryWrapper = new QueryWrapper<>();
        evaluationQueryWrapper.eq("user_id", user.getId());
        List<Evaluation> evaluations = evaluationService.list(evaluationQueryWrapper);
        evaluations.forEach(x -> {
            x.setEvUser(userService.getById(x.getUserId()));
            if (x.getType().equals(0)) {
                x.setTitleName(goodsService.getById(x.getGoodsId()).getName());
            } else {
                x.setTitleName(forumService.getById(x.getGoodsId()).getTitle());
            }
        });
        return evaluations;
    }

    /**
     * 删除数据
     */
    @GetMapping("/delForum/{id}")
    @Transactional(rollbackFor = Exception.class)
    public void delForum(@PathVariable("id") Integer id) {
        forumService.removeById(id);
        evaluationService.remove(new QueryWrapper<Evaluation>().eq("goods_id",id));
    }

    /**
     * 删除数据
     */
    @GetMapping("/delComment/{id}")
    @Transactional(rollbackFor = Exception.class)
    public void delComment(@PathVariable("id") Integer id) {
        Evaluation evaluation = evaluationService.getById(id);
        if(evaluation.getType().equals(1)){
            forumService.updateCommnet(evaluation.getGoodsId(),-1);
        }
        evaluationService.removeById(id);
    }

    /**
     * 获取所有化妆品
     */
    @GetMapping("/getAllGoods/{id}")
    public List<Goods> getAllGoods(@PathVariable("id") Integer id) {
        return goodsService.list(new QueryWrapper<Goods>().ne("id", id));
    }

    @GetMapping("/checkProduct/{pid}/{id}")
    public Map checkProduct(@PathVariable("pid") Integer pid, @PathVariable("id") Integer id) {
        Map<String, Object> map = new HashMap<>();
        GoodsDto product = goodsService.goodsDetail(pid);
        GoodsDto goods = goodsService.goodsDetail(id);
        //进行比较，返回比较后的结果
        map.put("result", compareGoods(product, goods));
        map.put("product", product);
        map.put("goods", goods);
        return map;
    }
}
