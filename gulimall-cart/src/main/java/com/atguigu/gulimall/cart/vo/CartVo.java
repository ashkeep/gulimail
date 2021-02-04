package com.atguigu.gulimall.cart.vo;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 整个购物车
 */


public class CartVo {

    private Integer total;//商品总数量

    private BigDecimal totalPrice; //宗商品价格

    private BigDecimal reductionPrice; //优惠了的价格

    private BigDecimal cartPrice; //购物车应该支付的价格

    @Getter
    @Setter
    private List<CartItemVo> items; // 购物车所有的购物项

    @Getter
    @Setter
    private String userKey;//临时用户的key




}
