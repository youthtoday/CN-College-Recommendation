package com.li.service.impl;

import com.li.entity.RecommCs;
import com.li.entity.RecommMs;
import com.li.repository.RecommMsRepository;
import com.li.service.RecommMsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecommMsServiceImpl implements RecommMsService {
    @Autowired
    RecommMsRepository recommMsRepository;

    @Override
    public RecommMs findByMcode(String mcode) {
        return recommMsRepository.findByMcode(mcode);
    }
}
