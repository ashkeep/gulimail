package com.atguigu.gulimall.ums.dao;

import com.atguigu.gulimall.ums.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author likun
 * @email 1393067551@qq.com
 * @date 2020-11-17 15:18:50
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
