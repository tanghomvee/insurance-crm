package com.homvee.insurancecrm.service.impl;

import com.google.common.base.Function;
import com.homvee.insurancecrm.dao.DataDao;
import com.homvee.insurancecrm.dao.entities.Account;
import com.homvee.insurancecrm.dao.entities.Data;
import com.homvee.insurancecrm.enums.YNEnum;
import com.homvee.insurancecrm.service.DataService;
import com.homvee.insurancecrm.vos.AccountVO;
import com.homvee.insurancecrm.vos.DataVO;
import com.homvee.insurancecrm.vos.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class DataServiceImpl extends BaseServiceImpl<Data,Long> implements DataService {
    @Resource
    private DataDao dataDao;

    @Override
    public PageVO<DataVO> list(DataVO vo, Integer pageNum, Integer pageSize) {
        Pageable pageReq = build(pageNum, pageSize);
        Data data = new Data();
        BeanUtils.copyProperties(vo , data);
        data.setYn(YNEnum.YES.getVal());

        Example<Data> cond = Example.of(data, ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("crateTime")
                .withMatcher("carNo", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("frameNo", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("yn", ExampleMatcher.GenericPropertyMatchers.exact())
        );

        Page<Data> all = dataDao.findAll(cond ,pageReq);

        PageVO<DataVO> pages = convert2PageVo(all , new Function<Data, DataVO>() {
            @NullableDecl
            @Override
            public DataVO apply(@NullableDecl Data tmpData) {
                DataVO dataVO = new DataVO();
                BeanUtils.copyProperties(tmpData ,dataVO);
                return dataVO;
            }
        });
        return pages;
    }

    @Transactional
    @Override
    public List<Data> save(List<Data> datas) {
        return dataDao.saveAll(datas);
    }

    @Override
    public Data findByFrameNo(String frameNo) {
        return dataDao.findByFrameNo(frameNo);
    }
}
