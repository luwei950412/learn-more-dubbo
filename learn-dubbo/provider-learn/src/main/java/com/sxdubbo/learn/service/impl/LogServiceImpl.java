package com.sxdubbo.learn.service.impl;
import com.sxdubbo.learn.domain.LogPO;
import com.sxdubbo.learn.repository.LogRepository;
import com.sxdubboapi.learn.domain.Log;
import com.sxdubboapi.learn.service.LogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class LogServiceImpl implements LogService{

    @Autowired
    public LogRepository logRepository;

    @Override
    public Log addLog(Log log) {
        LogPO logPO = new LogPO();
        BeanUtils.copyProperties(log,logPO);
        LogPO logPO1 = logRepository.save(logPO);
        Log log1 = new Log();
        BeanUtils.copyProperties(logPO1,log1);
        System.out.println(log1);
        return log1;
    }

    @Override
    public void deleteLog(Integer integer) {

    }

    @Override
    public Log updateLog(Log log) {
        return null;
    }

}
