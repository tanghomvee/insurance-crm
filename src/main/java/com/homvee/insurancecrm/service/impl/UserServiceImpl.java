package com.homvee.insurancecrm.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.homvee.insurancecrm.dao.UserDao;
import com.homvee.insurancecrm.dao.entities.User;
import com.homvee.insurancecrm.enums.YNEnum;
import com.homvee.insurancecrm.service.UserService;
import com.homvee.insurancecrm.vos.PageVO;
import com.homvee.insurancecrm.vos.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<User , Long> implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    @Lazy
    private UserService userService;

    @Override
    public UserVO findByUserNameOrPhoneNum(String acctName) {
        User user = userDao.findByUserNameAndYn(acctName, YNEnum.YES.getVal());
        if (user == null){
            user = userDao.findByPhoneNumAndYn(acctName , YNEnum.YES.getVal());
        }
        if (user == null){
            return null;
        }
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user , vo);
        return vo;
    }

    @Override
    public int modifyPwdById(String newPwd, Long id) {
        return userDao.modifyUserPwdById(newPwd , id);
    }

    @Override
    public User save(String userName, String phoneNum) {
        User user = new User();
        user.setPhoneNum(phoneNum);
        user.setUserName(userName);
        user.setUserPwd(phoneNum);
        user = userDao.save(user);

        return user;
    }

    @Transactional
    @Override
    public Integer del(Long[] ids) {
        userDao.del(Lists.newArrayList(ids));
        return ids.length;
    }

    @Override
    public PageVO<UserVO> list(UserVO vo, Integer pageNum, Integer pageSize) {
        Pageable pageReq = build(pageNum , pageSize);
        User user = new User();
        BeanUtils.copyProperties(vo , user);
        user.setYn(YNEnum.YES.getVal());

        Example<User> cond = Example.of(user,ExampleMatcher.matching()
                .withMatcher("userName", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("phoneNum", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("creator", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("yn", ExampleMatcher.GenericPropertyMatchers.exact())
        );

        Page<User> users = userDao.findAll(cond ,pageReq);



        PageVO<UserVO> pages = convert2PageVo(users , new Function<User, UserVO>() {
            @NullableDecl
            @Override
            public UserVO apply(@NullableDecl User user) {
                UserVO tmpUserVO = new UserVO();
                BeanUtils.copyProperties(user ,tmpUserVO);
                return tmpUserVO;
            }
        });
        return pages;
    }

    @Override
    public UserVO findById(Long id) {

        Optional<User> user = userDao.findById(id);
        if (!user.isPresent()){
            return  null;
        }
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user.get() , vo);
        return vo;
    }

    @Override
    public void save(UserVO userVO) {
        User user = new User();
        BeanUtils.copyProperties(userVO,user);
        userDao.saveAndFlush(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testTrx(){

        userDao.testTrx("1232");
        userService.testTrx2();
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testTrx2(){

        userDao.testTrx("AAA");

    }
}
