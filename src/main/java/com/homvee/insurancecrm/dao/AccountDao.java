package com.homvee.insurancecrm.dao;

import com.homvee.insurancecrm.dao.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface AccountDao extends JpaRepository<Account, Long> {


    @Modifying
    @Query(value = "update t_account set credit = credit + ?1 , change_time=NOW() where yn=1 and id=?2" ,nativeQuery = true)
    Integer incrCreditById(Long credit, Long id);

    @Modifying
    @Query(value = "update t_account set yn=0, change_time=NOW() where  id in (?1)" ,nativeQuery = true)
    Integer del(ArrayList<Long> ids);

    Account findByUserIdAndYn(Long userId, Integer yn);

    @Modifying
    @Query(value = "update t_account set credit = credit - ?1 , change_time=NOW() where credit-?1>0 and yn=1 and id=?2" ,nativeQuery = true)
    Integer decrCreditById(Long totalCredit, Long id);
}
