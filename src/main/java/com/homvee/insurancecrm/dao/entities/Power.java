package com.homvee.insurancecrm.dao.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_power")
@lombok.Data
public class Power extends BaseEntity {

    private String powerName;
    private String url;
    private Long parentId;
}
