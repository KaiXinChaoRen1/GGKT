package com.atguigu.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/**
 *  读取Excle需要写一个Listener 实现接口,重写方法
 */

public class ExcelListener extends AnalysisEventListener<User> {

    //一行一行读取excel内容(从第二行开始)，把每行内容封装到user对象
    @Override
    public void invoke(User user, AnalysisContext analysisContext) {
        System.out.println(user);
    }

    //读取表头的内容
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头："+headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
