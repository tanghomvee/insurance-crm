package com.homvee.insurancecrm.service;

import com.homvee.insurancecrm.dao.entities.Menu;
import com.homvee.insurancecrm.vos.MenuVO;
import com.homvee.insurancecrm.vos.PageVO;

public interface MenuService extends BaseService<Menu,Long>{
    PageVO<MenuVO> list(MenuVO vo, Long pageNum, Long pageSize);

    Menu save(Menu menu);

    Integer del(Long id);
}
