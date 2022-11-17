package com.li.repository;

import com.li.entity.Colleges;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CollegesRepository extends ElasticsearchRepository<Colleges, String> {
    //通过大学名查找
    @Query("{\"match\": {\"name\": {\"query\": \"?0\"}}}")
    Page<Colleges> findByName(String name, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"name\": {\"query\": \"?0\"}}}, " +
            "{\"match_phrase\": {\"province_name\": {\"query\":\"?1\"}}}]}}")
    Page<Colleges> findByNameAndProvince(String name, String province, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"name\": {\"query\": \"?0\"}}}, " +
            "{\"match_phrase\": {\"type_name\": {\"query\":\"?1\"}}}]}}")
    Page<Colleges> findByNameAndType(String name, String type, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"name\": {\"query\": \"?0\"}}}, " +
            "{\"match_phrase\": {\"province_name\": {\"query\":\"?1\"}}}, " +
            "{\"match_phrase\": {\"type_name\": {\"query\":\"?2\"}}}]}}")
    Page<Colleges> findByNameAndProvinceAndName(String name, String province, String type, Pageable pageable);

    @Query("{\"match_phrase\": {\"type_name\": {\"query\":\"?0\"}}}")
    Page<Colleges> findByType(String type, Pageable pageable);

    @Query("{\"match_phrase\": {\"province_name\": {\"query\":\"?0\"}}}")
    Page<Colleges> findByProvince(String province, Pageable pageable);

    @Query("{\"bool\": {\"must\": [" +
            "{\"match_phrase\": {\"province_name\": {\"query\":\"?0\"}}}, " +
            "{\"match_phrase\": {\"type_name\": {\"query\":\"?1\"}}}]}}")
    Page<Colleges> findByProvinceAndName(String province, String type, Pageable pageable);

    @Query("{\"match_phrase\": {\"school_id\": {\"query\":\"?0\"}}}")
    Colleges findBySid(int sid);

    @Query("{\"match_phrase\": {\"scode\": {\"query\":\"?0\"}}}")
    Colleges findByScode(int scode);
}
