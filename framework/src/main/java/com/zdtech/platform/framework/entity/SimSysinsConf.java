package com.zdtech.platform.framework.entity;

import javax.persistence.*;

/**
 * Created by yjli on 2017/3/17.
 */
@Entity
@Table(name = "sim_sysins_conf")
public class SimSysinsConf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sim_sysins_id")
    private Long simSysinsId;

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

    public Long getSimSysinsId() {
        return simSysinsId;
    }

    public void setSimSysinsId(Long simSysinsId) {
        this.simSysinsId = simSysinsId;
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
