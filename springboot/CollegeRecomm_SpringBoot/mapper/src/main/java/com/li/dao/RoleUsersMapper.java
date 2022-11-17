package com.li.dao;

import com.li.entity.RoleUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public  interface RoleUsersMapper extends JpaRepository<RoleUsers,Integer> {

}
