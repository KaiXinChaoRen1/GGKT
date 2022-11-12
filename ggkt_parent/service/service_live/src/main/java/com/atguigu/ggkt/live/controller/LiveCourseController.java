package com.atguigu.ggkt.live.controller;


import com.atguigu.ggkt.live.mtcloud.MTCloud;
import com.atguigu.ggkt.live.service.LiveCourseAccountService;
import com.atguigu.ggkt.live.service.LiveCourseService;
import com.atguigu.ggkt.model.live.LiveCourse;
import com.atguigu.ggkt.model.live.LiveCourseAccount;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.live.LiveCourseConfigVo;
import com.atguigu.ggkt.vo.live.LiveCourseFormVo;
import com.atguigu.ggkt.vo.live.LiveCourseQueryVo;
import com.atguigu.ggkt.vo.live.LiveCourseVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 直播课程表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-05-09
 */
@RestController
@RequestMapping(value = "/admin/live/liveCourse")
public class LiveCourseController {

    @Autowired
    private LiveCourseService liveCourseService;

    @Autowired
    private LiveCourseAccountService liveCourseAccountService;

    @ApiOperation(value = "获取最近的x个直播")
    @GetMapping("findLatelyList")
    public Result findLatelyList() {
        List<LiveCourseVo> list = liveCourseService.getLatelyList();
        return Result.ok(list);
    }

    /**
     * 后台管理系统
     */
    @ApiOperation(value = "修改直播配置")
    @PutMapping("updateConfig")
    public Result updateConfig(@RequestBody LiveCourseConfigVo liveCourseConfigVo) {
        liveCourseService.updateConfig(liveCourseConfigVo);
        return Result.ok(null);
    }

    /**
     * 后台管理系统
     */
    @ApiOperation(value = "获取直播账号信息")
    @GetMapping("getLiveCourseAccount/{id}")
    public Result getLiveCourseAccount(@PathVariable Long id) {
        LiveCourseAccount liveCourseAccount = liveCourseAccountService.getLiveCourseAccountCourseId(id);
        return Result.ok(liveCourseAccount);
    }

    /**
     * 后台管理系统
     */
    @ApiOperation(value = "获取直播配置信息(是否开启商城,开启观看人数等配置)")
    @GetMapping("getCourseConfig/{id}")
    public Result getCourseConfig(@PathVariable Long id) {
        LiveCourseConfigVo liveCourseConfigVo = liveCourseService.getCourseConfig(id);
        return Result.ok(liveCourseConfigVo);
    }

    //id查询直播课程基本信息
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        LiveCourse liveCourse = liveCourseService.getById(id);
        return Result.ok(liveCourse);
    }

    //id查询直播课程基本信息和描述信息
    @GetMapping("getInfo/{id}")
    public Result getInfo(@PathVariable Long id) {
        LiveCourseFormVo liveCourseFormVo = liveCourseService.getLiveCourseVo(id);
        return Result.ok(liveCourseFormVo);
    }

    /**
     * 修改直播信息(可以点看看参数Vo里的信息)
     * 1.查询信息回显到前端
     * 2.修改内容
     * 3.提交
     */
    @PutMapping("update")
    public Result update(@RequestBody LiveCourseFormVo liveCourseFormVo) {
        liveCourseService.updateLiveById(liveCourseFormVo);
        return Result.ok(null);
    }

    /**
     * 删除表中数据
     * 删除欢拓云中的数据
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        liveCourseService.removeLive(id);
        return Result.ok(null);
    }

    /**
     * 像在欢拓云手动添加直播一样,通过后台管理系统的后端代码也可以添加直播,使用其提供的工具类
     */
    @ApiOperation("直播课程添加")
    @PostMapping("save")
    public Result save(@RequestBody LiveCourseFormVo liveCourseFormVo) {
        liveCourseService.saveLive(liveCourseFormVo);
        return Result.ok(null);
    }

    @ApiOperation("直播课程列表")
    @GetMapping("{page}/{limit}")
    public Result list(@PathVariable Long page, @PathVariable Long limit) {
        Page<LiveCourse> pageParam = new Page<>(page, limit);
        IPage<LiveCourse> pageModel = liveCourseService.selectPage(pageParam);
        return Result.ok(pageModel);
    }
}

