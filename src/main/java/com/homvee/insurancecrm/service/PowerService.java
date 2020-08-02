package com.homvee.insurancecrm.service;

import com.homvee.insurancecrm.dao.entities.Power;

public interface PowerService extends BaseService<Power,Long>{
    Power save(Power power);

    Integer del(Long id);
}
