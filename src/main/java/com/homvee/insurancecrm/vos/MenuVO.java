package com.homvee.insurancecrm.vos;

import com.homvee.insurancecrm.dao.entities.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_menu")
@Data
public class MenuVO extends BaseEntity {

    private String menuName;
    private String menuType;
    private String url;
    private Long parentId;
    private String parentName;


    public MenuVO() {
    }
    public MenuVO(Long id , String menuName , String menuType ,String url,Long parentId , String parentName ) {
        this.id = id;
        this.menuName = menuName;
        this.menuType = menuType;
        this.url = url;
        this.parentId = parentId;
        this.parentName =parentName;
    }


}
