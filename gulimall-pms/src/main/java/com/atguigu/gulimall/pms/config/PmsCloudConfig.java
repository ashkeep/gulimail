package com.atguigu.gulimall.pms.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
@RefreshScope
public class PmsCloudConfig {

}
