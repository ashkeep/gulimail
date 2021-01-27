package com.atguigu.gulimall.pms.service;

import com.atguigu.gulimall.pms.vo.AttrSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;


/**
 * 商品属性
 *
 * @author likun
 * @email 1393067551@qq.com
 * @date 2020-11-17 15:55:00
 */
public interface AttrService extends IService<AttrEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo queryPageCatelogBaseAttrs(QueryCondition queryCondition, Long catId,Integer attrType);

    void saveAttrAndRelation(AttrSaveVo attr);
}

