package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by leepan on 2016/5/10.
 */
@Entity
@Table(name = "msg_format_comp_field")
public class MsgFormatCompField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="format_comp_id")
    @JsonIgnore
    private MsgFormatComp formatComp;
    private String property;
    @Column(name = "seq_no")
    private Integer seqNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MsgFormatComp getFormatComp() {
        return formatComp;
    }

    public void setFormatComp(MsgFormatComp formatComp) {
        this.formatComp = formatComp;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
}
