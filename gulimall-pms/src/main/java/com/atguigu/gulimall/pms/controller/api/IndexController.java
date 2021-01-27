package com.atguigu.gulimall.pms.controller.api;


import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.pms.service.CategoryService;
import com.atguigu.gulimall.pms.vo.CategoryWithChildrensVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @ApiOperation("获取所有的以及分类")
    @GetMapping("/cates")
    public Resp<Object> levellCate(){


        List<CategoryWithChildrensVo> childrens = categoryService.getCategoryChildrensAndSubsById(0);
        return Resp.ok(childrens);
    }

    @ApiOperation("获取所有的一级分类")
    @GetMapping("/cates/{id}")
    public Resp<Object> catelogChildrens(@PathVariable("id")Integer id){

        //要获取分类一级这个分类的子分类； subs
        List<CategoryWithChildrensVo> childrens = categoryService.getCategoryChildrensAndSubsById(id);
        return Resp.ok(childrens);
    }

}
