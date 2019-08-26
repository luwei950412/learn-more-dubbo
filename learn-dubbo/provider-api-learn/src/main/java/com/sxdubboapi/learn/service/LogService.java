package com.sxdubboapi.learn.service;

import com.sxdubboapi.learn.domain.Log;

public interface LogService {

    public Log addLog(Log log);

    public void deleteLog(Integer id);

    public Log updateLog(Log log);


}
