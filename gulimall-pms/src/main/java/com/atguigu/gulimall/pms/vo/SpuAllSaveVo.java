package com.atguigu.gulimall.pms.vo;

import com.atguigu.gulimall.pms.entity.SpuInfoEntity;
import lombok.Data;

import java.util.List;

@Data
public class SpuAllSaveVo extends SpuInfoEntity {

    //spu详情图
    private String[] spuImages;

    //当前spu的所有基本属性
    private List<BaseAttrVo> baseAttrs;

    //dang当前spu对应的所有sku信息
    private List<SkuVo> skus;


}
