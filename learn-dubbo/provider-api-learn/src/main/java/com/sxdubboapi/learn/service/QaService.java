package com.sxdubboapi.learn.service;

import com.sxdubboapi.learn.domain.Qa;
import com.sxdubboapi.learn.domain.User;
import com.sxdubboapi.learn.domain.Video;

import java.util.List;

/**
 * Created by fxb on 18-3-8.
 */
public interface QaService {
    public List<Qa> findByVideo(Video video);
    public Qa findById(Integer id);
    public Qa saveQa(Qa qa);
    public List<Qa> findAllQa();
    public List<Qa> findByUser(User user);
    public void deleteQa(Integer id);
    public List<Qa> findByLabel(String label);
}
