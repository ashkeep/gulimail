package com.atguigu.gulimall.pms.component;


import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.pms.annotation.GuliCache;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 1.导入aop的stater
 * 2.这是一个切面，同时放入容器
 * 3.申明通知方法和切入点方法
 */

@Slf4j
@Component
@Aspect
public class GuliCacheAspect {

    @Autowired
    StringRedisTemplate redisTemplate;

    ReentrantLock lock = new ReentrantLock();


    @Around("@annotation(com.atguigu.gulimall.pms.annotation.GuliCache)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;

        try {
            Object[] args = point.getArgs();//获取目标方法的所有参数
            //1。拿到注解的值
            //方法
            MethodSignature signature = (MethodSignature) point.getSignature();
            GuliCache guliCache = signature.getMethod().getAnnotation(GuliCache.class);
            if (guliCache == null){
                //无需缓存
                return point.proceed(args);
            }
            //需要缓存机制
            String prefix = guliCache.prefix();
            if (args != null){
                for (Object arg: args){
                    prefix += ":"+arg.toString();
                }
            }
            Object cache = getFromCache(prefix,signature);
            if (cache != null){
                return cache;
            }else{
                lock.lock();
                log.info("缓存切面介入工作。。。返回通知");
                cache = getFromCache(prefix, signature);
                if (cache == null){
                    System.out.println("缓存没有命中。。。");
                    result = point.proceed(args);
                    redisTemplate.opsForValue().set(prefix, JSON.toJSONString(result));
                    return  result;
                }else {
                    return cache;
                }

            }
        }catch (Exception e){

        }finally {
            log.info("缓存切面介入工作。。。。后置通知");
            if (lock.isLocked()){
                lock.unlock();
            }

        }
        return null;
    }

    private Object getFromCache(String prefix, MethodSignature signature) {
        String s = redisTemplate.opsForValue().get(prefix);
        if (StringUtils.isEmpty(s)){
            //如果拿到的数据不是null。缓存有数据。目标方法不用执行
            Class type = signature.getReturnType();//获取返回值类型
            return JSON.parseObject(s,type);
        }
        return null;
    }


    private void clearCurrentCache(String prefix){
        redisTemplate.delete(prefix);
    }


}
