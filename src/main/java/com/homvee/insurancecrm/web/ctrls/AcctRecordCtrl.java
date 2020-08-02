package com.homvee.insurancecrm.web.ctrls;

import com.homvee.insurancecrm.service.AccountRecordService;
import com.homvee.insurancecrm.service.AccountService;
import com.homvee.insurancecrm.vos.AccountRecordVO;
import com.homvee.insurancecrm.vos.Msg;
import com.homvee.insurancecrm.vos.PageVO;
import com.homvee.insurancecrm.web.BaseCtrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(path = {"/record"})
@Slf4j
public class AcctRecordCtrl extends BaseCtrl {

    @Resource
    private AccountRecordService accountRecordService;
    @Resource
    private AccountService accountService;

    @PostMapping(path = {"/list"})
    public Msg list(Long operatorId , Long pageNum,Long pageSize){

        AccountRecordVO vo = new AccountRecordVO();
        vo.setOperatorId(operatorId);
        PageVO<AccountRecordVO> pageVO = accountRecordService.list(vo, pageNum ,pageSize);

        return Msg.success(pageVO);
    }

}
