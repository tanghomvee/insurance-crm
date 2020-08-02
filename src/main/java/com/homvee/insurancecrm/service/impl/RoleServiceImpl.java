package com.homvee.insurancecrm.service.impl;

import com.google.common.base.Function;
import com.homvee.insurancecrm.dao.RoleDao;
import com.homvee.insurancecrm.dao.entities.Role;
import com.homvee.insurancecrm.enums.YNEnum;
import com.homvee.insurancecrm.service.RoleService;
import com.homvee.insurancecrm.vos.PageVO;
import com.homvee.insurancecrm.vos.RoleVO;
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

@Service
@Slf4j
public class RoleServiceImpl extends BaseServiceImpl<Role,Long> implements RoleService {
    @Resource
    private RoleDao roleDao;

    @Override
    public Role save(Role role) {
        return roleDao.save(role);
    }

    @Transactional
    @Override
    public Integer del(Long id) {
        return roleDao.del(id);
    }

    @Override
    public PageVO<RoleVO> list(RoleVO roleVO, Integer pageNum, Integer pageSize) {
        Pageable pageReq = build(pageNum , pageSize);

        Role role = new Role();
        BeanUtils.copyProperties(roleVO , role);
        role.setYn(YNEnum.YES.getVal());
        Example<Role> example = Example.of(role , ExampleMatcher.matching()
                .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("yn", ExampleMatcher.GenericPropertyMatchers.exact())
        );

        Page<Role> roles = roleDao.findAll(example,pageReq);
        return convert2PageVo(roles, new Function<Role, RoleVO>() {
            @NullableDecl
            @Override
            public RoleVO apply(@NullableDecl Role role) {
                RoleVO vo = new RoleVO();
                BeanUtils.copyProperties(role , vo);
                return vo;
            }
        });
    }
}
