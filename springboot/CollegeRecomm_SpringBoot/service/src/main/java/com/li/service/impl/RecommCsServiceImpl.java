package com.li.service.impl;

import com.li.entity.RecommCs;
import com.li.repository.RecommCsRepository;
import com.li.service.RecommCsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecommCsServiceImpl implements RecommCsService {
    @Autowired
    RecommCsRepository recommCsRepository;

    @Override
    public RecommCs findOne(int scode) {
        return recommCsRepository.findBySid(scode);
    }
}
