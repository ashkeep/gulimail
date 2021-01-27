package com.atguigu.gulimallgateway.config;


import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;

import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

    @Configuration
    public class GulimallGateWayConfig {

        /**
         * Gateway；
         *  Reactive；  Webflux；
         *
         * @return
         */
        @Bean
        public CorsWebFilter corsWebFilter(){

            //跨域的配置
            CorsConfiguration config = new CorsConfiguration();
            config.addAllowedMethod("*");
            config.addAllowedOrigin("*");
            config.addAllowedHeader("*");
            config.setAllowCredentials(true);//允许带cookie的跨域


            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

            source.registerCorsConfiguration("/**",config);

            CorsWebFilter filter = new CorsWebFilter(source);

            return filter;
        }
}
