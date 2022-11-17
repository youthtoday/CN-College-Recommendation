package com.li.repository;

import com.li.entity.Major;
import com.li.entity.Majors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorsReposiory extends ElasticsearchRepository<Majors, String > {
    //通过专业名查找
    @Query("{\"match_phrase\": {\"name\": {\"query\": \"?0\"}}}")
    List<Majors> findByName(String name);
    @Query("{\"match_phrase\": {\"spcode\": {\"query\": \"?0\"}}}")
    Majors findBySpcode(String spcode);

}
