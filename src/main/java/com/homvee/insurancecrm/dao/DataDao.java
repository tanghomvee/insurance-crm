package com.homvee.insurancecrm.dao;

import com.homvee.insurancecrm.dao.entities.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DataDao extends JpaRepository<Data, Long> {


    @Query(value = "select * from t_data where yn=1 and frame_no=?1 order by id desc limit 1" , nativeQuery = true)
    Data findByFrameNo(String frameNo);
}
