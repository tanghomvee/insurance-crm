package com.homvee.insurancecrm.service;

import com.homvee.insurancecrm.dao.entities.Role;
import com.homvee.insurancecrm.vos.PageVO;
import com.homvee.insurancecrm.vos.RoleVO;

public interface RoleService extends BaseService<Role,Long>{
    Role save(Role role);

    Integer del(Long id);

    PageVO<RoleVO> list(RoleVO roleVO, Integer pageNum, Integer pageSize);
}
