package com.homvee.insurancecrm.web.ctrls;

import com.homvee.insurancecrm.dao.entities.Role;
import com.homvee.insurancecrm.service.RoleService;
import com.homvee.insurancecrm.vos.Msg;
import com.homvee.insurancecrm.vos.PageVO;
import com.homvee.insurancecrm.vos.RoleVO;
import com.homvee.insurancecrm.web.BaseCtrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(path = {"/role"})
@Slf4j
public class RoleCtrl extends BaseCtrl {
    @Resource
    private RoleService roleService;

    @PostMapping("/add")
    public Msg add(RoleVO roleVO){
        if (StringUtils.isEmpty(roleVO.getRoleName())){
            return Msg.error();
        }

        Role role = new Role();
        BeanUtils.copyProperties(roleVO , role);
        role.setCreator(getUser().getId().toString());
        role = roleService.save(role);
        return Msg.success();
    }
    @PostMapping("/edit")
    public Msg edit(RoleVO roleVO){
        if (StringUtils.isEmpty(roleVO.getRoleName())){
            return Msg.error();
        }
        if (StringUtils.isEmpty(roleVO.getId())){
            return Msg.error();
        }

        Role role = new Role();
        BeanUtils.copyProperties(roleVO , role);
        role.setCreator(getUser().getId().toString());
        role = roleService.save(role);
        return Msg.success();
    }
    @PostMapping("/del")
    public Msg del(Long id){
        if (StringUtils.isEmpty(id)){
            return Msg.error();
        }

       int rs = roleService.del(id);
        return Msg.success();
    }
    @PostMapping("/list")
    public Msg list(RoleVO roleVO , Integer pageNum , Integer pageSize){

        PageVO<RoleVO>  vo = roleService.list(roleVO,pageNum,pageSize);
        return Msg.success(vo);
    }
}
