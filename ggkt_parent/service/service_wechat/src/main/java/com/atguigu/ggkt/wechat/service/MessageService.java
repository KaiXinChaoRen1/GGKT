package com.atguigu.ggkt.wechat.service;

import java.util.Map;

public interface MessageService {
    //处理 用户发消息->微信服务器->这里
    String receiveMessage(Map<String, String> param);

    //回复模板消息(每个人不一样),以订单为例
    void pushPayMessage(long id);
}
