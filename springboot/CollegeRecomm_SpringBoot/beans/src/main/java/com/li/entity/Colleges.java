package com.li.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Data
@Document(indexName = "colleges",createIndex = true)
@ApiModel(value = "Colleges对象",description = "大学信息")
public class Colleges {
    @ApiModelProperty(dataType = "String",required = true,value = "id")
    @Id
    private String id;
    @ApiModelProperty(dataType = "String",required = true,value = "大学名")
    private String name;
    @ApiModelProperty(dataType = "String",required = true,value = "大学英文名")
    private String nameEn;
    @ApiModelProperty(dataType = "String",required = true,value = "省份")
    private String province_name;
    @ApiModelProperty(dataType = "String",required = true,value = "所属地")
    private String belong;
    @ApiModelProperty(dataType = "String",required = true,value = "'双一流'或为空")
    private String dual_class_name;
    @ApiModelProperty(dataType = "int",required = true,value = "1:表示211；2:表示非211")
    private int f211;
    @ApiModelProperty(dataType = "int",required = true,value = "1:表示985；2:表示非985")
    private int f985;
    @ApiModelProperty(dataType = "String",required = true,value = "学校标签：985,211,双一流")
    private String tags;
    @ApiModelProperty(dataType = "String",required = true,value = "'公办'或'民办'或'中外合作办学'或'内地与港澳台地区合作办学'")
    private String nature_name;
    @ApiModelProperty(dataType = "int",required = true,value = "学校school_id")
    private int school_id;
    @ApiModelProperty(dataType = "int",required = true,value = "学校代码")
    private int scode;
    @ApiModelProperty(dataType = "int",required = true,value = "学校类别名")
    private String type_name;
    @ApiModelProperty(dataType = "String",required = true,value = "联系电话")
    private String phone;
    @ApiModelProperty(dataType = "String",required = true,value = "邮箱")
    private String email;
    @ApiModelProperty(dataType = "String",required = true,value = "管理类型")
    private String adminType;
    @ApiModelProperty(dataType = "String",required = true,value = "大学层次")
    private String level;
    @ApiModelProperty(dataType = "String",required = true,value = "喜欢人数")
    private int likes;
    @ApiModelProperty(dataType = "String",required = true,value = "大学简介")
    private String content;
    @ApiModelProperty(dataType = "String",required = true,value = "大学官网")
    private String school_site;

}
