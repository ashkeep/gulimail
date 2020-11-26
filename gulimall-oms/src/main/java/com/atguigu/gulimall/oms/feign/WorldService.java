package com.atguigu.gulimall.oms.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient("gulimall-pms")
public interface WorldService {

    @GetMapping("/world")
    public String world();

}
