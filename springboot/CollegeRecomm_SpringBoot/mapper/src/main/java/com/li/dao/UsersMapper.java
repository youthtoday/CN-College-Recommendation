package com.li.dao;

import com.li.entity.Users;
import org.apache.catalina.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface UsersMapper extends JpaRepository<Users,Integer>{
    //用户注册
//    @Query(value = "insert into users values(?1,?2)",nativeQuery = true)
//    public int insertUser(Users users);

    //根据名字密码查询用户信息
    @Query(value = "select *  from users  where username = ?1",nativeQuery = true)
    public Users queryUserByName(String name);


    @Query(value = "select * from users where username like %?1%",nativeQuery = true)
    Page<Users> findNameLike(String username, Pageable pageable);


}
