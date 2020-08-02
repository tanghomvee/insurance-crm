package com.homvee.insurancecrm.vos;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */
@Data
public class PageVO<T> implements Serializable {
    private List<T> data;
    private Integer pageSize = 10;
    private Integer pageNum;
    private Integer pages;
    private Integer total;

}
