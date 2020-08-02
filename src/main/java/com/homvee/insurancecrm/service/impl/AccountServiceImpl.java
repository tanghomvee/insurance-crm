package com.homvee.insurancecrm.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.homvee.insurancecrm.dao.AccountDao;
import com.homvee.insurancecrm.dao.entities.Account;
import com.homvee.insurancecrm.dao.entities.AccountRecord;
import com.homvee.insurancecrm.enums.TradeTypeEnum;
import com.homvee.insurancecrm.enums.YNEnum;
import com.homvee.insurancecrm.service.AccountRecordService;
import com.homvee.insurancecrm.service.AccountService;
import com.homvee.insurancecrm.vos.AccountVO;
import com.homvee.insurancecrm.vos.Msg;
import com.homvee.insurancecrm.vos.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class AccountServiceImpl extends BaseServiceImpl<Account,Long> implements AccountService {
    @Resource
    private AccountDao accountDao;
    @Resource
    private AccountRecordService accountRecordService;
    @Override
    public Long save(AccountVO vo) {
        Account account = new Account();

        BeanUtils.copyProperties(vo , account);
        account.setCreateTime(DateTime.now().toDate());
        account.setYn(YNEnum.YES.getVal());
        account.setCredit(0L);
        return accountDao.save(account).getId();
    }

    @Transactional
    @Override
    public synchronized Integer charge(Long id, Long credit,Long operatorId) {
        Integer rs = accountDao.incrCreditById(credit , id);
        AccountRecord ar = new AccountRecord();
        ar.setAccountId(id);
        ar.setOperatorId(operatorId);
        ar.setTradeCredit(credit);
        ar.setTradeDate(DateTime.now().toDate());
        ar.setTradeType(TradeTypeEnum.CHARGE.getVal().byteValue());
        ar.setNote(TradeTypeEnum.CHARGE.getDesc());
        ar.setCreateTime(DateTime.now().toDate());
        ar.setCreator(operatorId.toString());
        accountRecordService.save(ar);
        return rs;
    }

    @Override
    public PageVO<AccountVO> list(AccountVO vo, Long pageNum, Long pageSize) {

        Pageable pageReq = build(pageNum.intValue(), pageSize.intValue());
        Account account = new Account();
        BeanUtils.copyProperties(vo , account);
        account.setYn(YNEnum.YES.getVal());

        Example<Account> cond = Example.of(account, ExampleMatcher.matching()
                .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("creator", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("yn", ExampleMatcher.GenericPropertyMatchers.exact())
        );

        Page<Account> accounts = accountDao.findAll(cond ,pageReq);

        PageVO<AccountVO> pages = convert2PageVo(accounts , new Function<Account, AccountVO>() {
            @NullableDecl
            @Override
            public AccountVO apply(@NullableDecl Account account1) {
                AccountVO tmpAccountVO = new AccountVO();
                BeanUtils.copyProperties(account1 ,tmpAccountVO);
                return tmpAccountVO;
            }
        });
        return pages;
    }

    @Override
    public AccountVO one(Long id) {
        Account account = accountDao.getOne(id);
        if (account == null){
            return null;
        }
        AccountVO vo = new AccountVO();
        BeanUtils.copyProperties(account ,vo);
        return vo;
    }

    @Override
    public synchronized Long edit(AccountVO vo) {
        Account account = new Account();
        BeanUtils.copyProperties(vo ,account);
        return accountDao.saveAndFlush(account).getId();
    }

    @Transactional
    @Override
    public  synchronized Integer del(Long[] ids) {
        return accountDao.del(Lists.newArrayList(ids));
    }

    @Override
    public boolean exist(Long userId) {
        Account account = accountDao.findByUserIdAndYn(userId , YNEnum.YES.getVal());
        return account != null && account.getCredit() > 0;
    }

    @Transactional
    @Override
    public synchronized Msg consume(Set<String> matchFrames, Long userId) {
        Account account = accountDao.findByUserIdAndYn(userId , YNEnum.YES.getVal());
        if (account == null || account.getCredit() < matchFrames.size()){
            return Msg.error("余额不足");
        }
        List<AccountRecord> accountRecords = Lists.newArrayList();
        Long totalCredit = 0L;
        for (String frameNo : matchFrames){
            AccountRecord record = new AccountRecord();
            record.setCreator(userId.toString());
            record.setCreateTime(DateTime.now().toDate());
            record.setYn(YNEnum.YES.getVal());
            record.setTradeDate(DateTime.now().toDate());
            record.setNote("匹配车辆信息消费:" + frameNo);
            record.setAccountId(account.getId());
            record.setOperatorId(userId);
            record.setTradeCredit(1L);
            record.setTradeType(TradeTypeEnum.MATCH.getVal().byteValue());
            totalCredit = totalCredit + record.getTradeCredit();
            accountRecords.add(record);
        }

        accountRecordService.save(accountRecords);

        accountDao.decrCreditById(totalCredit , account.getId());

        return Msg.success();
    }
}
