package com.li.service.impl;

import com.li.dao.CategoryMapper;
import com.li.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> findAll(){
        List<Category> all = categoryMapper.findAll();
        return all;
    }

}
