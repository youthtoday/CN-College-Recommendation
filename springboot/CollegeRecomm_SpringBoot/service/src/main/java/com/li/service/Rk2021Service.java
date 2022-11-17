package com.li.service;

import com.li.entity.Rk2021;
import org.springframework.data.domain.Page;

public interface Rk2021Service {
    Page<Rk2021> findAll(Integer pageNum, Integer pageSize);
    Page<Rk2021> findByType(String type,Integer pageNum,Integer pageSize);
    Page<Rk2021> findByProvince(String province,Integer pageNum,Integer pageSize);
    Page<Rk2021> findByProvinceAndName(String province,String type,Integer pageNum,Integer pageSize);
}
