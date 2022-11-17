package com.li.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Data
@Document(indexName = "all_college_major",createIndex = true)
@ApiModel(value = "AllCollegeMajor对象",description = "大学所有专业信息")
public class AllCollegeMajor {
    @ApiModelProperty(dataType = "String",required = true,value = "id")
    @Id
    private String id;
    @ApiModelProperty(dataType = "int",required = true,value = "学校school_id")
    @Field(name = "school_id")
    private int schoolid;
    @ApiModelProperty(dataType = "int",required = true,value = "专业special_id")
    private int special_id;
    @ApiModelProperty(dataType = "int",required = true,value = "学校school_id")
    private int scode;
    @ApiModelProperty(dataType = "int",required = true,value = "专业special_id")
    private String  spcode;
    @ApiModelProperty(dataType = "String",required = true,value = "专业special_name")
    private String special_name;
    @ApiModelProperty(dataType = "String",required = true,value = "专业类别ID")
    private int class_id;
    @ApiModelProperty(dataType = "String",required = true,value = "专业类别")
    private String type_name;
    @ApiModelProperty(dataType = "String",required = true,value = "专业年份")
    private String limit_year;
    @ApiModelProperty(dataType = "String",required = true,value = "专业年份")
    private float rating;
}
