package com.li.controller;

import com.li.entity.Users;
import com.li.service.UsersService;
import com.li.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Api(tags = "用户管理",value = "用户登录和注册的功能")
public class UserController {
    @Autowired
    private UsersService usersService;

    @ApiOperation("用户登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string",name = "username",value = "用户登录账号",required = true),
            @ApiImplicitParam(dataType = "string",name = "password",value = "用户登录密码",required = true)
    })
    @PostMapping("/login")
    public ResultVO login(@RequestBody Users user){
        return usersService.checkLogin2(user.getUsername(),user.getPassword() );
    }
//    @ApiOperation("用户注册接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(dataType = "string",name = "username",value = "用户登录账号",required = true),
//            @ApiImplicitParam(dataType = "string",name = "password",value = "用户登录密码",required = true)
//    })
//    @PostMapping("/regist")
//    public ResultVO regist(@RequestParam("username") String name,@RequestParam("password") String pwd){
//        return usersService.userRegist(name,pwd);
//    }

    @ApiOperation("用户注册接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string",name = "username",value = "用户登录账号",required = true),
            @ApiImplicitParam(dataType = "string",name = "password",value = "用户登录密码",required = true)
    })
    @PostMapping("/regist")
    public ResultVO regist(@RequestBody Users user){
        return usersService.userRegist(user.getUsername(),user.getPassword());
    }

    @ApiOperation("用户注册用户名是否存在接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string",name = "username",value = "用户登录账号",required = true),
            @ApiImplicitParam(dataType = "string",name = "password",value = "用户登录密码",required = true)
    })
    @PostMapping("/regist/name")
    public ResultVO regist1(@RequestBody Users user){
        return usersService.userRegist1(user.getUsername());
    }

    @ApiOperation("用户退出接口")
    @GetMapping("/logout")
    public ResultVO logout(){
        return usersService.logout();
    }

//    @ApiOperation("校验token是否过期接口")
//    @GetMapping("/check")
//    public ResultVO userTokencheck(@RequestHeader("token") String token){
//        return new ResultVO(1000,"success",null);
    //    }

    @ApiOperation("根据id查询用户信息接口")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:admin')")
    public ResultVO userSearchById(@PathVariable Integer id){
        return usersService.userSearchById(id);
    }

    @ApiOperation("根据用户名模糊查询用户信息接口")
    @GetMapping("/list/{name}")
    @PreAuthorize("hasAuthority('user:admin')")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string",name = "pageNum",value = "页码",required = false),
            @ApiImplicitParam(dataType = "string",name = "pageSize",value = "一页的大小",required = false)
    })
    public ResultVO userSearchByPage(@RequestParam(required = false,defaultValue ="1") Integer pageNum,
                                     @RequestParam(required = false,defaultValue ="2" ) Integer pageSize,
                                     @PathVariable (required = true) String name){
        return usersService.userSearchByPage(pageNum,pageSize,name);
    }

    @ApiOperation("分页查询全部用户信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string",name = "pageNum",value = "页码",required = false),
            @ApiImplicitParam(dataType = "string",name = "pageSize",value = "一页的大小",required = false)
    })
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('user:admin')")
    public ResultVO userAllByPage(@RequestParam(required = false,defaultValue ="1") Integer pageNum,
                                     @RequestParam(required = false,defaultValue ="10" ) Integer pageSize){
        return usersService.userSearchByPage(pageNum,pageSize);
    }

    //user/1
    @ApiOperation("删除用户接口")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:admin')")
    public ResultVO userDeletById(@PathVariable Integer id){
        return usersService.userDeleteById(id);
    }

    //新增
    @ApiOperation("添加用户信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string",name = "username",value = "用户登录账号",required = true),
            @ApiImplicitParam(dataType = "string",name = "password",value = "用户登录密码",required = true)
    })
    @PostMapping
    @PreAuthorize("hasAuthority('user:admin')")
    public ResultVO userAdd(@RequestBody Users user){
        return usersService.userRegist(user.getUsername(),user.getPassword());
    }

    //更新
    @ApiOperation("更新用户信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string",name = "username",value = "用户登录账号",required = true),
            @ApiImplicitParam(dataType = "string",name = "password",value = "用户登录密码",required = true)
    })
    @PutMapping
    @PreAuthorize("hasAuthority('user:admin')")
    public ResultVO update(@RequestBody Users user){
        return usersService.userSave(user);
    }
}
