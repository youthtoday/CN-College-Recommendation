package com.li.service.impl;

import com.li.dao.MajorMapper;
import com.li.entity.Major;
import com.li.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {
    @Autowired
    MajorMapper majorMapper;

    @Override
    public List<Major> findByName(String name) {
        List<Major> majors = majorMapper.findByName(name);
        System.out.println("majors:"+majors);
        return majors;
    }
}
