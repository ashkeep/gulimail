package com.atguigu.gulimallpms.dao;

import com.atguigu.gulimall.pms.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author likun
 * @email 1393067551@qq.com
 * @date 2020-11-17 15:16:41
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
