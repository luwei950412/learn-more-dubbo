package com.sxdubbo.learn.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * created by  luwei
 * 2018-01-22 18:50.
 **/
@Entity
@Table(name = "UserVideo")
public class UserVideoPO implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer userId;

    private Integer videoId;

    private Integer courseId;

    private Double progress;//视频学习进度

    private Date createDate;

    private Date modifyDate;

    private Integer finished;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public double getProgress() {
        return progress;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getFinished() {
        return finished;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }
}
