package com.li.service.impl;
import com.li.dao.PermissionMapper;
import com.li.dao.UsersMapper;
import com.li.entity.LoginUser;
import com.li.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements  UserDetailsService{
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        Users users = usersMapper.queryUserByName(username);

        if(Objects.isNull(users)){
            throw new UsernameNotFoundException("Not Found");
        }
        //TODO 查询对应的权限信息
        List<String> list = permissionMapper.selectPermsByUser(users.getId());
        System.out.println(list);
        //把数据封装为UserDetails返回
        return new LoginUser(users,list);
    }
}
