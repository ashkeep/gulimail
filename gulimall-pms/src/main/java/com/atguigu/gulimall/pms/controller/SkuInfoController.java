package com.atguigu.gulimall.pms.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.pms.entity.SkuInfoEntity;
import com.atguigu.gulimall.pms.service.SkuInfoService;




/**
 * sku信息
 *
 * @author likun
 * @email 1393067551@qq.com
 * @date 2020-11-17 15:55:00
 */
@Api(tags = "sku信息管理")
@RestController
@RequestMapping("pms/skuinfo")
public class SkuInfoController {
    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/cart/{skuId}")
    public Resp<SkuInfoVo> getSkuinfoForCart(@PathVariable("skuId")Long skuId){
        SkuInfoVo vo = skuInfoService.getSkuVo(skuId);

        return Resp.ok(vo);
    }


    ///list/spu/{spuId}
    @GetMapping("/list/spu/{spuId}")
    @ApiOperation("根据商品Id（spuId）查出所有的sku信息")
    public Resp<List<SkuInfoEntity>> spuSkuInfo(
            @PathVariable("spuId") Long SpuId){
        System.out.println(SpuId);
        List<SkuInfoEntity> skus = skuInfoService.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", SpuId));


        return Resp.ok(skus);
    }

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('pms:skuinfo:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = skuInfoService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{skuId}")
    @PreAuthorize("hasAuthority('pms:skuinfo:info')")
    public Resp<SkuInfoEntity> info(@PathVariable("skuId") Long skuId){
		SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return Resp.ok(skuInfo);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('pms:skuinfo:save')")
    public Resp<Object> save(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.save(skuInfo);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('pms:skuinfo:update')")
    public Resp<Object> update(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.updateById(skuInfo);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('pms:skuinfo:delete')")
    public Resp<Object> delete(@RequestBody Long[] skuIds){
		skuInfoService.removeByIds(Arrays.asList(skuIds));

        return Resp.ok(null);
    }

}
