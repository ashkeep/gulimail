package com.atguigu.gulimall.oms.controller;

import com.atguigu.gulimall.oms.feign.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope  //可以从配置中心动态的获取配置
@RestController
public class HelloController {
    @Autowired
    WorldService worldService;

//    @Value("${content}")
    private String value = "";

    @GetMapping("/hello")
    public String hello(){
        String world = worldService.world();
       return "hello " + world+" "+value;
    }

}
