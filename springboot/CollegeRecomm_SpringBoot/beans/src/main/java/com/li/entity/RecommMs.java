package com.li.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;

@Data
@Document(indexName = "recommms",createIndex = true)
@ApiModel(value = "recommms对象",description = "专业推荐对象")
public class RecommMs {
    @Id
    private String id;
    private int class_id;
    private String mcode;
    private String confidence;
}
