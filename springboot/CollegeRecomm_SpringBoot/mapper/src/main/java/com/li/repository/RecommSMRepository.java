package com.li.repository;

import com.li.entity.RecommSM;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RecommSMRepository  extends ElasticsearchRepository<RecommSM, String> {

    @Query("{\"bool\": {\"must\": [{\"match_phrase\": {\"scode\": {\"query\": \"?0\"}}}, " +
            "{\"match_phrase\": {\"mcode\": {\"query\":\"?1\"}}}]}}")
    RecommSM findByScodeAndMcode(int scode,String mcode);
}
