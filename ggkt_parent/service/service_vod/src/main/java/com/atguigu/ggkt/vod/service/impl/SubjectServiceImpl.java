package com.atguigu.ggkt.vod.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.vo.vod.SubjectEeVo;
import com.atguigu.ggkt.vod.listener.SubjectListener;
import com.atguigu.ggkt.vod.mapper.SubjectMapper;
import com.atguigu.ggkt.vod.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-04-21
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    /**
     * 读取Excle要用的监听器对象
     */
    @Autowired
    private SubjectListener subjectListener;

    //课程分类列表
    //懒加载，每次查询一层数据
    @Override
    public List<Subject> selectSubjectList(Long id) {
        //SELECT * FROM SUBJECT WHERE parent_id=0
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Subject> subjectList = baseMapper.selectList(wrapper);
        //遍历，判断这一层的数据是否有子层级
        for (Subject subject : subjectList) {
            Long subjectId = subject.getId();
            boolean isChild = this.hasChildren(subjectId);
            subject.setHasChildren(isChild); //这个属性设为true
        }
        return subjectList;
    }

    //课程分类导出
    @Override
    public void exportData(HttpServletResponse response) {
        try {
            //1.设置下载信息
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("课程分类", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            //2.查询课程分类表中所有数据
            List<Subject> subjectList = baseMapper.selectList(null);
            //List<Subject> ===>  List<SubjectEeVo>
            List<SubjectEeVo> subjectEeVoList = new ArrayList<>();
            for (Subject subject : subjectList) {
                SubjectEeVo subjectEeVo = new SubjectEeVo();
                // BeanUtils.copyProperties(xx,xx)
                BeanUtils.copyProperties(subject, subjectEeVo);
                subjectEeVoList.add(subjectEeVo);
            }

            //3.EasyExcel写操作
            EasyExcel.write(response.getOutputStream(), SubjectEeVo.class)
                    .sheet("课程分类")
                    .doWrite(subjectEeVoList);

        } catch (Exception e) {
            throw new GgktException(20001, "导出失败");
        }
    }

    //课程分类导入
    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),
                    SubjectEeVo.class,
                    subjectListener).sheet().doRead();
        } catch (IOException e) {
            throw new GgktException(20001, "导入失败");
        }
    }

    //判断是否有下一层数据的工具方法
    private boolean hasChildren(Long subjectId) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", subjectId);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }
}
