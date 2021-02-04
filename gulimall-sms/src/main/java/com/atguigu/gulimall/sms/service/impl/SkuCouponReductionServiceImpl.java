package com.atguigu.gulimall.sms.service.impl;


import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuSaleInfoTo;
import com.atguigu.gulimall.sms.dao.CouponDao;
import com.atguigu.gulimall.sms.dao.SkuFullReductionDao;
import com.atguigu.gulimall.sms.dao.SkuLadderDao;
import com.atguigu.gulimall.sms.entity.CouponEntity;
import com.atguigu.gulimall.sms.entity.SkuFullReductionEntity;
import com.atguigu.gulimall.sms.entity.SkuLadderEntity;
import com.atguigu.gulimall.sms.feign.SpuFeignService;
import com.atguigu.gulimall.sms.service.SkuCouponReductionService;
import com.atguigu.gulimall.sms.to.SkuCouponTo;
import com.atguigu.gulimall.sms.to.SkuInfoTo;
import com.atguigu.gulimall.sms.to.SkuReductionTo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkuCouponReductionServiceImpl implements SkuCouponReductionService {

    @Autowired
    SpuFeignService spuFeignService;

    @Autowired
    SkuLadderDao ladderDao;

    @Autowired
    SkuFullReductionDao reductionDao;

    @Autowired
    CouponDao couponDao;

    @Override
    public List<SkuCouponTo> getCoupons(Long skuId) {
        //1.根据skuId查出是spuId
        List<SkuCouponTo> skuCouponTos = new ArrayList<>();
        Resp<SkuInfoTo> info = spuFeignService.info(skuId);
        if (info.getData() != null){
            Long spuId = info.getData().getSpuId();
            List<CouponEntity> tos = couponDao.selectCouponsBySpuId(spuId);
            if (tos != null && tos.size() >0){
                for (CouponEntity to : tos) {
                    SkuCouponTo skuCouponTo = new SkuCouponTo();
                    skuCouponTo.setAmoount(to.getAmount());
                    skuCouponTo.setCouponId(to.getId());
                    skuCouponTo.setDesc(to.getCouponName());
                    skuCouponTo.setSkuId(skuId);
                    skuCouponTos.add(skuCouponTo);

                }
            }
        }

        return skuCouponTos;

    }

    @Override
    public List<SkuReductionTo> getReductions(Long skuId) {
        List<SkuLadderEntity> ladderEntities = ladderDao.selectList(new QueryWrapper<SkuLadderEntity>().eq("sku_id", skuId));
        List<SkuFullReductionEntity> reductionEntities = reductionDao.selectList(new QueryWrapper<SkuFullReductionEntity>().eq("sku_id", skuId));

        List<SkuReductionTo> tos = new ArrayList<>();
        ladderEntities.forEach((item)->{
            SkuReductionTo to = new SkuReductionTo();
            //0-打折
            BeanUtils.copyProperties(item, to);
            to.setDesc("满"+item.getFullCount()+"件，享受"+item.getDiscount()+"折优惠");
            to.setType(0);
            tos.add(to);
        });

        reductionEntities.forEach((item)->{
            SkuReductionTo to = new SkuReductionTo();
            //1-满减
            BeanUtils.copyProperties(item, to);
            to.setType(1);
            to.setDesc("消费满"+item.getFullPrice()+",减" + item.getReducePrice()+"");
            tos.add(to);
        });

        return tos;

    }
}
