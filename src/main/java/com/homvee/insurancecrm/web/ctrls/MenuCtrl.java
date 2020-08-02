package com.homvee.insurancecrm.web.ctrls;

import com.homvee.insurancecrm.dao.entities.Menu;
import com.homvee.insurancecrm.enums.MenuTypeEnum;
import com.homvee.insurancecrm.service.MenuService;
import com.homvee.insurancecrm.vos.AccountRecordVO;
import com.homvee.insurancecrm.vos.MenuVO;
import com.homvee.insurancecrm.vos.Msg;
import com.homvee.insurancecrm.vos.PageVO;
import com.homvee.insurancecrm.web.BaseCtrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(path = {"/menu"})
@Slf4j
public class MenuCtrl extends BaseCtrl {

    @Resource
    private MenuService menuService;

    @PostMapping(path = {"/list"})
    public Msg list(MenuVO vo , Long pageNum, Long pageSize){
        PageVO<MenuVO> pageVO = menuService.list(vo, pageNum ,pageSize);

        return Msg.success(pageVO);
    }
    @PostMapping(path = {"/add"})
    public Msg add(MenuVO vo){

        if (StringUtils.isEmpty(vo.getMenuName())){
            return Msg.error();
        }
        if (StringUtils.isEmpty(vo.getMenuType())){
            return Msg.error();
        }
        if (MenuTypeEnum.ITEM.toString().equals(vo.getMenuType())){
            if (StringUtils.isEmpty(vo.getUrl())){
                Msg.error();
            }
        }

        Menu menu = new Menu();
        BeanUtils.copyProperties(vo , menu);
        menuService.save(menu);

        return Msg.success(vo);
    }
    @PostMapping(path = {"/del"})
    public Msg add(Long id){

        menuService.del(id);
        return Msg.success();
    }

}
