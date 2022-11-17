package com.li.service;

import com.li.entity.Colleges;
import org.springframework.data.domain.Page;

import java.util.Optional;


public interface CollegesService {
    Page<Colleges> findAll(Integer pageNum,Integer pageSize);
    Page<Colleges> findByName(String name, Integer pageNum,Integer pageSize);
    Page<Colleges> findByNameAndProvince(String name,String province, Integer pageNum,Integer pageSize);
    Colleges findOne(int id);

    Page<Colleges> findByNameAndType(String name, String type, Integer pageNum, Integer pageSize);

    Page<Colleges> findByNameAndProvinceAndName(String name, String province, String type, Integer pageNum, Integer pageSize);

    Page<Colleges> findByType(String type, Integer pageNum, Integer pageSize);

    Page<Colleges> findByProvince(String province, Integer pageNum, Integer pageSize);

    Page<Colleges> findByProvinceAndName(String province, String type, Integer pageNum, Integer pageSize);

    Colleges findByScode(int scode);
}
