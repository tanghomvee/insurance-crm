package com.homvee.insurancecrm.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.homvee.insurancecrm.service.BaseService;
import com.homvee.insurancecrm.vos.PageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 *
 * @author Administrator
 * @date 2017/7/14
 */
public class BaseServiceImpl<T  , PK extends Serializable> implements BaseService<T, PK> {

    protected  <M,S> PageVO<M> convert2PageVo(Page<S> page, Function<S, M> function) {
        PageVO<M> pages = new PageVO<>();
        pages.setData(Lists.transform(page.getContent(), function));

        pages.setPages(page.getTotalPages());
        pages.setTotal(Long.valueOf(page.getTotalElements()).intValue());
        pages.setPageSize(page.getSize());
        pages.setPageNum(page.getNumber());
        return pages;
    }
    protected  <M> PageVO<M> convert2PageVo(Page<M> page) {
        PageVO<M> pages = new PageVO<>();
        pages.setData(page.getContent());

        pages.setPages(page.getTotalPages());
        pages.setTotal(Long.valueOf(page.getTotalElements()).intValue());
        pages.setPageSize(page.getSize());
        pages.setPageNum(page.getNumber());
        return pages;
    }

    protected Pageable build(Integer pageNum ,Integer pageSize){
        return PageRequest.of(pageNum , pageSize , Sort.Direction.DESC , "id");
    }
}
