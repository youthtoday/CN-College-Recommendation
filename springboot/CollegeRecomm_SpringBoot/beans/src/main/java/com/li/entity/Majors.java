package com.li.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "majors",createIndex = true)
@ApiModel(value = "Majors对象",description = "专业信息")
public class Majors {
    @Id
    private String id;
    private int special_id;
    private String name;
    private Integer boy_rate;
    private Integer girl_rate;
    private String degree;
    private String level1_name;
    private String level2_name;
    private String level3_name;
    private String limit_year;
    private String spcode;
    private String is_what;
    private String learn_what;
    private String do_what;
    private String job;
    private String content;
    private String sel_adv;
    private String course;
    private String direction;
    private String celebrity;
}
