package com.homvee.insurancecrm.web.ctrls;

import com.homvee.insurancecrm.service.AccountService;
import com.homvee.insurancecrm.service.UserService;
import com.homvee.insurancecrm.vos.AccountVO;
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
@RequestMapping(path = {"/acct"})
@Slf4j
public class AcctCtrl extends BaseCtrl {

    @Resource
    private AccountService accountService;

    @PostMapping(path = {"/add"})
    public Msg add(@RequestBody AccountVO vo){
        if (vo.getUserId() == null || vo.getUserId() < 1){
            return Msg.error("参数错误");
        }
        if (StringUtils.isEmpty(vo.getAcctName())){
            return Msg.error("参数错误");
        }
        vo.setCreator(getUser().getId().toString());
        Long pk = accountService.save(vo);

        return Msg.success();
    }
    @PostMapping(path = {"/edit"})
    public Msg edit(@RequestBody AccountVO vo){
        if (vo.getUserId() == null || vo.getUserId() < 1){
            return Msg.error("参数错误");
        }
        if (vo.getId() == null || vo.getId() < 1){
            return Msg.error("参数错误");
        }
        if (StringUtils.isEmpty(vo.getAcctName())){
            return Msg.error("参数错误");
        }
        Msg tmp = one(vo.getId());

        if (!tmp.isSuccess()){
            return tmp;
        }

        AccountVO tmpVo = (AccountVO) tmp.getData();

        tmpVo.setAcctName(vo.getAcctName());
        tmpVo.setChanger(getUser().getId().toString());
        tmpVo.setChangeTime(DateTime.now().toDate());
        Long pk = accountService.edit(tmpVo);

        return Msg.success();
    }
    @GetMapping(path = {"/one"})
    public Msg one(Long id){
        if (id == null || id < 1){
            return Msg.error("参数错误");
        }

        AccountVO vo = accountService.one(id);
        if (vo == null){
            return Msg.error("数据不存在");
        }
        return Msg.success(vo);
    }
    @GetMapping(path = {"/charge"})
    public Msg charge(Long id,Long credit){
        if (id == null || id < 1){
            return Msg.error("参数错误");
        }
        if (credit == null || credit < 1){
            return Msg.error("参数错误");
        }
        Msg tmp = one(id);

        if (!tmp.isSuccess()){
            return tmp;
        }
        Integer rs = accountService.charge(id ,credit ,getUser().getId());

        return Msg.success();
    }
    @PostMapping(path = {"/list"})
    public Msg list(Long userId , Long pageNum,Long pageSize){
//        if (userId == null || userId < 1){
//            return Msg.error("参数错误");
//        }
        AccountVO vo = new AccountVO();
        if (!"admin".equals(getUser().getUserName())){
            vo.setCreator(getUser().getId().toString());
        }
        vo.setUserId(userId);
        PageVO<AccountVO> pageVO = accountService.list(vo , pageNum ,pageSize);

        return Msg.success(pageVO);
    }

    @PostMapping(path = {"/del"})
    public Msg del(Long[] ids){

        if (ids == null || ids.length < 1){
            return Msg.error("参数错误");
        }
        accountService.del(ids);
        return Msg.success();
    }

}
