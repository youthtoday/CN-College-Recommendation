package com.li.repository;

import com.li.entity.RecommCs;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RecommCsRepository extends ElasticsearchRepository<RecommCs, String> {
    @Query("{\"match_phrase\": {\"school_id\": {\"query\":\"?0\"}}}")
    RecommCs findBySid(int scode);
}
