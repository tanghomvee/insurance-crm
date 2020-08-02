package com.homvee.insurancecrm.dao;

import com.homvee.insurancecrm.dao.entities.AccountRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRecordDao extends JpaRepository<AccountRecord, Long> {

}
