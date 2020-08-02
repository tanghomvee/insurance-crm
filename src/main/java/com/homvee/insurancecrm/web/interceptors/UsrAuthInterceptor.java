package com.homvee.insurancecrm.web.interceptors;

import com.alibaba.fastjson.JSON;
import com.homvee.insurancecrm.constants.SessionKey;
import com.homvee.insurancecrm.vos.Msg;
import com.homvee.insurancecrm.vos.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class UsrAuthInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsrAuthInterceptor.class);

    private static final String MEDIA_JSON = "application/json;charset=UTF-8";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        LOGGER.info("来自请求方的IP:remoteAddr={},remoteHost={},remotePort={},forwarded-for={}" ,
                request.getRemoteAddr() ,
                request.getRemoteHost(),
                request.getRemotePort(),
                request.getHeader("X-FORWARDED-FOR"));

        String uri = request.getServletPath();
        UserVO operator = (UserVO) request.getSession().getAttribute(SessionKey.USER);


        if (operator.getId() != 1){
            if (!"/userData/download".equals(uri)){
                if (!"/userData/list".equals(uri)){
                if (!"/userData/in".equals(uri)){

                    response.setContentType(MEDIA_JSON);
                    PrintWriter printWriter = response.getWriter();
                    printWriter.write(JSON.toJSONString(Msg.error("无权限")));
                    printWriter.flush();
                    printWriter.close();
                    return false;
                }

                }
            }
        }

        return super.preHandle(request, response, handler);

    }

}
