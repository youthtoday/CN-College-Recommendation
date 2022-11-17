package com.li.repository;

import com.li.entity.Colleges;
import com.li.entity.Rk2021;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Rk2021Repository extends ElasticsearchRepository<Rk2021, String> {

    @Query("{\"match_phrase\": {\"type\": {\"query\": \"?0\"}}}")
    Page<Rk2021> findByType(String type, Pageable pageable);
    @Query("{\"match_phrase\": {\"province\": {\"query\": \"?0\"}}}")
    Page<Rk2021> findByProvince(String province, Pageable pageable);
    @Query("{\"bool\": {\"must\": [{\"match_phrase\": {\"province\": {\"query\": \"?0\"}}}, " +
            "{\"match_phrase\": {\"type\": {\"query\":\"?1\"}}}]}}")
    Page<Rk2021> findByProvinceAndName(String province, String type, Pageable pageable);
}
