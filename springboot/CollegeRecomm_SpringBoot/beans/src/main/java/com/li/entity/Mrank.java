package com.li.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "mrank",createIndex = true)
@ApiModel(value = "Mrank对象",description = "大学学科排名对象")
public class Mrank {
    @Id
    private String id;
    private Integer rid;
    private Integer xid;
    private String percent;
    private String result;
    private String name;
    private String province_name;
    private String belong;
    private int dual_class;
    private String dual_class_name;
    private int f211;
    private int f985;
    private String nature_name;
    private int rank;
    private int school_id;
    private int type;
    private String type_name;
    private String nameEn;
}
