package com.atguigu.ggkt.order.api;

import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.order.service.OrderInfoService;
import com.atguigu.ggkt.order.service.WXPayService;
import com.atguigu.ggkt.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *
 */

@Api(tags = "微信支付接口")
@RestController
@RequestMapping("/api/order/wxPay")
public class WXPayController {

    @Autowired
    private WXPayService wxPayService;

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 回调查询微信支付的订单是否成功,
     * orderNo是微信支付金额的订单
     */
    @ApiOperation(value = "查询支付状态")
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result queryPayStatus(
            @ApiParam(name = "orderNo", value = "订单No", required = true)
            @PathVariable("orderNo") String orderNo) {

        //根据订单号调用微信接口查询支付状态
        Map<String, String> resultMap = wxPayService.queryPayStatus(orderNo);   //这是我们手写的方法,不是工具包方法
        //判断支付是否成功
        if (resultMap == null) {
            throw new GgktException(20001, "支付出错");
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))) {//成功
            //更新订单状态为--已经支付
            String out_trade_no = resultMap.get("out_trade_no");
            orderInfoService.updateOrderStatus(out_trade_no);
            return Result.ok(null).message("支付成功");
        }
        return Result.ok(null).message("支付中");
    }


    @ApiOperation(value = "微信支付的下单支付(小程序支付),不是课程订单的下单")
    @GetMapping("/createJsapi/{orderNo}")
    public Result createJsapi(
            @ApiParam(name = "orderNo", value = "订单No", required = true)
            @PathVariable("orderNo") String orderNo) {
        Map<String, Object> map = wxPayService.createJsapi(orderNo);
        return Result.ok(map);
    }
}
