package com.zdtech.platform.framework.entity;

import javax.persistence.*;

/**
 * Created by lcheng on 2015/5/6.
 */
@Entity
@Table(name = "sys_codes")
public class SysCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String category;

    @Column(name = "[key]",length = 50)
    private String key;
    @Column(length = 60)
    private String value;

    @Column(name = "seq_no")
    private Integer seqNo;

    public SysCode() {
    }

    public SysCode(String category, String key, String value, Integer seqNo) {
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
}
