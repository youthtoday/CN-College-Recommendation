package com.li.service.impl;

import com.li.entity.Rk2021;
import com.li.repository.Rk2021Repository;
import com.li.service.Rk2021Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class Rk2021ServiceImpl implements Rk2021Service {

    @Autowired
    Rk2021Repository rk2021Repository;

    @Override
    public Page<Rk2021> findAll(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return rk2021Repository.findAll(pageable);
    }

    @Override
    public Page<Rk2021> findByType(String type, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return rk2021Repository.findByType(type,pageable);
    }

    @Override
    public Page<Rk2021> findByProvince(String province, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return rk2021Repository.findByProvince(province,pageable);
    }

    @Override
    public Page<Rk2021> findByProvinceAndName(String province, String type, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return rk2021Repository.findByProvinceAndName(province,type,pageable);
    }
}
