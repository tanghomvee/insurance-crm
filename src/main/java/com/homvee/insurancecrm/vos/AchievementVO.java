package com.homvee.insurancecrm.vos;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 11:10
 */
@Data
public class AchievementVO extends BaseVO {

    private String uid;
    private String cookie;
    private List<List<Object>> data;
    private Date fromDate;
    private Date toDate;

    public AchievementVO(String uid, String cookie, List<List<Object>> excelData,Date fromDate,Date toDate) {
        this.uid = uid;
        this.cookie = cookie;
        this.data = excelData;
        this.fromDate =fromDate;
        this.toDate =toDate;
    }
}
