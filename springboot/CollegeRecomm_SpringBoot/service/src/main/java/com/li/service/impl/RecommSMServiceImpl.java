package com.li.service.impl;

import com.li.entity.RecommSM;
import com.li.repository.RecommSMRepository;
import com.li.service.RecommSMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecommSMServiceImpl implements RecommSMService {
    @Autowired
    RecommSMRepository recommSMRepository;

    @Override
    public RecommSM findByScodeAndMcode(int scode, String mcode) {
        return recommSMRepository.findByScodeAndMcode(scode,mcode);
    }
}
