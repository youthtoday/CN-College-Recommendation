package com.li.repository;

import com.li.entity.Rk2021;
import com.li.entity.Xyhrank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XyhrankRepository extends ElasticsearchRepository<Xyhrank, String> {
    @Query("{\"match\": {\"type\": {\"query\": \"?0\"}}}")
    Page<Xyhrank> findByType(String type, Pageable pageable);
    @Query("{\"match\": {\"province\": {\"query\": \"?0\"}}}")
    Page<Xyhrank> findByProvince(String province, Pageable pageable);
    @Query("{\"bool\": {\"must\": [{\"match\": {\"province\": {\"query\": \"?0\"}}}, " +
            "{\"match_phrase\": {\"type\": {\"query\":\"?1\"}}}]}}")
    Page<Xyhrank> findByProvinceAndName(String province, String type, Pageable pageable);
}
