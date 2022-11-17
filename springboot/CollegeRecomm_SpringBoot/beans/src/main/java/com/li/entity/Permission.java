package com.li.entity;

import lombok.Data;

import javax.persistence.*;


@Data
@Table(name = "permission")
@Entity
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String permissionName;//访问名称
    private String permissionCode;//访问url
}
