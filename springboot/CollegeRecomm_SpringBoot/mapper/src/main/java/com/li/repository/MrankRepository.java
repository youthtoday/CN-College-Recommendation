package com.li.repository;

import com.li.entity.Mrank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MrankRepository extends ElasticsearchRepository<Mrank, String> {

    @Query("{\"match_phrase\": {\"xid\": {\"query\": ?0}}}")
    Page<Mrank> findCollegesByXid(Integer xid, Pageable pageable);
}
