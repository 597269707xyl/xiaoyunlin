package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by leepan on 2016/5/5.
 */
@Entity
@Table(name = "msg_field_set_comp")
public class MsgFieldSetComp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="msg_field_set_id")
    @JsonIgnore
    private MsgFieldSet fieldSet;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "msg_field_id")
    private MsgField field;

    @Column(name = "mo_flag")
    private boolean moFlag;

    @Column(name = "fix_flag")
    private boolean fixFlag;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "seq_no")
    private Integer seqNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MsgFieldSet getFieldSet() {
        return fieldSet;
    }

    public void setFieldSet(MsgFieldSet fieldSet) {
        this.fieldSet = fieldSet;
    }

    public MsgField getField() {
        return field;
    }

    public void setField(MsgField field) {
        this.field = field;
    }

    public boolean isMoFlag() {
        return moFlag;
    }

    public void setMoFlag(boolean moFlag) {
        this.moFlag = moFlag;
    }

    public boolean isFixFlag() {
        return fixFlag;
    }

    public void setFixFlag(boolean fixFlag) {
        this.fixFlag = fixFlag;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
}
