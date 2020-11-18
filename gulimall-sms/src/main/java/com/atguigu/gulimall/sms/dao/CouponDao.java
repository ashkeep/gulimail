package com.atguigu.gulimall.sms.dao;

import com.atguigu.gulimall.sms.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author likun
 * @email 1393067551@qq.com
 * @date 2020-11-17 15:58:30
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
