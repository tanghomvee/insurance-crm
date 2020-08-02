package com.homvee.insurancecrm.dao;

import com.homvee.insurancecrm.dao.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao  extends JpaRepository<User, Long> {

    User findByUserNameAndYn(String acctName,Integer yn);

    User findByPhoneNumAndYn(String phoneNum,Integer yn);

    @Modifying
    @Query(value = "update t_user set user_pwd=?1 ,change_time=now() where id in(?2)" , nativeQuery = true)
    int modifyUserPwdById(String newPwd, Long id);

    @Modifying
    @Query(value = "update t_user set yn=0 ,change_time=now() where id in(?1)" , nativeQuery = true)
    Integer del(List<Long> ids);

}
