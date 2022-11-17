package com.li.service;

import com.li.entity.RecommSM;

public interface RecommSMService {
    RecommSM findByScodeAndMcode(int scode,String mcode);
}
