package com.homvee.insurancecrm.vos;

import lombok.Data;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 11:10
 */
@Data
public class UserVO extends BaseVO {

    private String userName;
    private String userPwd;
    private String phoneNum;
}
