package com.li.repository;

import com.li.entity.AllCollegeMajor;
import com.li.entity.Colleges;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllCollegeMajorRepository extends ElasticsearchRepository<AllCollegeMajor, String> {
    @Query("{\"match\": {\"school_id\": {\"query\":\"?0\"}}}")
    Page<AllCollegeMajor> findAllMajorsBySid(int sid,Pageable pageable);

    Page<AllCollegeMajor> findBySchoolidOrderByRatingDesc(int sid,Pageable pageable);

    @Query("{\"match\": {\"special_id\": {\"query\":\"?0\"}}}")
    Page<AllCollegeMajor> findBySpecial_id(int mid,Pageable pageable);

}
