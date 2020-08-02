package com.homvee.insurancecrm.vos;

import com.homvee.insurancecrm.dao.entities.BaseEntity;


@lombok.Data
public class RoleVO extends BaseEntity {

    private String roleName;

    public RoleVO() {
    }
    public RoleVO(String roleName) {
        this.roleName = roleName;
    }


}
