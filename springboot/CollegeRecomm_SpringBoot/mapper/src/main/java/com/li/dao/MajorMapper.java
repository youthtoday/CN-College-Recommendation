package com.li.dao;

import com.li.entity.Major;
import com.li.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorMapper extends JpaRepository<Major,Integer> {

    @Query(value = "SELECT * FROM  major WHERE cid = ?1",nativeQuery = true)
    List<Major > findMajorsByCid(Integer cid);

    @Query(value = "SELECT * FROM  major WHERE name LIKE CONCAT('%',:name,'%')",nativeQuery = true)
    List<Major> findByName(@Param("name") String name);


}
