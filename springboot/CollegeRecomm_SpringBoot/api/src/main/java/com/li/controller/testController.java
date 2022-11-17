package com.li.controller;

import com.li.vo.ResStatus;
import com.li.vo.ResultVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {

    @GetMapping("/test/hello")
    @PreAuthorize("hasAuthority('user:list')")
    public String hello(){
        return "hello security";
    }

    @RequestMapping("/testCors")
    public ResultVO testCors(){
        return new ResultVO(ResStatus.NO,"testCors",null);
    }
}
