package com.li.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ResultVO对象",description = "封装接口返回给前端的数据")
public class ResultVO {
    //响应给前端的状态码
    @ApiModelProperty(value = "响应状态码",dataType = "int")
    private int code;

    //提示信息
    @ApiModelProperty(value = "响应提示信息",dataType = "String")
    private String msg;

    //响应数据
    @ApiModelProperty(value = "响应数据",dataType = "Object")
    private Object data;
}
