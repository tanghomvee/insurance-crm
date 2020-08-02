package com.homvee.insurancecrm.service;

import com.homvee.insurancecrm.dao.entities.Data;
import com.homvee.insurancecrm.vos.DataVO;
import com.homvee.insurancecrm.vos.PageVO;

import java.util.List;

public interface DataService extends BaseService<Data , Long> {
    PageVO<DataVO> list(DataVO vo, Integer pageNum, Integer pageSize);

    List<Data> save(List<Data> datas);

    Data findByFrameNo(String frameNo);
}
