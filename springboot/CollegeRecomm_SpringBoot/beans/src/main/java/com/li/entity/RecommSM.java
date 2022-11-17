package com.li.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;

@Data
@Document(indexName = "recommsm",createIndex = true)
@ApiModel(value = "recommsm对象",description = "大学专业推荐对象")
public class RecommSM {
    @Id
    private String id;
    private int scode;
    private int class_id;
    private String mcode;
    private String recommend;
}
