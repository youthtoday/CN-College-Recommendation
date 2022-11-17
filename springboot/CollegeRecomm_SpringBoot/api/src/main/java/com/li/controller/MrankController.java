package com.li.controller;

import com.li.entity.Mrank;
import com.li.entity.Rk2021;
import com.li.entity.Xyhrank;
import com.li.service.MrankService;
import com.li.service.Rk2021Service;
import com.li.service.XyhrankService;
import com.li.vo.ResStatus;
import com.li.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "排名",value = "排名功能")
public class MrankController {
    @Autowired
    private MrankService mrankService;

    @Autowired
    private Rk2021Service rk2021Service;

    @Autowired
    private XyhrankService xyhrankService;

    @ApiOperation("大学按专业分类的排名接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "Integer",name = "pageNum",value = "页码",required = false),
            @ApiImplicitParam(dataType = "Integer",name = "pageSize",value = "每页记录数",required = false),
            @ApiImplicitParam(dataType = "Integer",name = "xid",value = "专业id",required = true)
    })
    @GetMapping("/mrank")
    public ResultVO CollegeRankByMajor(@RequestParam(required = false,defaultValue ="1") Integer pageNum,
                                       @RequestParam(required = false,defaultValue ="10" ) Integer pageSize,
                                       @RequestParam Integer xid){
        if (pageNum <= 0) pageNum = 1;
        if (pageSize <= 0) pageSize = 10;
        Page<Mrank> mranks = mrankService.findCollegesByXid(xid,pageNum, pageSize);
        return new ResultVO(ResStatus.OK,"查询成功",mranks);
    }


    @ApiOperation("大学软科排名接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "Integer",name = "pageNum",value = "页码",required = false),
            @ApiImplicitParam(dataType = "Integer",name = "pageSize",value = "每页记录数",required = false),
            @ApiImplicitParam(dataType = "String",name = "province",value = "省份",required = false),
            @ApiImplicitParam(dataType = "String",name = "type",value = "类型",required = false)
    })
    @GetMapping("/rkrank")
    public ResultVO RkRank(@RequestParam(required = false,defaultValue ="1") Integer pageNum,
                           @RequestParam(required = false,defaultValue ="10" ) Integer pageSize,
                           @RequestParam(required = false,defaultValue = "全部省份") String province,
                           @RequestParam(required = false,defaultValue = "全部类型") String type){
        if (pageNum <= 0) pageNum = 1;
        if (pageSize <= 0) pageSize = 10;
        Page<Rk2021> rk2021s;
        if ("全部省份".equals(province)){
            if ("全部类型".equals(type))
                rk2021s = rk2021Service.findAll(pageNum, pageSize);
            else{
                rk2021s = rk2021Service.findByType(type,pageNum,pageSize);
            }
        }else {
            if ("全部类型".equals(type))
                rk2021s = rk2021Service.findByProvince(province,pageNum,pageSize);
            else {
                rk2021s = rk2021Service.findByProvinceAndName(province,type,pageNum,pageSize);
            }
        }
        return new ResultVO(ResStatus.OK,"查询成功",rk2021s);
    }

    @ApiOperation("校友会排名接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "Integer",name = "pageNum",value = "页码",required = false),
            @ApiImplicitParam(dataType = "Integer",name = "pageSize",value = "每页记录数",required = false),
            @ApiImplicitParam(dataType = "String",name = "province",value = "省份",required = false),
            @ApiImplicitParam(dataType = "String",name = "type",value = "类型",required = false)
    })
    @GetMapping("/xyhrank")
    public ResultVO XyhRank(@RequestParam(required = false,defaultValue ="1") Integer pageNum,
                            @RequestParam(required = false,defaultValue ="10" ) Integer pageSize,
                            @RequestParam(required = false,defaultValue = "全部省份") String province,
                            @RequestParam(required = false,defaultValue = "全部类型") String type){
        if (pageNum <= 0) pageNum = 1;
        if (pageSize <= 0) pageSize = 10;
        Page<Xyhrank> xyhranks;
        if ("全部省份".equals(province)){
            if ("全部类型".equals(type))
                xyhranks = xyhrankService.findAll(pageNum, pageSize);
            else{
                xyhranks = xyhrankService.findByType(type,pageNum,pageSize);
            }
        }else {
            if ("全部类型".equals(type))
                xyhranks = xyhrankService.findByProvince(province,pageNum,pageSize);
            else {
                xyhranks = xyhrankService.findByProvinceAndName(province,type,pageNum,pageSize);
            }
        }
        return new ResultVO(ResStatus.OK,"查询成功",xyhranks);
    }


}
