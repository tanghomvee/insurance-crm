package com.homvee.insurancecrm.dao;

import com.homvee.insurancecrm.dao.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RoleDao extends JpaRepository<Role, Long> {


    @Modifying
    @Query(value = "update t_role set yn =0 where id=?1" , nativeQuery = true)
    Integer del(Long id);


}
