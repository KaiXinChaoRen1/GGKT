package com.atguigu.ggkt.order.controller;


import com.atguigu.ggkt.model.order.OrderInfo;
import com.atguigu.ggkt.order.service.OrderInfoService;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.order.OrderInfoQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *  后台管理系统的订单Controller
 */
@RestController
@RequestMapping(value="/admin/order/orderInfo")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    //订单列表分页查询
    @GetMapping("{page}/{limit}")
    public Result listOrder(@PathVariable Long page,
                            @PathVariable Long limit,
                            OrderInfoQueryVo orderInfoQueryVo) {
        //创建page对象
        Page<OrderInfo> pageParam = new Page<>(page,limit);
        Map<String,Object> map =
                orderInfoService.selectOrderInfoPage(pageParam,orderInfoQueryVo);
        return Result.ok(map);
    }
}

