package com.li.service.impl;

import com.li.entity.Xyhrank;
import com.li.repository.XyhrankRepository;
import com.li.service.XyhrankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class XyhrankServiceImpl implements XyhrankService {

    @Autowired
    private XyhrankRepository xyhrankRepository;

    @Override
    public Page<Xyhrank> findAll(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return xyhrankRepository.findAll(pageable);
    }

    @Override
    public Page<Xyhrank> findByType(String type, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return xyhrankRepository.findByType(type,pageable);
    }

    @Override
    public Page<Xyhrank> findByProvince(String province, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return xyhrankRepository.findByProvince(province,pageable);
    }

    @Override
    public Page<Xyhrank> findByProvinceAndName(String province, String type, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return xyhrankRepository.findByProvinceAndName(province,type,pageable);
    }
}
