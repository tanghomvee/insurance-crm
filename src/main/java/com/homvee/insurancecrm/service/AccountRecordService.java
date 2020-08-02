package com.homvee.insurancecrm.service;

import com.homvee.insurancecrm.dao.entities.AccountRecord;
import com.homvee.insurancecrm.vos.AccountRecordVO;
import com.homvee.insurancecrm.vos.PageVO;

import java.util.List;

public interface AccountRecordService extends  BaseService<AccountRecord ,Long> {
    AccountRecord save(AccountRecord ar);
    List<AccountRecord> save(List<AccountRecord> ar);

    PageVO<AccountRecordVO> list(AccountRecordVO vo, Long pageNum, Long pageSize);
}
