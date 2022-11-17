package com.li.service;

import com.li.entity.Majors;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MajorsService {
    Page<Majors> findAll(Integer pageNum, Integer pageSize);
    List<Majors> findByName(String name);
    Majors findBySpcode(String spcode);
}
