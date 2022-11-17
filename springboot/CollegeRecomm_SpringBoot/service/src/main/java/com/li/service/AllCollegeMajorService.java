package com.li.service;

import com.li.entity.AllCollegeMajor;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AllCollegeMajorService {
    Page<AllCollegeMajor> findAllMajorsBySid(int sid, Integer pageNum, Integer pageSize);
    Page<AllCollegeMajor> findAllByMid(int mid, Integer pageNum, Integer pageSize);
}
