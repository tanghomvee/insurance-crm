package com.homvee.insurancecrm.service.impl;

import com.google.common.base.Function;
import com.homvee.insurancecrm.dao.AccountRecordDao;
import com.homvee.insurancecrm.dao.entities.Account;
import com.homvee.insurancecrm.dao.entities.AccountRecord;
import com.homvee.insurancecrm.enums.YNEnum;
import com.homvee.insurancecrm.service.AccountRecordService;
import com.homvee.insurancecrm.service.AccountService;
import com.homvee.insurancecrm.service.UserService;
import com.homvee.insurancecrm.vos.AccountRecordVO;
import com.homvee.insurancecrm.vos.AccountVO;
import com.homvee.insurancecrm.vos.PageVO;
import com.homvee.insurancecrm.vos.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class AccountRecordServiceImpl extends BaseServiceImpl<AccountRecord , Long> implements AccountRecordService {

    @Resource
    private AccountRecordDao accountRecordDao;

    @Resource
    private AccountService accountService;
    @Resource
    private UserService userService;

    @Override
    public AccountRecord save(AccountRecord ar) {
        return accountRecordDao.save(ar);
    }

    @Override
    public List<AccountRecord> save(List<AccountRecord> ar) {
        return accountRecordDao.saveAll(ar);
    }


    @Override
    public PageVO<AccountRecordVO> list(AccountRecordVO vo, Long pageNum, Long pageSize) {

        Pageable pageReq = PageRequest.of(pageNum.intValue() , pageSize.intValue() , Sort.Direction.DESC , "id");

        AccountRecord ar = new AccountRecord();
        BeanUtils.copyProperties(vo ,ar);
        ar.setYn(YNEnum.YES.getVal());

        Example<AccountRecord> cond = Example.of(ar, ExampleMatcher.matching()
                .withMatcher("operatorId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("accountId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("yn", ExampleMatcher.GenericPropertyMatchers.exact())
        );

        Page<AccountRecord> accounts = accountRecordDao.findAll(cond ,pageReq);

        PageVO<AccountRecordVO> pages = convert2PageVo(accounts , new Function<AccountRecord, AccountRecordVO>() {
            @NullableDecl
            @Override
            public AccountRecordVO apply(@NullableDecl AccountRecord tempAccountRecord) {
                AccountRecordVO tmpAccountRecordVO = new AccountRecordVO();
                BeanUtils.copyProperties(tempAccountRecord ,tmpAccountRecordVO);

                ;
                AccountVO accountVO = accountService.one(tempAccountRecord.getAccountId());
                tmpAccountRecordVO.setAcctName(accountVO.getAcctName());
                UserVO tmpUser = userService.findById(accountVO.getUserId());
                tmpAccountRecordVO.setUserName(tmpUser.getUserName());


                Long operatorId = tmpAccountRecordVO.getOperatorId();
                tmpUser = userService.findById(operatorId);
                tmpAccountRecordVO.setOperatorName(tmpUser.getUserName());
                return tmpAccountRecordVO;
            }
        });
        return pages;
    }
}
