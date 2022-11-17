package com.li.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.*;
import java.util.Date;


@Data
@Table(name = "users")
@Entity
@EntityListeners(AuditingEntityListener.class)
@ApiModel(value = "User对象",description = "用户信息")
public class Users {
    @ApiModelProperty(dataType = "int",required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(dataType = "String",required = true,value = "用户名")
    private String username;

    @ApiModelProperty(dataType = "String",required = true,value = "用户密码")
    private String password;

    private String province;

    private int expect;

    private String gender;

    private String nationality;

    private String subjects;

    @Column(name = "create_time")
    @CreatedDate
    private Date createTime;

    @Column(name = "update_time")
    @LastModifiedDate
    private Date updateTime;

}
