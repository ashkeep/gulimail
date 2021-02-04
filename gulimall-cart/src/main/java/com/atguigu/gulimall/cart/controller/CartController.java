package com.atguigu.gulimall.cart.controller;


import com.atguigu.gulimall.cart.service.CartService;
import com.atguigu.gulimall.cart.vo.CartVo;
import com.atguigu.gulimall.commons.bean.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import sun.applet.resources.MsgAppletViewer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@Api(tags = "购物车系统")
@RestController
@RequestMapping("/cart")
public class CartController {


    @Autowired
    CartService cartService;


    @Autowired
    @Qualifier("otherExecutor")
    ThreadPoolExecutor executor;


    public Resp<Object> closeThreadPool(){
        int activeCount = executor.getActiveCount();
        int corePoolSize = executor.getCorePoolSize();
        executor.shutdown();
        Map<String,Object> map = new HashMap<>();
        map.put("closeQueue", executor.getQueue().size());
        map.put("waitActiveCount", executor.getActiveCount());
        return Resp.ok(map);
    }


    /**
     * 某个请求参数有多个值封装数组
     * 传：skuId=1&skuId=2&skuId=3
     * 封：@RequestParam("skuId") Long[] skuId,
     * @param skuId
     * @param status  0代表不选中 1 代表选中
     * @param userKey
     * @param authorization
     * @return
     */
    @ApiOperation("选中/不选中")
    @PostMapping("/check")
    public Resp<CartVo> checkCart(@RequestParam("skuId") Long[] skuId,
                                 @RequestParam("status")Integer status,
                                 String userKey,
                                 @RequestHeader(name = "Authorization",required = false)String authorization){
        CartVo cartVo = cartService.checkCart(skuId,status,userKey,authorization);

        return Resp.ok(cartVo);

    }

    @ApiOperation("更新购物车数量")
    @PostMapping("/update")
    public Resp<CartVo> updateCart(@RequestParam(name = "skuId",required = true)Long skuId,
                                   @RequestParam(name = "num",defaultValue = "1")Integer num,
                                   String userKey,
                                   @RequestHeader(name = "Authorization",required = false)String authorization){


        CartVo cartVo = cartService.updateCart(skuId,num,userKey,authorization);

        return Resp.ok(cartVo);
    }




    /**
     * 以后购物车的所有操作，前端带两个令牌
     * 1）、登录以后的jwt放在请求头的authorization 字段（不一定带）
     * 2）、只要前端有收到服务器响应过一个userKey的东西，以后保存起来，访问所有请求都带上（不一定有）
     * @param userKey
     * @param authorization
     * @return
     */
    @ApiOperation("获取购物车中的数据")
    @GetMapping("/list")
    public Resp<CartVo> getCart(String userKey,
                                @RequestHeader(name = "Authorization",required = false) String authorization)throws ExecutionException, InterruptedException{
        CartVo cartVo = cartService.getCart(userKey,authorization);


        return Resp.ok(null);
    }


    /**
     *
     * @param skuId
     * @param num
     * @param userKey  临时用户的令牌，如果有就带
     * @param authorization
     * @return
     */
    @ApiOperation("将某个sku添加到购物车")
    @PostMapping("/add")
    public Resp<Object> addToCart(@RequestParam(name = "skuId",required = true) Long skuId,
                                  @RequestParam(name = "num",defaultValue = "1") Integer num,
                                  String userKey,
                                  @RequestHeader(name = "Authorization",required = false) String authorization){

        CartVo cartVo = cartService.addToCart(skuId,num,userKey,authorization);
        Map<String ,String> map = new HashMap<>();
        map.put("userKey",cartVo.getUserKey());

        return Resp.ok(map);
    }




}
