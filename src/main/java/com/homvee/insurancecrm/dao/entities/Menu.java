package com.homvee.insurancecrm.dao.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_menu")
@Data
public class Menu extends BaseEntity {

    private String menuName;
    private String menuType;
    private String url;
    private Long parentId;
}
