package com.li.service.impl;

import com.li.entity.AllCollegeMajor;
import com.li.entity.Mrank;
import com.li.repository.AllCollegeMajorRepository;
import com.li.service.AllCollegeMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllCollegeMajorServiceImpl implements AllCollegeMajorService {
    @Autowired
    AllCollegeMajorRepository allCollegeMajorRepository;

    @Override
    public Page<AllCollegeMajor> findAllMajorsBySid(int sid, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
//        return allCollegeMajorRepository.findAllMajorsBySid(sid,pageable);
        return allCollegeMajorRepository.findBySchoolidOrderByRatingDesc(sid,pageable);
    }

    @Override
    public Page<AllCollegeMajor> findAllByMid(int mid, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return allCollegeMajorRepository.findBySpecial_id(mid,pageable);
    }
}
