package com.li.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "xyhrank",createIndex = true)
@ApiModel(value = "xyhrank对象",description = "校友会排名对象")
public class Xyhrank {
    @Id
    private String id;
    private int school_id;
    private int ranking;
    private String univUrl;
    private String sname;
    private String snameEn;
    private String univTags;
    private String type;
    private String province;
}
