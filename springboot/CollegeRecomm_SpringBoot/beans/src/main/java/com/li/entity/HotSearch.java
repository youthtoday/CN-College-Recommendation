package com.li.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "hotsearch")
@Entity
public class HotSearch {
    @ApiModelProperty(dataType = "int",required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String hotword;
}
