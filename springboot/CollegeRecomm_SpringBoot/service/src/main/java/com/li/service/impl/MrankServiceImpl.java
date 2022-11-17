package com.li.service.impl;

import com.li.entity.Mrank;
import com.li.repository.MrankRepository;
import com.li.service.MrankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MrankServiceImpl implements MrankService {
    @Autowired
    MrankRepository mrankRepository;

    @Override
    public Page<Mrank> findCollegesByXid(Integer xid, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Mrank> mranks = mrankRepository.findCollegesByXid(xid, pageable);
        return mranks;
    }
}
