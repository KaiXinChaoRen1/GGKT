package com.atguigu.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * 写出Excle的测试练习
 */
public class TestWrite {

    public static void main(String[] args) {
        //设置文件名称和路径
        String fileName = "C:\\Users\\lwq\\Desktop\\lwq.xlsx";
        //调用方法
        EasyExcel.write(fileName,User.class)        //相当于创建了workbook
                  .sheet("写操作")        //创建sheet
                  .doWrite(data());                 //写数据
    }
    //创建要写的数据
    private static List<User> data() {
        List<User> list = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            User data = new User();
            data.setId(i);
            data.setName("lucy"+i);
            list.add(data);
        }
        return list;
    }
}
