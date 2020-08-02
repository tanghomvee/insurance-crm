package com.homvee.insurancecrm.vos;

import com.homvee.insurancecrm.dao.entities.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
@Data
public class PowerVO extends BaseEntity {

    private String powerName;
    private String url;
    private Long parentId;
    private String parentName;
    private String parentUrl;
}
