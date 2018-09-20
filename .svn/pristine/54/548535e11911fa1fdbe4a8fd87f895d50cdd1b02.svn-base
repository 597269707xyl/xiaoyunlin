package com.zdtech.platform.framework.entity;

import javax.persistence.*;

/**
 * Created by htma on 2016/5/12.
 */
@Entity
@Table(name = "sys_adapter_conf")
public class SysAdapterConf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "adapter_id")
    private Long adapterId;

    @Column(name = "param_key")
    private String paramKey;

    @Column(name = "param_key_name")
    private String paramKeyName;

    @Column(name = "param_value")
    private String paramValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdapterId() {
        return adapterId;
    }

    public void setAdapterId(Long adapterId) {
        this.adapterId = adapterId;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamKeyName() {
        return paramKeyName;
    }

    public void setParamKeyName(String paramKeyName) {
        this.paramKeyName = paramKeyName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
