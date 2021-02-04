package com.atguigu.gulimall.cart.to;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class SkuCouponTo {

    private Long skuId;//商品Id

    private Long couponId;//优惠卷id

    private String desc;//优惠卷描述

    private BigDecimal amount;//优惠卷金额

}
