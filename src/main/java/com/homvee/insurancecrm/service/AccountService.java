package com.homvee.insurancecrm.service;

import com.homvee.insurancecrm.dao.entities.Account;
import com.homvee.insurancecrm.vos.AccountVO;
import com.homvee.insurancecrm.vos.Msg;
import com.homvee.insurancecrm.vos.PageVO;

import java.util.Set;

public interface AccountService extends BaseService<Account, Long> {

    Long save(AccountVO vo);

    /**
     * 充值
     * @param id
     * @param credit
     * @return
     */
    Integer charge(Long id, Long credit,Long operatorId);

    PageVO<AccountVO> list(AccountVO vo, Long pageNum,Long pageSize);

    AccountVO one(Long id);

    Long edit(AccountVO vo);

    Integer del(Long[] ids);

    boolean exist(Long userId);

    Msg consume(Set<String> matchFrames, Long userId);
}
