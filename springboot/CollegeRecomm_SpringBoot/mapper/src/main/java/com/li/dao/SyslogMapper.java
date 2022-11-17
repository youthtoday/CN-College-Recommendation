package com.li.dao;

import com.li.entity.Syslog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface SyslogMapper  extends JpaRepository<Syslog,Integer> {


    public List<Syslog> findByUidOrderByCreateTimeDesc(int uid);

    @Modifying
    @Transactional
    @Query(value = "insert into syslog(create_time, sid, uid,operation,params) values (?1,?2,?3,?4,?5)",nativeQuery = true)
    int saveLog(Date createTime, int sid, int uid, String operation,String params);
}
