package com.homvee.insurancecrm.dao.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_account_record")
@Data
public class AccountRecord extends BaseEntity {

    private Long accountId;

    private Byte  tradeType;

    private Date tradeDate;

    /**
     * 交易积分
     */
    private Long tradeCredit;

    /**
     * 操作者
     */
    private Long operatorId;

    private String note;
}
