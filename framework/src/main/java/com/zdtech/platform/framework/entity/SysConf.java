package com.zdtech.platform.framework.entity;

import javax.persistence.*;

/**
 * Created by htma on 2016/5/17.
 */
@Entity
@Table(name = "sys_conf")
public class SysConf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    @Column(name = "[key]")
    private String key;
    private String value;

    @Column(name = "seq_no")
    private Integer seqNo;
    @Column(name = "key_val")
    private String keyVal;
    public SysConf() {
    }

    public SysConf(String category, String key, String value, Integer seqNo) {
        this.category = category;
        this.key = key;
        this.value = value;
        this.seqNo = seqNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getKeyVal() {
        return keyVal;
    }

    public void setKeyVal(String keyVal) {
        this.keyVal = keyVal;
    }
}
