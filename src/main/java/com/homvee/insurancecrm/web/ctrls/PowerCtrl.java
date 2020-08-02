package com.homvee.insurancecrm.web.ctrls;

import com.homvee.insurancecrm.dao.entities.Power;
import com.homvee.insurancecrm.enums.YNEnum;
import com.homvee.insurancecrm.service.PowerService;
import com.homvee.insurancecrm.vos.Msg;
import com.homvee.insurancecrm.vos.PowerVO;
import com.homvee.insurancecrm.web.BaseCtrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(path = {"/power"})
@Slf4j
public class PowerCtrl extends BaseCtrl {

    @Resource
    private PowerService powerService;

    @PostMapping("/add")
    public Msg add(PowerVO vo){
        if (StringUtils.isEmpty(vo.getPowerName())){
            return Msg.error();
        }
        if (StringUtils.isEmpty(vo.getUrl())){
            return Msg.error();
        }
        Power power = new Power();
        BeanUtils.copyProperties(vo , power);
        power.setYn(YNEnum.YES.getVal());
        power.setCreator(getUser().getId().toString());

        powerService.save(power);
        return Msg.success();
    }
    @PostMapping("/edit")
    public Msg edit(PowerVO vo){
        if (StringUtils.isEmpty(vo.getPowerName())){
            return Msg.error();
        }
        if (StringUtils.isEmpty(vo.getUrl())){
            return Msg.error();
        }
        if (StringUtils.isEmpty(vo.getId())){
            return Msg.error();
        }
        Power power = new Power();
        BeanUtils.copyProperties(vo , power);
        power.setYn(YNEnum.YES.getVal());
        power.setCreator(getUser().getId().toString());

        powerService.save(power);
        return Msg.success();
    }
    @PostMapping("/del")
    public Msg del(Long id){
        Integer rs = powerService.del(id);
        return Msg.success();
    }
    @PostMapping("/list")
    public Msg list(Long id){
        return Msg.success();
    }

}
