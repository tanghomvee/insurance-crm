package com.homvee.insurancecrm.web.ctrls;

import com.homvee.insurancecrm.enums.YNEnum;
import com.homvee.insurancecrm.service.UserService;
import com.homvee.insurancecrm.vos.Msg;
import com.homvee.insurancecrm.vos.PageVO;
import com.homvee.insurancecrm.vos.UserVO;
import com.homvee.insurancecrm.web.BaseCtrl;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(path = {"/user"})
@Slf4j
public class UserCtrl extends BaseCtrl {

    @Resource
    private UserService userService;

    @PostMapping(path = {"/login"})
    public Msg login(@RequestBody UserVO vo){
        if (StringUtils.isEmpty(vo.getUserName())){
            return Msg.error("请输入账户名");
        }
        if (StringUtils.isEmpty(vo.getUserPwd())){
            return Msg.error("请输入账户密码");
        }

        UserVO userVO = userService.findByUserNameOrPhoneNum(vo.getUserName());

        if (userVO == null){
            return Msg.error("用户不存在");
        }

        if (!vo.getUserPwd().equals(userVO.getUserPwd())){
            return Msg.error("用户密码错误");
        }

        setUser(userVO);

        return Msg.success(userVO);
    }
    @PostMapping(path = {"/add"})
    public Msg add(@RequestBody  UserVO userVO){
        if (StringUtils.isEmpty(userVO.getUserName())){
            return Msg.error("请输入账户名");
        }
        if (StringUtils.isEmpty(userVO.getPhoneNum())){
            return Msg.error("请输入手机号码");
        }
        userVO.setUserPwd(userVO.getPhoneNum());
        userVO.setCreator(getUser().getId().toString());
        userVO.setCreateTime(DateTime.now().toDate());
        userVO.setCreator(getUser().getId().toString());
        userVO.setYn(YNEnum.YES.getVal());
        userService.save(userVO);


        return Msg.success();
    }
    @PostMapping(path = {"/setting"})
    public Msg setting(String newPwd,String oldPwd){
        if (StringUtils.isEmpty(newPwd)){
            return Msg.error("请输入新密码");
        }
        if (StringUtils.isEmpty(oldPwd)){
            return Msg.error("请输入原始密码");
        }

        int rs = userService.modifyPwdById(newPwd , getUser().getId());

        return Msg.success();
    }
    @PostMapping(path = {"/edit"})
    public Msg edit(@RequestBody UserVO vo){

        Long id = vo.getId();

        Msg msg = one(id);

        if(!msg.isSuccess()){
            return  msg;
        }

        UserVO userVO = (UserVO) msg.getData();

        if (StringUtils.isEmpty(vo.getUserName())){
            return Msg.error("请输入账户名");
        }
        if (StringUtils.isEmpty(vo.getPhoneNum())){
            return Msg.error("请输入手机号码");
        }

        userVO.setUserName(vo.getUserName());
        userVO.setPhoneNum(vo.getPhoneNum());
        userVO.setChangeTime(DateTime.now().toDate());
        userVO.setChanger(getUser().getId().toString());
        userService.save(userVO);
        return Msg.success();
    }
    @GetMapping(path = {"/one"})
    public Msg one(Long id){

        if (id == null || id < 1){
            return Msg.error("参数错误");
        }

        UserVO userVO = userService.findById(id);
        if (userVO == null){
            return Msg.error("数据不存在");
        }
        return Msg.success(userVO);
    }
    @PostMapping(path = {"/del"})
    public Msg del(Long[] ids){

        if (ids == null || ids.length < 1){
            return Msg.error("参数错误");
        }
        userService.del(ids);
        return Msg.success();
    }

    @PostMapping(path = {"/list"})
    public Msg list(String userName,String phoneNum , Integer pageNum , Integer pageSize){

        UserVO vo = new UserVO();
        vo.setPhoneNum(phoneNum);
        vo.setUserName(userName);
        vo.setCreator(getUser().getId().toString());
        PageVO<UserVO> page = userService.list(vo , pageNum ,pageSize);
        return Msg.success(page);
    }

}
