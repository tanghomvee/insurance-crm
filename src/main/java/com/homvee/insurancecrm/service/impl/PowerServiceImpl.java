package com.homvee.insurancecrm.service.impl;

import com.homvee.insurancecrm.dao.PowerDao;
import com.homvee.insurancecrm.dao.entities.Power;
import com.homvee.insurancecrm.service.PowerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
public class PowerServiceImpl extends BaseServiceImpl<Power,Long> implements PowerService {
    @Resource
    private PowerDao powerDao;

    @Override
    public Power save(Power power) {
        return powerDao.save(power);

    }

    @Transactional
    @Override
    public Integer del(Long id) {
        return powerDao.del(id);
    }
}
