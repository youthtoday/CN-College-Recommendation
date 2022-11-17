package com.li.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "rk2021",createIndex = true)
@ApiModel(value = "rk2021对象",description = "大学软科排名对象")
public class Rk2021 {
    @Id
    private String id;
    private int school_id;
    private int ranking;
    private String univUrl;
    private String sname;
    private String snameEn;
    private int inbound;
    private String likes;
    private String univTags;
    private String type;
    private String province;
    private int score;
    private String indData;
}
