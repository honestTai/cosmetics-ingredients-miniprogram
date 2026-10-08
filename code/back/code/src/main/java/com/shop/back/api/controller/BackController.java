package com.shop.back.api.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.back.api.bean.dto.StatisticsOrderDto;
import com.shop.back.api.bean.general.result.Result;
import com.shop.back.api.bean.general.result.ResultException;
import com.shop.back.api.bean.general.result.ResultStatus;
import com.shop.back.api.bean.vto.*;
import com.shop.back.api.entity.Class;
import com.shop.back.api.entity.Goods;
import com.shop.back.api.entity.HonorGoods;
import com.shop.back.api.entity.User;
import com.shop.back.api.service.*;
import com.shop.back.api.service.impl.HonorServiceImpl;
import com.shop.back.util.RedisUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.shop.back.constant.operationConstants.*;
import static com.shop.back.util.File.downLoad;
import static com.shop.back.util.File.upload;

/**
 * 后台统一Add,Update,delete,Select接口
 */
@RestController
@RequestMapping("/back")
public class BackController extends BaseController {

    /**
     * 后台用户登录
     * 管理员与商店用户
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginVto loginVto) throws ResultException {
        return Result.success(userService.login(loginVto));
    }



    /**
     * 退出登录
     */
    @GetMapping("/loginOut")
    public void loginOut() {
        SecurityContextHolder.clearContext();
    }

    /**
     * <p>
     * 查询所有列表
     * 统一名称模糊查询，分页查询
     * 化妆品带有分类查询
     * 代有化妆品名称模糊查询，化妆品分类查询，下单时间查询，用户名字模糊查询，用户手机号码模糊查询
     * 数据权限：
     * 如果登录的用户为商店用户，化妆品与查询带有商店标识
     * 0顾客信息列表,后台用户信息列表-admin-admin
     * 2分类列表-admin
     * 3化妆品列表-admin无需数据筛选merchant需要数据筛选
     * 5评价列表
     */
    @PostMapping("/list")
    public Page<?> list(@RequestBody ListVto listVto) throws ResultException {
        return getPage(listVto);
    }


    /**
     * 统一数据库增加，修改接口
     * operationType->0修改1新增
     * 修改传入不同的模块进行修改
     * 传入不同的实体-对应不同的模块，modelType（1，2，3，4）
     * 1用户模块
     * 2分类模块
     * 3化妆品模块
     * 4模块（仅仅修改其状态），默认配送完成后台修改
     * 5 评价回复
     * <p>
     * 三元需要return,这里使用if else
     * <p>
     */
    @PostMapping("/operation")
    public void operation(@RequestBody OperationVto operationVto) throws ResultException {
        operationApi(operationVto);
    }

    /**
     * 获取顶级分类接口
     * 不分页
     */
    @GetMapping("/parentClass")
    public List<Class> parentClass() {
        return classService.list(new QueryWrapper<Class>().eq("parent_id", 0));
    }

    /**
     * 获取末级分类
     * 不分页
     */
    @GetMapping("/childrenClass")
    public List<Class> childrenClass() {
        return classService.list(new QueryWrapper<Class>().ne("parent_id", 0));
    }

    @GetMapping("/getAllGoods")
    public List<Goods> getAllGoods() {
        return goodsService.list();
    }

    /**
     * 上传图片,登录拦截
     */
    @PostMapping("/uploadGoodsPhoto")
    @ResponseBody
    public Result uploadImgAddUser(@RequestParam("image") MultipartFile uploadFile) throws Exception {
        return upload(uploadFile, filePath, fileUrl);
    }


    /**
     * 上传不拦截
     */
    @PostMapping("/upload")
    @ResponseBody
    public Result uploadMerchantHead(@RequestParam("head") MultipartFile uploadFile) throws Exception {
        return upload(uploadFile, filePath, fileUrl);
    }


}
