package com.li.service.impl;

import com.li.entity.Colleges;
import com.li.repository.CollegesRepository;
import com.li.service.CollegesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CollegesServiceImpl implements CollegesService {
    @Autowired
    private CollegesRepository college1Repository;


    @Override
    public Page<Colleges> findAll(Integer pageNum,Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        //构建查询
        return college1Repository.findAll(pageable);
    }

    @Override
    public Page<Colleges> findByName(String name, Integer pageNum,Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return college1Repository.findByName(name,pageable);
    }

    @Override
    public Page<Colleges> findByNameAndProvince(String name, String province, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return college1Repository.findByNameAndProvince(name,province,pageable);
    }

    @Override
    public Colleges findOne(int id) {
        System.out.println("service:"+id);
        Colleges optional = college1Repository.findBySid(id);
        System.out.println("service:"+optional);
        return optional;
    }

    @Override
    public Page<Colleges> findByNameAndType(String name, String type, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return college1Repository.findByNameAndType(name,type,pageable);
    }

    @Override
    public Page<Colleges> findByNameAndProvinceAndName(String name, String province, String type, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return college1Repository.findByNameAndProvinceAndName(name,province,type,pageable);
    }

    @Override
    public Page<Colleges> findByType(String type, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return college1Repository.findByType(type,pageable);
    }

    @Override
    public Page<Colleges> findByProvince(String province, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return college1Repository.findByProvince(province,pageable);
    }

    @Override
    public Page<Colleges> findByProvinceAndName(String province, String type, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return college1Repository.findByProvinceAndName(province,type,pageable);
    }

    @Override
    public Colleges findByScode(int scode) {
        return college1Repository.findByScode(scode);
    }
}
