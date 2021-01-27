package com.atguigu.gulimall.pms.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuVo {

    private String skuName;
    private String skuDesc;
    private String skuTitle;
    private String skuSubtitle;
    private BigDecimal weight;
    private BigDecimal price;
    private String[] images;
    //==================以上sku基本信息===================

    //当前sku对应的销售信息
    private List<SaleAttrVo> saleAttrs;
    //=================以上sku对应的销售属性组合===========


    private BigDecimal growBounds;
    private BigDecimal buyBounds;
    /**
     * 优惠生效情况【1111】（四个状态位，co从左到右）
     * 0 - 无优惠，成长积分是否赠送；
     * 1 - 无优惠，购物积分是否赠送
     * 2 - 有优惠，成长积分受否赠送
     * 3 - 有优惠，购物积分是否赠送【状态位0：不赠送，1：赠送】
    */

     private Integer[] work;
     //上面是积分设置的信息

    private Integer fullCount;
    private BigDecimal discount;
    private Integer ladderAddOther;
    //上面是  阶梯价格信息

    /**
     * "fullPrice": 0, //满多少
     * "reducePrice": 0, //减多少
     * "fullAddOther": 0, //满减是否可以叠加其他优惠
     */
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer fullAddOther;



}
