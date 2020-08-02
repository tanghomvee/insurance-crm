package com.homvee.insurancecrm.service;

import com.homvee.insurancecrm.dao.entities.UserData;
import com.homvee.insurancecrm.vos.Msg;
import com.homvee.insurancecrm.vos.PageVO;
import com.homvee.insurancecrm.vos.UserDataVO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

public interface UserDataService extends BaseService<UserData , Long> {
    PageVO<UserDataVO> list(UserDataVO vo, Integer pageNum, Integer pageSize);

    Msg save(List<UserData> datas , Long userId);

    OutputStream download(Date beginDate, Date finishDate, Long id) throws IOException;
}
