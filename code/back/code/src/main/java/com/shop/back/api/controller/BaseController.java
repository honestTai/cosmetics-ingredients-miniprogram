package com.shop.back.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.back.api.bean.dto.GoodsDto;
import com.shop.back.api.bean.general.result.ResultException;
import com.shop.back.api.bean.general.result.ResultStatus;
import com.shop.back.api.bean.vto.ListVto;
import com.shop.back.api.bean.vto.OperationVto;
import com.shop.back.api.service.*;
import com.shop.back.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.shop.back.constant.operationConstants.*;

/**
 * 需要注入Bean，但不能在对象类使用的方法，写在这个对象中
 */
public class BaseController {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ClassService classService;

    @Autowired
    GoodsService goodsService;


    @Autowired
    EvaluationService evaluationService;

    @Autowired
    ForumService forumService;

    @Autowired
    HonorService honorService;

    @Autowired
    HonorGoodsService honorGoodsService;

    @Autowired
    UserService userService;


    @Value("${file-path}")
    protected String filePath;

    @Value("${ip}")
    protected String fileUrl;

    public void isKey(String key) throws ResultException {
        if (redisUtil.isKey(key)) {
            throw new ResultException(ResultStatus.NO_LOGIN);
        }
    }

    public List<String> compareGoods(GoodsDto product, GoodsDto goods) {
        List<String> result = new ArrayList<>();
        // 比较价格
        int priceComparison = product.getGoods().getPrice().compareTo(goods.getGoods().getPrice());
        if (priceComparison > 0) {
            result.add(product.getGoods().getName() + "价格高于" + goods.getGoods().getName());
        } else if (priceComparison < 0) {
            result.add(product.getGoods().getName() + "价格低于" + goods.getGoods().getName());
        } else {
            result.add(product.getGoods().getName() + "价格与" + goods.getGoods().getName() + "相同");
        }
        // 比较得分
        int scoreComparison = Double.compare(product.getGoods().getScore(), goods.getGoods().getScore());
        if (scoreComparison > 0) {
            result.add(product.getGoods().getName() + "得分高于" + goods.getGoods().getName());
        } else if (scoreComparison < 0) {
            result.add(product.getGoods().getName() + "得分低于" + goods.getGoods().getName());
        } else {
            result.add(product.getGoods().getName() + "得分与" + goods.getGoods().getName() + "相同");
        }
        return result;
    }

    protected Page<?> getPage(ListVto listVto) throws ResultException {
        Integer listType = listVto.getListType();
        Page<?> resultPage = null;
        if (Objects.equals(listType, USER)) {
            resultPage = userService.userList(listVto);
        } else if (Objects.equals(listType, CLASS)) {
            resultPage = classService.classList(listVto);
        } else if (Objects.equals(listType, GOODS)) {
            resultPage = goodsService.goodsList(listVto);
        } else if (Objects.equals(listType, COMMENT)) {
            resultPage = evaluationService.evaluaList(listVto);
        }else if(Objects.equals(listType, FORUM)){
            resultPage = forumService.forumList(listVto);
        }else if(Objects.equals(listType, HONOR)){
            resultPage = honorService.honorList(listVto);
        }else if(Objects.equals(listType, HONOR_GOODS)){
            resultPage = honorGoodsService.honorGoodsList(listVto);
        }
        return resultPage;
    }

    protected void operationApi(OperationVto operationVto) throws ResultException {
        Integer operationType = operationVto.getOperationType();
        Integer type = operationVto.getType();
        if (Objects.equals(type, USER)) {
            userService.operationUser(operationVto.getUser(), operationType);
        } else if (Objects.equals(type, CLASS)) {
            classService.operationClass(operationVto.getAClass(), operationType);
        } else if (Objects.equals(type, GOODS)) {
            goodsService.operationGoods(operationVto.getGoods(), operationType);
        } else if (Objects.equals(type, COMMENT)) {
            evaluationService.operationEvaluation(operationVto.getEvaluation(), operationType);
        } else if(Objects.equals(type, FORUM)){
            forumService.operationForum(operationVto.getForum());
        }else if(Objects.equals(type, HONOR)){
            honorService.operationHonor(operationVto.getHonor(),operationType);
        }else if(Objects.equals(type, HONOR_GOODS)){
            honorGoodsService.operationHonorGoods(operationVto.getHonorGoods(),operationType);
        }
    }
}
