package com.li.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "major")
@Entity
public class Major {
    @ApiModelProperty(dataType = "int",required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer mid;

    private String name;

    private Integer cid;

}
