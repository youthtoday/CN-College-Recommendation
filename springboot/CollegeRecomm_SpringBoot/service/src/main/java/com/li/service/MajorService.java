package com.li.service;

import com.li.entity.Major;

import java.util.List;

public interface MajorService {
    public List<Major> findByName(String Name);
}
