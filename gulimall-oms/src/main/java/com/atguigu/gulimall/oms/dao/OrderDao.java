package com.atguigu.gulimall.oms.dao;

import com.atguigu.gulimall.oms.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author likun
 * @email 1393067551@qq.com
 * @date 2020-11-17 15:23:16
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
