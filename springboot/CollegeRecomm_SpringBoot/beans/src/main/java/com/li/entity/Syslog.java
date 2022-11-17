package com.li.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "syslog")
@Entity
@EntityListeners(AuditingEntityListener.class)
@ApiModel(value = "syslog对象",description = "日志信息")
public class Syslog {
    @ApiModelProperty(dataType = "int",required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int uid;

    private int sid;

    private String operation;//方法

    private String params;

    @Column(name = "create_time")
    @CreatedDate
    private Date createTime;
}
