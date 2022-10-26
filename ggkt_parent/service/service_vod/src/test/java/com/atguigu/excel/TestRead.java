package com.atguigu.excel;

import com.alibaba.excel.EasyExcel;


/**
 * 读取Excle的测试练习
 */
public class TestRead {

    public static void main(String[] args) {


        String fileName = "C:\\Users\\lwq\\Desktop\\lwq.xlsx";
        //调用方法进行读操作
        EasyExcel.read(fileName, User.class, new ExcelListener()).sheet().doRead();
    }
}
