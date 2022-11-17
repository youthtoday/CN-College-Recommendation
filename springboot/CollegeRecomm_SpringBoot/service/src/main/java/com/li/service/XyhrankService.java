package com.li.service;

import com.li.entity.Xyhrank;
import org.springframework.data.domain.Page;

public interface XyhrankService {
    Page<Xyhrank> findAll(Integer pageNum, Integer pageSize);

    Page<Xyhrank> findByType(String type, Integer pageNum, Integer pageSize);

    Page<Xyhrank> findByProvince(String province, Integer pageNum, Integer pageSize);

    Page<Xyhrank> findByProvinceAndName(String province, String type, Integer pageNum, Integer pageSize);
}
