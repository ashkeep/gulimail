package com.atguigu.gulimall.sms.to;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class SkuReductionTo {

    private Long id;

    private Long skuId;

    private String desc;//描述

    private BigDecimal fullPrice;

    private BigDecimal reducePrice;

    private Integer addOther;

    private Integer fullCount;//满几件

    private BigDecimal discount;//打几折

    private Integer type;//满减类型，满几件打几折，还是满XXX减XXX元；0-打折  1-满减


}
