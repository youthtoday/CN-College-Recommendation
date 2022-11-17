package com.li.dao;

import com.li.entity.Category;
import com.li.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMapper extends JpaRepository<Category,Integer> {

    @Query(value = "select c_name from category where id = ?1",nativeQuery = true)
    public String findCNameById(Integer cid);

}
