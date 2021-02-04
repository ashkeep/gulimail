package com.atguigu.gulimall.cart.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class CartConfig {




    @Bean("mainExecutor")
    @Primary//默认
    public ThreadPoolExecutor mainThreadPoolExecutor(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 1000, 0L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(Integer.MAX_VALUE/2));
        return executor;
    }


    @Bean("otherExecutor")
    public ThreadPoolExecutor noMainExecutor(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 1000, 0L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(Integer.MAX_VALUE/2));
        return executor;
    }

}
