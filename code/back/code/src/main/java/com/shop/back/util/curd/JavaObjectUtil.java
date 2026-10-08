package com.shop.back.util.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 通用查询构造器
 */
public class JavaObjectUtil {

    /**
     * 反射构造通用的查询器方法
     * 自定义了两个注解eq等于，like模糊
     *
     * @param t 传入进来的参数
     */
    public final static <T> QueryWrapper<T> queryWrapper(T t) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        //反射获取传入过来的参数对象字段，然后获取字段上的注解信息
        for (Field declaredField : t.getClass().getDeclaredFields()) {
            declaredField.setAccessible(true);
            //判断是否带有查询注解
            boolean eqSearch = declaredField.isAnnotationPresent(EqSearch.class);
            boolean likeSearch = declaredField.isAnnotationPresent(LikeSearch.class);
            boolean orderByAsc = declaredField.isAnnotationPresent(OrderByAsc.class);
            if (eqSearch) {
                //获取字段名称，然后判断该字段是否有值
                try {
                    Object data = declaredField.get(t);
                    if (Objects.nonNull(data)) {
                        queryWrapper.eq(declaredField.getName(), data);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            if (likeSearch) {
                //获取字段名称，然后判断该字段是否有值
                try {
                    Object data = declaredField.get(t);
                    if (Objects.nonNull(data)) {
                        queryWrapper.like(declaredField.getName(), "%" + String.valueOf(data) + "%");
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            if(orderByAsc){
                queryWrapper.orderByAsc(declaredField.getName());
            }
        }
        return queryWrapper;
    }
}
