package com.atguigu.ggkt.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.wechat.Menu;
import com.atguigu.ggkt.vo.wechat.MenuVo;
import com.atguigu.ggkt.wechat.mapper.MenuMapper;
import com.atguigu.ggkt.wechat.service.MenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单明细 订单明细 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-04-30
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    //config里有配,不过随便看看,反正是复制粘贴
    @Autowired
    private WxMpService wxMpService;

    //删除公众号菜单
    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new GgktException(20001, "公众号菜单删除失败");
        }
    }

    //同步公众号菜单
    @Override
    public void syncMenu() {
        //将菜单数据封装成微信指定的JSON格式
        //获取所有菜单数据
        List<MenuVo> menuVoList = this.findMenuInfo();
        //封装button里面结构，数组格式
        JSONArray buttonList = new JSONArray();
        for (MenuVo oneMenuVo : menuVoList) {
            //json对象  一级菜单
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            //json数组   二级菜单
            JSONArray subButton = new JSONArray();
            for (MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                JSONObject view = new JSONObject();
                view.put("type", twoMenuVo.getType());
                if (twoMenuVo.getType().equals("view")) {
                    view.put("name", twoMenuVo.getName());
                    view.put("url", "http://ggkt2.vipgz1.91tunnel.com/#"
                            + twoMenuVo.getUrl());
                } else {
                    view.put("name", twoMenuVo.getName());
                    view.put("key", twoMenuVo.getMeunKey());
                }
                subButton.add(view);
            }
            one.put("sub_button", subButton);
            buttonList.add(one);
        }
        //封装最外层button部分
        JSONObject button = new JSONObject();
        button.put("button", buttonList);

        try {
            //String menuId =           //(据说这个id用不到,用不到你隔这占地方?)
            this.wxMpService.getMenuService().menuCreate(button.toJSONString());  //将数据整理成指定格式就可以通过这个方法同步了
            //System.out.println("menuId" + menuId);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new GgktException(20001, "公众号菜单同步失败");
        }
    }


    //获取所有菜单，按照一级和二级菜单封装
    @Override
    public List<MenuVo> findMenuInfo() {
        //1 创建list集合，最终数据返回
        List<MenuVo> finalMenuList = new ArrayList<>();
        //2 查询所有菜单数据（包含一级和二级）
        List<Menu> menuList = baseMapper.selectList(null);
        //3 从所有菜单数据获取所有一级菜单数据（parent_id=0）
        List<Menu> oneMenuList = menuList.stream()
                .filter(menu -> menu.getParentId().longValue() == 0)
                .collect(Collectors.toList());
        //4 封装一级菜单数据，封装到最终数据list集合
        //遍历一级菜单list集合
        for (Menu oneMenu : oneMenuList) {
            MenuVo oneMenuVo = new MenuVo();
            BeanUtils.copyProperties(oneMenu, oneMenuVo);
            //5 封装二级菜单数据,找到当前一级菜单下的二级菜单并放到当前一级菜单下
            List<Menu> twoMenuList = menuList.stream()
                    .filter(menu -> menu.getParentId().longValue() == oneMenu.getId())
                    .collect(Collectors.toList());
            //List<Menu>  --  List<MenuVo>
            List<MenuVo> childrenList = new ArrayList<>();
            twoMenuList.stream().forEach(twoMenu -> {
                MenuVo twoMenuVo = new MenuVo();
                BeanUtils.copyProperties(twoMenu, twoMenuVo);
                childrenList.add(twoMenuVo);
            });
            oneMenuVo.setChildren(childrenList);
            //把oneMenuVo放到最终list集合
            finalMenuList.add(oneMenuVo);
        }
        //最终数据返回
        return finalMenuList;
    }

    //获取所有一级菜单
    @Override
    public List<Menu> findMenuOneInfo() {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0);
        List<Menu> list = baseMapper.selectList(wrapper);
        return list;
    }

}
