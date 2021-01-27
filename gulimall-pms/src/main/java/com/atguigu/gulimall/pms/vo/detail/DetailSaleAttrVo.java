package com.atguigu.gulimall.pms.vo.detail;


import lombok.Data;

@Data
public class DetailSaleAttrVo {
    private Long attrId;//营销属性的id
    private String attrName;//营销属性的名字
    private String[] attrValues;


}
