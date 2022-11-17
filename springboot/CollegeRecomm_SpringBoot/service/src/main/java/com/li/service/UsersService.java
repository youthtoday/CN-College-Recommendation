package com.li.service;

import com.li.entity.Users;
import com.li.vo.ResultVO;
public interface UsersService {
    //用户注册
    public ResultVO userRegist(String name,String pwd);

    //用户密码
    public ResultVO checkLogin(String name,String pwd);

    //用户登录2
    public ResultVO checkLogin2(String name,String pwd);

    ResultVO logout();
    
    ResultVO userSearchById(Integer id);

    ResultVO userSearchByPage(Integer page, Integer pageSize,String name);

    ResultVO userSearchByPage(Integer page, Integer pageSize);

    ResultVO userSave(Users user);

    ResultVO userDeleteById(Integer id);

    ResultVO userRegist1(String username);
}
