package com.homvee.insurancecrm.dao.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_role")
@lombok.Data
public class Role extends BaseEntity {

    private String roleName;

}
