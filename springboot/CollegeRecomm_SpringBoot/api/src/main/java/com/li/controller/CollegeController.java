package com.li.controller;

import com.li.aop.LogAnno;
import com.li.entity.*;
import com.li.service.*;
import com.li.vo.ResStatus;
import com.li.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/colleges")
@Api(tags = "大学管理",value = "大学功能")
public class CollegeController {
    @Autowired
    CollegesService collegesService;
    @Autowired
    private ISparkSubmitService iSparkSubmitService;
    @Autowired
    RecommCsService recommCsService;
    @Autowired
    AllCollegeMajorService allCollegeMajorService;
    @Resource
    private HBaseService hBaseService;
    @Autowired
    private RecommSMService recommSMService;
    @Autowired
    private MajorsService majorsService;

    @ApiOperation("查询所有大学接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "Integer",name = "pageNum",value = "页码",required = false),
            @ApiImplicitParam(dataType = "Integer",name = "pageSize",value = "每页记录数",required = false),
            @ApiImplicitParam(dataType = "String",name = "province",value = "省份",required = false),
            @ApiImplicitParam(dataType = "String",name = "type",value = "类型",required = false)
    })
    @GetMapping("/list")
    public ResultVO CollegesList(@RequestParam(required = false,defaultValue ="1") Integer pageNum,
                                 @RequestParam(required = false,defaultValue ="10" ) Integer pageSize,
                                 @RequestParam(required = false,defaultValue = "全部省份") String province,
                                 @RequestParam(required = false,defaultValue = "全部类型") String type){
        if (pageNum <= 0) pageNum = 1;
        if (pageSize <= 0) pageSize = 10;
        Page<Colleges> colleges;
        if ("全部省份".equals(province)){
            if ("全部类型".equals(type))
                colleges = collegesService.findAll(pageNum, pageSize);
            else{
                colleges = collegesService.findByType(type,pageNum,pageSize);
            }
        }else {
            if ("全部类型".equals(type))
                colleges = collegesService.findByProvince(province,pageNum,pageSize);
            else {
                colleges = collegesService.findByProvinceAndName(province,type,pageNum,pageSize);
            }
        }
        return new ResultVO(ResStatus.OK,"查询成功",colleges);
    }

    @ApiOperation("根据名字查询大学接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "Integer",name = "pageNum",value = "页码",required = false),
            @ApiImplicitParam(dataType = "Integer",name = "pageSize",value = "每页记录数",required = false),
            @ApiImplicitParam(dataType = "String",name = "province",value = "省份",required = false),
            @ApiImplicitParam(dataType = "String",name = "type",value = "类型",required = false)
    })
    @GetMapping("/list/{name}")
    public ResultVO CollegesList(@RequestParam(required = false,defaultValue ="1") Integer pageNum,
                                 @RequestParam(required = false,defaultValue ="10" ) Integer pageSize,
                                 @RequestParam(required = false,defaultValue = "全部省份") String province,
                                 @RequestParam(required = false,defaultValue = "全部类型") String type,
                                 @PathVariable String name){
        if (pageNum <= 0) pageNum = 1;
        if (pageSize <= 0) pageSize = 10;
        Page<Colleges> colleges;
        if ("全部省份".equals(province)){
            if ("全部类型".equals(type))
                colleges = collegesService.findByName(name,pageNum, pageSize);
            else{
                colleges = collegesService.findByNameAndType(name,type,pageNum,pageSize);
            }
        }else {
            if ("全部类型".equals(type))
                colleges = collegesService.findByNameAndProvince(name,province,pageNum,pageSize);
            else {
                colleges = collegesService.findByNameAndProvinceAndName(name,province,type,pageNum,pageSize);
            }
        }
        return new ResultVO(ResStatus.OK,"查询成功",colleges);
    }

    @LogAnno(operateType = "大学详情信息")
    @ApiOperation("根据id查询大学详细信息接口")
    @GetMapping("/{id}")
    public ResultVO CollegesList(@PathVariable int id){
        Map<String,Object> map = new HashMap<>();
        Colleges colleges = collegesService.findOne(id);
        map.put("details",colleges);
        RecommCs recommCs = recommCsService.findOne(colleges.getScode());
        Map<String,Object> recommdata = new HashMap<>();
        recommdata.put("school_id",id);
        recommdata.put("1",collegesService.findByScode(recommCs.getSim1()));
        recommdata.put("2",collegesService.findByScode(recommCs.getSim2()));
        recommdata.put("3",collegesService.findByScode(recommCs.getSim3()));
        recommdata.put("4",collegesService.findByScode(recommCs.getSim4()));
        recommdata.put("5",collegesService.findByScode(recommCs.getSim5()));
        recommdata.put("6",collegesService.findByScode(recommCs.getSim6()));
        recommdata.put("7",collegesService.findByScode(recommCs.getSim7()));
        recommdata.put("8",collegesService.findByScode(recommCs.getSim8()));
        recommdata.put("9",collegesService.findByScode(recommCs.getSim9()));
        recommdata.put("10",collegesService.findByScode(recommCs.getSim10()));

        map.put("recommcs",recommdata);
        Page<AllCollegeMajor> majors = allCollegeMajorService.findAllMajorsBySid(id,1,150);
        map.put("majors",majors.getContent());
        Map<String, String> hrefs = hBaseService.queryHrefByName(colleges.getName());
        map.put("hrefs",hrefs);
        return new ResultVO(ResStatus.OK,"查询成功",map);
//        String mainClass = "com.hrong.job.etl.DbTableEtl";
//        String jarPath  = "hdfs://Master:9000/api-spark/jars/spark-job.jar";
//        String master = "yarn";
//        String deployMode = "cluster";
//        String driverMemory = "1g";
//        String executorMemory = "1g";
//        String executorCores = "1";
//        Map<String,String> otherConfParams = new HashMap<>();
//        RecommdVO vo = new RecommdVO(mainClass,jarPath,master,deployMode,driverMemory,executorMemory,executorCores,otherConfParams);
//        try {
//            String list = iSparkSubmitService.submitApplication1(vo.getSparkApplicationParam(),
//                    vo.getSchool_id());
//            map.put("recommLists",list);
//            return new ResultVO(ResStatus.OK,"查询成功",map);
//        }catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//            log.error("执行出错：{}", e.getMessage());
//            return new ResultVO(ResStatus.NO, e.getMessage(),null);
//        }
    }

    @ApiOperation("根据学校名和专业名查询专业详细信息")
    @GetMapping("/details")
    public ResultVO getinfobyname(@RequestParam int scode,
                                  @RequestParam String spcode){
        Map<String,Object> map = new HashMap<>();
        RecommSM recommSM = recommSMService.findByScodeAndMcode(scode, spcode);
        String[] recommends = recommSM.getRecommend().split("-");
        List<Map<String,Object>> recommdatas = new ArrayList<>();
        for(String recommend:recommends){
            String[] sm = recommend.split(":");
            int scode1 = Integer.parseInt(sm[0]);
            Colleges colleges = collegesService.findByScode(scode1);
            if (!Objects.isNull(colleges) && (sm.length == 2) && (recommdatas.size() < 10)){
                Map<String,Object> recommdata = new HashMap<>();
                recommdata.put("college",colleges);
                String[] spcodes= sm[1].split(",");
                System.out.println("spcodes:"+spcodes.toString());
                List<Majors> majors = new ArrayList<>();
                for (String spcode1:spcodes){
                    Majors recomm = majorsService.findBySpcode(spcode1);
                    if (!Objects.isNull(recomm)) majors.add(recomm);
                }
                recommdata.put("majors",majors);
                recommdatas.add(recommdata);
            }
        }
        //获取mname和sname
        Colleges colleges = collegesService.findByScode(scode);
        Majors majors = majorsService.findBySpcode(spcode);
        String sname = colleges.getName();
        String mname = majors.getName();
        map.put("mname",mname);
        //获取专业信息
        map.put("sname",sname);
        String info = hBaseService.queryDataByName(sname, mname);
        map.put("minfo",info);
        map.put("recommcs",recommdatas);
        return new ResultVO(ResStatus.OK,"查询成功",map);
    }

}
