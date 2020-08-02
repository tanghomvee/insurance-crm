package com.homvee.insurancecrm.dao;

import com.homvee.insurancecrm.dao.entities.Power;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PowerDao extends JpaRepository<Power, Long> {


    @Modifying
    @Query(value = "update t_power set yn=0 ,change_time=now()where id=?1",nativeQuery = true)
    Integer del(Long id);
}
