package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.vod.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-09-24
 */
@Api(tags="讲师管理接口")
@RestController
@RequestMapping("/admin/vod/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;


    //逻辑删除
    @ApiOperation("根据id逻辑删除讲师")
    @DeleteMapping("remove/{id}")
    public boolean removeTeacher(@PathVariable Long id){
        return teacherService.removeById(id);
    }

    @ApiOperation("查询所有讲师")//swagger注解
    @GetMapping("/findAll")
    public List<Teacher> findAllTeacher(){
        List<Teacher> list = teacherService.list();
        return list;
    }


}

