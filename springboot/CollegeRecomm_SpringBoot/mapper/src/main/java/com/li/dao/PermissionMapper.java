package com.li.dao;

import com.li.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper extends JpaRepository<Permission,Integer> {
    //查询用户对应的权限
    @Query(value = " SELECT DISTINCT p.permission_code  FROM users AS u " +
            "        LEFT JOIN role_users AS ur ON u.id = ur.uid  LEFT JOIN role AS r ON r.id = ur.rid\n" +
            "        LEFT JOIN role_permission AS rp ON r.id = rp.role_id\n" +
            "        LEFT JOIN permission AS p ON p.id = rp.permission_id\n" +
            "        WHERE u.id = ?1",nativeQuery = true)
    List<String> selectPermsByUser(Integer uid);
}
