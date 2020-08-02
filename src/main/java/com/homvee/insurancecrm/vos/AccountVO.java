package com.homvee.insurancecrm.vos;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountVO  extends BaseVO {
    private Long userId;
    private String userName;
    private String acctName;
    private Long credit;
}
