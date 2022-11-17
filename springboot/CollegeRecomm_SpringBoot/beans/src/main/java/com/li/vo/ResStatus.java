package com.li.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ResStatus对象",description = "状态码")
public class ResStatus {
    @ApiModelProperty(value = "请求成功1000")
    public static final int OK = 1000;//请求成功1000
    @ApiModelProperty(value = "请求失败1002")
    public static final int NO = 1002;//请求失败1001
    @ApiModelProperty(value = "用户名已存在1001")
    public static final int EXIST = 1001;//用户名已存在 or 用户名不存在


    public static final int LOGIN_SUCCESS= 2000;//认证成功
    public static final int LOGIN_FAIL_NOT=20001;//用户未登陆
    public static final int LOGIN_FAIL_OVERDUE=20002;//用户登录失效
}
