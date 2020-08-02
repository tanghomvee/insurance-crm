package com.homvee.insurancecrm.dao.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_account")
@Data
public class Account extends BaseEntity {

    private Long userId;
    private String acctName;
    private Long credit;

}
