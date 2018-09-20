package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;

/**
 * Created by yjli on 2017/9/12.
 */
@Entity
@Table(name = "nxy_usecase_exec_batch")
public class NxyUsecaseExecBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @Column(name = "begin_time")
    private Date beginTime = new Date();

    @Column(name="exec_speed")
    private String execSpeed;

    @Column(name="uc_count")
    private Integer ucCount;

    @Column(name="uc_succ_count")
    private Integer ucSuccCount;

    @Column(name="uc_error_count")
    private Integer ucErrorCount;

    private String rate;

    @Column(name="item_id")
    private Long itemId;

    @Column(name="item_type")
    private String itemType;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @Column(name = "end_time")
    private Date endTime = new Date();

    private String status;

    @Column(name="user_name")
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Integer getUcCount() {
        return ucCount;
    }

    public void setUcCount(Integer ucCount) {
        this.ucCount = ucCount;
    }

    public Integer getUcSuccCount() {
        return ucSuccCount;
    }

    public void setUcSuccCount(Integer ucSuccCount) {
        this.ucSuccCount = ucSuccCount;
    }

    public Integer getUcErrorCount() {
        return ucErrorCount;
    }

    public void setUcErrorCount(Integer ucErrorCount) {
        this.ucErrorCount = ucErrorCount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExecSpeed() {
        return execSpeed;
    }

    public void setExecSpeed(String execSpeed) {
        this.execSpeed = execSpeed;
    }
}
