package com.atguigu.gulimall.pms.config;



import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
/**
 * 数据源配置
 *
 * @author HelloWoodes
 */
@Configuration
@EnableConfigurationProperties({MybatisPlusProperties.class})
public class DataSourceProxyConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // paginationInterceptor.setLimit(你的最大单页限制数量，默认 500 条，小于 0 如 -1 不受限制);
        return paginationInterceptor;
    }

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource originDataSource(@Value("${spring.datasource.url}") String url){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(url);
        return hikariDataSource;
    }


    @Bean
    @Primary  //这个返回的对象是默认的数据源
    public DataSource dataSource(DataSource dataSource){

        return new DataSourceProxy(dataSource);
    }

    /*@ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource originDataSource(@Value("${spring.datasource.url}") String url){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(url);
        return hikariDataSource;
    }

    @Bean("dataSource")
    @Primary
    public DataSourceProxy dataSourceProxy(DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }




    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSourceProxy dataSourceProxy,
                                                   MybatisPlusProperties mybatisProperties) {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSourceProxy);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] mapperLocaltions = resolver
                    .getResources(mybatisProperties.getMapperLocations()[0]);
            bean.setMapperLocations(mapperLocaltions);

            if (StringUtils.isNotBlank(mybatisProperties.getConfigLocation())) {
                Resource[] resources = resolver.getResources(mybatisProperties.getConfigLocation());
                bean.setConfigLocation(resources[0]);
            }
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/
}
/*
@Configuration
@EnableConfigurationProperties({MybatisPlusProperties.class})
public class DataSourceProxyConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean
    public DataSourceProxy dataSourceProxy(DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSourceProxy dataSourceProxy,
                                                   MybatisPlusProperties mybatisProperties) {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSourceProxy);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] mapperLocaltions = resolver
                    .getResources(mybatisProperties.getMapperLocations()[0]);
            bean.setMapperLocations(mapperLocaltions);

            if (StringUtils.isNotBlank(mybatisProperties.getConfigLocation())) {
                Resource[] resources = resolver.getResources(mybatisProperties.getConfigLocation());
                bean.setConfigLocation(resources[0]);
            }
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
*/

