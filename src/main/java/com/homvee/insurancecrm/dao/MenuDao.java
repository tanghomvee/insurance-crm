package com.homvee.insurancecrm.dao;

import com.homvee.insurancecrm.dao.entities.Menu;
import com.homvee.insurancecrm.vos.MenuVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuDao extends JpaRepository<Menu, Long> {


    @Query(value = "select new com.homvee.insurancecrm.vos.MenuVO(" +
            "child.id, child.menuName, child.menuType, child.url,child.parentId, parent.menuName" +
            ") from Menu child left join Menu parent on parent.id = child.parentId " +
            "where child.yn=1 and child.menuName=:#{#vo.menuName}")
    Page<MenuVO> list(@Param("vo") MenuVO vo, Pageable pageReq);

    @Modifying
    @Query(value = "update t_menu set yn =0 where id=?1" ,nativeQuery = true)
    Integer del(Long id);
}
