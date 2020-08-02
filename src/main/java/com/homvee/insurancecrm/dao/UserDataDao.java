package com.homvee.insurancecrm.dao;

import com.homvee.insurancecrm.dao.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UserDataDao extends JpaRepository<UserData, Long> {


    List<UserData> findByYnAndCreatorAndCreateTimeBetween(Integer val, String creator, Date beginDate, Date finishDate);
}
