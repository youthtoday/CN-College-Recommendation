package com.li.entity;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.Id;

@Data
@Document(indexName = "recommcss",createIndex = true)
@ApiModel(value = "recommcs对象",description = "大学推荐对象")
public class RecommCs {
    @Id
    private String id;
    private int scode;
    private int sim1;
    private int sim2;
    private int sim3;
    private int sim4;
    private int sim5;
    private int sim6;
    private int sim7;
    private int sim8;
    private int sim9;
    private int sim10;
}
