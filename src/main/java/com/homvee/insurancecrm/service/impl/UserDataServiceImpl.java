package com.homvee.insurancecrm.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.homvee.insurancecrm.dao.UserDataDao;
import com.homvee.insurancecrm.dao.entities.UserData;
import com.homvee.insurancecrm.enums.MatchStateEnum;
import com.homvee.insurancecrm.enums.YNEnum;
import com.homvee.insurancecrm.service.AccountService;
import com.homvee.insurancecrm.service.UserDataService;
import com.homvee.insurancecrm.utils.ExcelUtils;
import com.homvee.insurancecrm.vos.Msg;
import com.homvee.insurancecrm.vos.PageVO;
import com.homvee.insurancecrm.vos.UserDataVO;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class UserDataServiceImpl extends BaseServiceImpl<UserData,Long> implements UserDataService {
    @Resource
    private UserDataDao userDataDao;

    @Resource
    private AccountService accountService;
    @Override
    public PageVO<UserDataVO> list(UserDataVO vo, Integer pageNum, Integer pageSize) {
        Pageable pageReq = build(pageNum, pageSize);
        UserData data = new UserData();
        BeanUtils.copyProperties(vo , data);
        data.setYn(YNEnum.YES.getVal());

        Example<UserData> cond = Example.of(data, ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("crateTime")
                .withMatcher("frameNo", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("matchState", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("yn", ExampleMatcher.GenericPropertyMatchers.exact())
        );

        Page<UserData> all = userDataDao.findAll(cond ,pageReq);

        PageVO<UserDataVO> pages = convert2PageVo(all , new Function<UserData, UserDataVO>() {
            @NullableDecl
            @Override
            public UserDataVO apply(@NullableDecl UserData tmpData) {
                UserDataVO userDataVO = new UserDataVO();
                BeanUtils.copyProperties(tmpData ,userDataVO);
                return userDataVO;
            }
        });
        return pages;
    }

    @Transactional
    @Override
    public  Msg save(List<UserData> datas , Long userId) {

        Set<String> matchFrames = Sets.newHashSet();
        for (UserData userData : datas){
            if (!MatchStateEnum.MATCHED.getVal().equals(userData.getMatchState())){
                continue;
            }
            matchFrames.add(userData.getFrameNo());
        }
       Msg msg = accountService.consume(matchFrames , userId);
        if (!msg.isSuccess()){
            return msg;
        }
        userDataDao.saveAll(datas);
        return Msg.success();
    }

    @Override
    public OutputStream download(Date beginDate, Date finishDate, Long id) throws IOException {
        if (beginDate == null){
            beginDate = DateTime.now().minusYears(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
        }
        if (finishDate == null){
            finishDate = DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();
        }
        List<UserData> userData = userDataDao.findByYnAndCreatorAndCreateTimeBetween(YNEnum.YES.getVal() , id + "",beginDate ,finishDate);
        /*UserData userData =new UserData();
        userData.setYn(YNEnum.YES.getVal());

        Example<UserData> cond = Example.of(userData, ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("crateTime" , ExampleMatcher.GenericPropertyMatchers.)
                .withMatcher("yn", ExampleMatcher.GenericPropertyMatchers.exact())
        );*/
        if (CollectionUtils.isEmpty(userData)){
            Map<String,String> dataMap = Maps.newHashMap();
            dataMap.put("自带车牌","");
            dataMap.put("车辆品牌","");
            dataMap.put("识别代码","");
            dataMap.put("保险公司","");
            dataMap.put("商业险保单号","");
            dataMap.put("终保日期","");
            dataMap.put("车辆使用价值","");
            dataMap.put("商业险原价","");
            dataMap.put("商业险折扣","");
            dataMap.put("商业险优惠后","");
            dataMap.put("交强险","");
            dataMap.put("车船税","");
            dataMap.put("内容","");
            return ExcelUtils.download("匹配的数据" , Lists.newArrayList(dataMap));
        }

        List<Map<String,String>> excelData = Lists.transform(userData, new Function<UserData, Map<String, String>>() {
            @NullableDecl
            @Override
            public Map<String, String> apply(@NullableDecl UserData data) {
                Map<String,String> dataMap = Maps.newHashMap();
                dataMap.put("自带车牌", data.getCarNo());
                dataMap.put("车辆品牌", data.getBrand());
                dataMap.put("识别代码", data.getFrameNo());
                dataMap.put("保险公司", data.getCompany());
                dataMap.put("商业险保单号", data.getViPolicyNo());
                dataMap.put("终保日期", data.getFinalInsDate());
                dataMap.put("车辆使用价值", data.getUseVal());
                dataMap.put("商业险原价", data.getViAmount());
                dataMap.put("商业险折扣", data.getViDiscount());
                dataMap.put("商业险优惠后", data.getPayViAmt());
                dataMap.put("交强险", data.getCiAmount());
                dataMap.put("车船税", data.getVvt());
                dataMap.put("内容", data.getContent());
                return dataMap;
            }
        });



        return ExcelUtils.download("匹配的数据" , excelData);
    }
}
