package com.atguigu.gulimall.pms.vo.detail;

import lombok.Data;

@Data
public class CouponsVo {

    private String name;

    //0-优惠卷 1-满减 2-阶梯
    private Integer type;
}
