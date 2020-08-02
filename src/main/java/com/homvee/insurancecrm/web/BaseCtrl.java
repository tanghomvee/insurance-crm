package com.homvee.insurancecrm.web;

import com.homvee.insurancecrm.constants.SessionKey;
import com.homvee.insurancecrm.vos.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 09:34
 */
public class BaseCtrl {
    protected Logger LOGGER = null;

    @Resource
    protected HttpSession session;



    public BaseCtrl() {
        LOGGER = LoggerFactory.getLogger(this.getClass());
    }


    protected UserVO getUser() {

        return (UserVO) session.getAttribute(SessionKey.USER);

    }

    protected void setUser(UserVO user) {
      session.setAttribute(SessionKey.USER , user);
    }

}
