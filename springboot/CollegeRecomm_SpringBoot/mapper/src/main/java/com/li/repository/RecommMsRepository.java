package com.li.repository;

import com.li.entity.RecommMs;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RecommMsRepository  extends ElasticsearchRepository<RecommMs, String> {
    @Query("{\"match_phrase\": {\"mcode\": {\"query\": \"?0\"}}}")
    RecommMs findByMcode(String mcode);
}
