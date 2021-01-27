package com.atguigu.gulimall.pms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.Max;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.atguigu.gulimall.pms.feign")
public class PmsCloudConfig {


    /**
     * 自己的线程池
     */
    @Bean("mainThreadPool")
    public ThreadPoolExecutor threadPool(@Value("10") Integer coreSize
                                         ,@Value("1000") Integer max){

        return new ThreadPoolExecutor(coreSize,max ,0L
                , TimeUnit.SECONDS,new LinkedBlockingQueue<>(Integer.MAX_VALUE/2));

    }

}
