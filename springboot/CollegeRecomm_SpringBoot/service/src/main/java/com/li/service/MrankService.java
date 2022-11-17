package com.li.service;

import com.li.entity.Mrank;
import org.springframework.data.domain.Page;

public interface MrankService {
    Page<Mrank> findCollegesByXid(Integer id, Integer pageNum, Integer pageSize);
}
