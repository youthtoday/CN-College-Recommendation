package com.li.dao;

import com.li.entity.HotSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotSearchMapper extends JpaRepository<HotSearch,Integer> {

}
