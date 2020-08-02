package com.homvee.insurancecrm.service.impl;

import com.homvee.insurancecrm.dao.MenuDao;
import com.homvee.insurancecrm.dao.entities.Menu;
import com.homvee.insurancecrm.service.MenuService;
import com.homvee.insurancecrm.vos.MenuVO;
import com.homvee.insurancecrm.vos.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
public class MenuServiceImpl extends BaseServiceImpl<Menu , Long> implements MenuService {
    @Resource
    private MenuDao menuDao;

    @Override
    public PageVO<MenuVO> list(MenuVO vo, Long pageNum, Long pageSize) {
        Pageable pageReq = PageRequest.of(pageNum.intValue() , pageSize.intValue() , Sort.Direction.DESC , "id");
        Page<MenuVO> menuVOS = menuDao.list(vo , pageReq);

        return convert2PageVo(menuVOS);
    }

    @Override
    public Menu save(Menu menu) {
        return menuDao.save(menu);
    }

    @Transactional
    @Override
    public Integer del(Long id) {
        return menuDao.del(id);

    }
}
