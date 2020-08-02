package com.homvee.insurancecrm.vos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AccountRecordVO implements Serializable {
    private Long accountId;
    private String acctName;
    private String userName;

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

    private String operatorName;

    private String note;
}
