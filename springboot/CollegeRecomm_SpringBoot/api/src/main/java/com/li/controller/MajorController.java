package com.li.controller;

import com.li.entity.*;
import com.li.service.*;
import com.li.vo.ResStatus;
import com.li.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/majors")
@Api(tags = "专业管理",value = "专业查询功能")
public class MajorController {

    @Autowired
    private MajorsService majorsService;
    @Autowired
    AllCollegeMajorService allCollegeMajorService;
    @Autowired
    CollegesService collegesService;
    @Autowired
    RecommMsService recommMsService;

    @ApiOperation("查询所有专业接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "Integer",name = "pageNum",value = "页码",required = false),
            @ApiImplicitParam(dataType = "Integer",name = "pageSize",value = "每页记录数",required = false)
    })
    @GetMapping("/list")
    public ResultVO MajorsList(@RequestParam(required = false,defaultValue ="1") Integer pageNum,
                                 @RequestParam(required = false,defaultValue ="10" ) Integer pageSize){
        if (pageNum <= 0) pageNum = 1;
        if (pageSize <= 0) pageSize = 10;
        Page<Majors> majors = majorsService.findAll(pageNum, pageSize);
        return new ResultVO(ResStatus.OK,"查询成功",majors.getContent());
    }

//    @ApiOperation("专业详细信息")
//    @GetMapping("/detail")
//    public ResultVO MajorsDetail(@RequestParam String mname){
//        List<Majors> majors = majorsService.findByName(mname);
//        Majors major = majors.get(0);
//        return new ResultVO(ResStatus.OK,"查询成功",major);
//    }


    @ApiOperation("根据名字查询专业接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "Integer",name = "pageNum",value = "页码",required = false),
            @ApiImplicitParam(dataType = "Integer",name = "pageSize",value = "每页记录数",required = false)
    })
    @GetMapping("/list/{name}")
    public ResultVO MajorsList(@PathVariable(required = true) String name){
        List<Majors> majors = majorsService.findByName(name);
        return new ResultVO(ResStatus.OK,"查询成功",majors);
    }

    @ApiOperation("根据名字查询学校排名接口")
    @GetMapping("/{name}")
    public ResultVO CollegesList(@RequestParam(required = false,defaultValue ="1") Integer pageNum,
        @RequestParam(required = false,defaultValue ="10" ) Integer pageSize,
        @PathVariable(required = true) String name){
            if (pageNum <= 0) pageNum = 1;
        if (pageSize <= 0) pageSize = 10;
        Map<String,Object> map = new HashMap<>();
        List<Majors> majors = majorsService.findByName(name);
        Majors major;
        if (null != majors && majors.size() !=0 ){
            major = majors.get(0);
            map.put("major",major);
            Page<AllCollegeMajor> allCollegeMajors = allCollegeMajorService.findAllByMid(major.getSpecial_id(), pageNum, pageSize);
            List<AllCollegeMajor> collegeMajorList = allCollegeMajors.getContent();
            int toTalPages = allCollegeMajors.getTotalPages();
            long toTalElements = allCollegeMajors.getTotalElements();
            List<Colleges> colleges = new ArrayList<>();
            Map<String,Object> mapcolleges = new HashMap<>();
            for (AllCollegeMajor collegeMajor:collegeMajorList){
                int sid = collegeMajor.getSchoolid();
                colleges.add(collegesService.findOne(sid));
            }
            mapcolleges.put("content",colleges);
            mapcolleges.put("toTalPages",toTalPages);
            mapcolleges.put("toTalElements",toTalElements);
            map.put("colleges",mapcolleges);
            //专业相似推荐
            List<Majors> recommdata = new ArrayList<>();
            String mcode = major.getSpcode();
            RecommMs recommMs = recommMsService.findByMcode(mcode);
            String[] spcodes = recommMs.getConfidence().split(",");
            for (String spcode:spcodes){
                Majors recomm = majorsService.findBySpcode(spcode);
                recommdata.add(recomm);
            }
            map.put("recommms",recommdata);
            return new ResultVO(ResStatus.OK,"查询成功",map);
        }else {
            return new ResultVO(ResStatus.NO,"查询失败",name);
        }
    }

}
