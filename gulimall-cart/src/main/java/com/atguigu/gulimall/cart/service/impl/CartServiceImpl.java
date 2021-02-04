package com.atguigu.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.cart.feign.SkuCouponRedutionFeignService;
import com.atguigu.gulimall.cart.feign.SkuFeignService;
import com.atguigu.gulimall.cart.service.CartService;
import com.atguigu.gulimall.cart.to.SkuCouponTo;
import com.atguigu.gulimall.cart.vo.CartItemVo;
import com.atguigu.gulimall.cart.vo.CartVo;
import com.atguigu.gulimall.cart.vo.SkuCouponVo;
import com.atguigu.gulimall.cart.vo.SkuFullReductionVo;
import com.atguigu.gulimall.commons.bean.Constant;
import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuInfoVo;
import com.atguigu.gulimall.commons.utils.GuliJwtUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    RedissonClient redisson;

    @Autowired
    SkuFeignService skuFeignService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    @Qualifier("mainExecutor")
    ThreadPoolExecutor mainExecutor;

    @Autowired
    SkuCouponRedutionFeignService skuCouponRedutionFeignService;


    @Override
    public CartVo getCart(String userKey, String authorization) throws ExecutionException, InterruptedException {
        CartVo cartVo = new CartVo();

        CartKey cartKey = getKey(userKey,authorization);
        //购物车在Redis中保存
        if(cartKey.isLogin()){
            //如果登陆了就合并购物车
            mergerCart(userKey,Long.parseLong(cartKey.getKey()));
        }else{
            cartVo.setUserKey(cartKey.getKey());
        }

        //获取购物车的购物项
        List<CartItemVo> cartItemVos = getCartItems(cartKey.getKey());
        cartVo.setItems(cartItemVos);
        return cartVo;
    }


    @Override
    public CartVo addToCart(Long skuId, Integer num, String userKey, String authorization) {
        CartVo cartVo = new CartVo();
        CartKey key = getKey(userKey, authorization);
        String cartKey = key.getKey();

        //1.获取购物车
        RMap<Object, Object> map = redisson.getMap(Constant.CART_PREFIX + cartKey);

        //2.添加购物车之前先确认购物车中有没有这个商品，如果有就数量加1  如果没有新增
        String item = (String) map.get(skuId.toString());
        if (!StringUtils.isEmpty(item)){
            CartItemVo itemVo = JSON.parseObject(item, CartItemVo.class);
            itemVo.setNum(itemVo.getNum()+num);
            map.put(skuId.toString(), JSON.toJSONString(itemVo));
        }else{
            //1.查询sku当前商品的详情
            Resp<SkuInfoVo> sKuInfoForCart = skuFeignService.getSKuInfoForCart(skuId);
            SkuInfoVo data = sKuInfoForCart.getData();

            //2.购物项
            CartItemVo itemVo = new CartItemVo();
            BeanUtils.copyProperties(data, itemVo);
            itemVo.setNum(num);

            //3.保存购物车数据
            map.put(skuId.toString(), JSON.toJSONString(itemVo));

        }

        cartVo.setUserKey(cartKey);
        return cartVo;
    }

    @Override
    public CartVo checkCart(Long[] skuId, Integer status, String userKey, String authorization) {
        CartKey key = getKey(userKey, authorization);
        String cartKey = key.getKey();
        RMap<String, String> cart = redisson.getMap(cartKey);

        if (skuId != null && skuId.length >0){
            for (Long sku : skuId) {
                String json = cart.get(sku.toString());
                CartItemVo itemVo = JSON.parseObject(json, CartItemVo.class);
                itemVo.setCheck(status == 0 ? false : true);
                cart.put(sku.toString(), JSON.toJSONString(itemVo));
            }
        }

        //获取到这个购物车的sui所有记录
     List<CartItemVo> cartItems = getCartItems(cartKey);
        CartVo cartVo = new CartVo();
        cartVo.setItems(cartItems);

        return cartVo;
    }

    @Override
    public CartVo updateCart(Long skuId, Integer num, String userKey, String authorization) {
        CartKey key = getKey(userKey, authorization);
        String key1 = key.getKey();
        RMap<String, String> cart = redisson.getMap(Constant.CART_PREFIX+key1);

        String itemJson = cart.get(skuId.toString());
        CartItemVo itemVo = JSON.parseObject(itemJson, CartItemVo.class);
        itemVo.setNum(num);
        //修改购物车，覆盖redis数据
        cart.put(skuId.toString(), JSON.toJSONString(itemJson));

        //获取购车最新的所有购物项
        List<CartItemVo> cartItems = getCartItems(key1);
        CartVo cartVo = new CartVo();
        cartVo.setItems(cartItems);

        return cartVo;
    }


    private CartKey getKey(String userKey, String authorization) {

        CartKey cartKey = new CartKey();
        String key = "";
        if (!StringUtils.isEmpty(authorization)) {
            //登录了
            Map <String ,Object> body = GuliJwtUtils.getJwtBody(authorization);
            Object id = body.get("id");
            key = id.toString() + "";
            cartKey.setKey(key);
            cartKey.setLogin(true);
            if (!StringUtils.isEmpty(userKey)) {
                cartKey.setMerge(true);
            }

        }else {
            //未登录
            //第一次啥都没有
            if(!StringUtils.isEmpty(userKey)){
                key = userKey;
                cartKey.setLogin(false);
                cartKey.setMerge(false);
            }else {
                key = UUID.randomUUID().toString().replace("-", "");
                cartKey.setLogin(false);
                cartKey.setMerge(false);
                cartKey.setTemp(true);//这是一个临时的
            }
        }

        cartKey.setKey(key);

        return cartKey;
    }


    /**
     * 将临时购物车和在线购物车合并
     * @param userKey
     * @param userId
     */
    private void mergerCart(String userKey, Long userId) throws ExecutionException, InterruptedException {
        //1.获取购物车
        RMap<String, String> tempCart = redisson.getMap(Constant.CART_PREFIX + userKey);


        Collection<String> values = tempCart.values();
        //判断如果两个都有需要合并购物车
        if (values != null && values.size()>0){
            for (String value : values) {
                CartItemVo cartItemVo = JSON.parseObject(value, CartItemVo.class);
                //将临时购物车里面的这个购物项添加到在线购物车里面
                addCartItemVo(cartItemVo.getSkuId(),cartItemVo.getNum(),userId.toString());
            }
        }
        redisTemplate.delete(Constant.CART_PREFIX + userKey);

    }




    /**
     * 将skuId添加到指定购物车
     * @param skuId
     * @param num
     * @param cartKey 不用拼前缀，方法自动拼
     */
    private CartItemVo addCartItemVo(Long skuId, Integer num, String cartKey) throws ExecutionException, InterruptedException {
        CartItemVo vo = null;
        RMap<String, String> cart = redisson.getMap(Constant.CART_PREFIX + cartKey);

        //添加购物车之前先确定购物车中有没有这个商品，如果有就数量+1，没有就新增
        String item = cart.get(skuId.toString());
        if (!StringUtils.isEmpty(item)){
            //购物车之前有
            CartItemVo itemVo = JSON.parseObject(item, CartItemVo.class);
            itemVo.setNum(num);
            cart.put(skuId.toString(), JSON.toJSONString(itemVo));
            vo = itemVo;
        }else{
            CartItemVo itemVo = new CartItemVo();
            //1)、封装基本信息
            CompletableFuture<Void> infoAsync = CompletableFuture.runAsync(() -> {
                //查询sku当前商品的详情
                Resp<SkuInfoVo> sKuInfoForCart = skuFeignService.getSKuInfoForCart(skuId);
                SkuInfoVo data = sKuInfoForCart.getData();
                //2.购物项
                BeanUtils.copyProperties(data, itemVo);
                itemVo.setNum(num);
            }, mainExecutor);


            //2)、封装优惠卷信息
            CompletableFuture<Void> couponAsync = CompletableFuture.runAsync(() -> {
                //获取当前购物项的优惠卷相关信息 //
                Resp<List<SkuCouponTo>> coupons = skuCouponRedutionFeignService.getCoupons(skuId);

                //To封装别人传来的数据
                List<SkuCouponTo> data = coupons.getData();
                List<SkuCouponVo> vos = new ArrayList<>();
                if (data != null && data.size() > 0) {
                    for (SkuCouponTo datum : data) {
                        SkuCouponVo skuCouponVo = new SkuCouponVo();
                        BeanUtils.copyProperties(datum, skuCouponVo);
                        vos.add(skuCouponVo);
                    }
                    itemVo.setCoupons(vos);
                }
            }, mainExecutor);
            //3)、封装商品满减信息
            CompletableFuture<Void> reductionAsync = CompletableFuture.runAsync(() -> {
                Resp<List<SkuFullReductionVo>> reductions = skuCouponRedutionFeignService.getReductions(skuId);
                List<SkuFullReductionVo> data = reductions.getData();
                if (data != null && data.size() > 0) {
                    itemVo.setReductions(data);
                }
            }, mainExecutor);
            //4）、保存购物车数据
            CompletableFuture.allOf(infoAsync,couponAsync,reductionAsync).get();
            cart.put(skuId.toString(), JSON.toJSONString(itemVo));
            vo = itemVo;
        }

        return vo;
    }
    /**
     * 获取购物项
     * @param key
     * @return
     */
    private List<CartItemVo> getCartItems(String key) {

        List<CartItemVo> vos = new ArrayList<>();
        //没登录获取临时购物车
        RMap<String, String> cart = redisson.getMap(Constant.CART_PREFIX + key);
        Collection<String> values = cart.values();
        if (values != null && values.size() > 0){
            for (String value : values) {
                CartItemVo itemVo = JSON.parseObject(value,CartItemVo.class);
                vos.add(itemVo);
            }
        }

        return vos;
    }
}

@Data
class CartKey {
    private String key;
    private boolean login;
    private boolean temp;
    private boolean merge;
}