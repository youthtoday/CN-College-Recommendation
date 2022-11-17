package com.li.service.impl;

import com.li.dao.SyslogMapper;
import com.li.entity.Syslog;
import com.li.service.SyslogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyslogServiceImpl implements SyslogService {
    @Autowired
    SyslogMapper syslogMapper;

    @Override
    public void saveLog(Syslog syslog) {
        int save = syslogMapper.saveLog(syslog.getCreateTime(),syslog.getSid(),syslog.getUid(),syslog.getOperation(),syslog.getParams());
        System.out.println("save:"+save);

    }
}
