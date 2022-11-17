package com.li.service.impl;

import com.li.entity.Majors;
import com.li.repository.MajorsReposiory;
import com.li.service.MajorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorsServiceImpl implements MajorsService {
    @Autowired
    private MajorsReposiory majorsReposiory;
    @Override
    public Page<Majors> findAll(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return majorsReposiory.findAll(pageable);
    }

    @Override
    public List<Majors> findByName(String name) {
        return majorsReposiory.findByName(name);
    }

    @Override
    public Majors findBySpcode(String spcode) {
        return majorsReposiory.findBySpcode(spcode);
    }


}
