package com.homvee.insurancecrm.dao.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_user")
@Data
public class User extends BaseEntity {
    private String userName;
    private String userPwd;
    private String phoneNum;

}
