package com.atguigu.ggkt.order.api;

import com.atguigu.ggkt.order.service.OrderInfoService;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.order.OrderFormVo;
import com.atguigu.ggkt.vo.order.OrderInfoVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户下单,生成订单
 * 需要用户的id(远程调用获取用户详细信息,在前端公众号授权时装在token中返给前端了),课程id,优惠券信息(远程调用优惠券模块)
 * 处理之
 * 返回生成的订单信息
 */

@RestController
@RequestMapping("api/order/orderInfo")
public class OrderInfoApiController {

    @Autowired
    private OrderInfoService orderInfoService;

    //生成订单方法
    @PostMapping("submitOrder")
    public Result submitOrder(@RequestBody OrderFormVo orderFormVo) {
        Long orderId = orderInfoService.submitOrder(orderFormVo);
        return Result.ok(orderId);
    }

    @ApiOperation(value = "获取")
    @GetMapping("getInfo/{id}")
    public Result getInfo(@PathVariable Long id) {
        OrderInfoVo orderInfoVo = orderInfoService.getOrderInfoById(id);
        return Result.ok(orderInfoVo);
    }
}
