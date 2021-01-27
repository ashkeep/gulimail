package com.atguigu.gulimall.sms.feign;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuSaleInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-pms")
public interface PmsSkuSaleInfoFeignService {

    @PostMapping("/pms/skubounds/saleinfo/save")
    public Resp<Object> saveSkuInfos(@RequestBody List<SkuSaleInfoTo> to);
}
