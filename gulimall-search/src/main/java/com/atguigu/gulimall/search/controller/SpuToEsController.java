package com.atguigu.gulimall.search.controller;


import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.es.EsSkuVo;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.atguigu.gulimall.commons.bean.Constant;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/es")
public class SpuToEsController {


    @Autowired
    JestClient jestClient;

    @PostMapping("/spu/up")
    public Resp<Object> spuUp(@RequestBody List<EsSkuVo> vo){

        vo.forEach(vol->{
            Index index = new Index.Builder(vol)
                    .index(Constant.ES_SPU_INDEX)
                    .type(Constant.ES_SPU_TYPE)
                    .id(vol.getId().toString())
                    .build();
            try {
                jestClient.execute(index);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        return Resp.ok(null);
    }



}
