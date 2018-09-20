package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.persistence.QueryDef;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.List;

/**
 * Created by ll007 on 2016/5/9.
 */
@Entity
@Table(name="sim_message_field")
@QueryDef(queryTag = "simMessageField", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "simMessageFieldDao")
@EntityListeners(value = EntityCodeListener.class)
public class SimMessageField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="message_id")
    @JsonIgnore
    private SimMessage simMessage;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "message_field_id")
    private MsgField msgField;
    @OneToMany(mappedBy = "simMessageField")
    @JsonIgnore
    private List<SimMsgFieldValue> simMsgFieldValues;

    @Column(name="field_type")
    @FieldEnum(code="MESSAGEFIELDTYPE")
    private String fieldType;
    @Transient
    private String fieldTypeDis;
    @Column(name="mo_flag")
    private Boolean mflag;

    @Column(name="fix_flag")
    private Boolean fflag;

    @Column(name="default_value")
    private String dvalue;

    @Column(name="sign_flag")
    private Boolean sflag;

    private Integer seq_no;

    @Column(name = "pad_flag")
    private String padFlag;

    @Column(name = "pad_char")
    private String padChar;

    @Column(name = "field_value_type")
    private String fieldValueType;

    @Column(name = "field_value_type_param")
    private String fieldValueTypeParam;

    @OneToMany(mappedBy = "msgField", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnore
    private List<SimMsgFieldCode> msgFieldCodes;

    public List<SimMsgFieldValue> getSimMsgFieldValues() {
        return simMsgFieldValues;
    }

    public void setSimMsgFieldValues(List<SimMsgFieldValue> simMsgFieldValues) {
        this.simMsgFieldValues = simMsgFieldValues;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SimMessage getSimMessage() {
        return simMessage;
    }

    public void setSimMessage(SimMessage simMessage) {
        this.simMessage = simMessage;
    }

    public MsgField getMsgField() {
        return msgField;
    }

    public void setMsgField(MsgField msgField) {
        this.msgField = msgField;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Boolean getMflag() {
        return mflag;
    }

    public void setMflag(Boolean mflag) {
        this.mflag = mflag;
    }

    public Boolean getFflag() {
        return fflag;
    }

    public void setFflag(Boolean fflag) {
        this.fflag = fflag;
    }

    public Boolean getSflag() {
        return sflag;
    }

    public void setSflag(Boolean sflag) {
        this.sflag = sflag;
    }

    public String getDvalue() {
        return dvalue;
    }

    public void setDvalue(String dvalue) {
        this.dvalue = dvalue;
    }


    public Integer getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(Integer seq_no) {
        this.seq_no = seq_no;
    }
    @Column(name="value_type")
    @FieldEnum(code = "FIELDSETVALUEMETHOD")
    private String valueType;
    @Transient
    private String valueTypeDis;
    private String value;

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueTypeDis() {
        return valueTypeDis;
    }

    public void setValueTypeDis(String valueTypeDis) {
        this.valueTypeDis = valueTypeDis;
    }

    public String getFieldTypeDis() {
        return fieldTypeDis;
    }

    public void setFieldTypeDis(String fieldTypeDis) {
        this.fieldTypeDis = fieldTypeDis;
    }

    public String getPadFlag() {
        return padFlag;
    }

    public void setPadFlag(String padFlag) {
        this.padFlag = padFlag;
    }

    public String getFieldValueType() {
        return fieldValueType;
    }

    public void setFieldValueType(String fieldValueType) {
        this.fieldValueType = fieldValueType;
    }

    public String getPadChar() {
        return padChar;
    }

    public void setPadChar(String padChar) {
        this.padChar = padChar;
    }

    public String getFieldValueTypeParam() {
        return fieldValueTypeParam;
    }

    public void setFieldValueTypeParam(String fieldValueTypeParam) {
        this.fieldValueTypeParam = fieldValueTypeParam;
    }

    public List<SimMsgFieldCode> getMsgFieldCodes() {
        return msgFieldCodes;
    }

    public void setMsgFieldCodes(List<SimMsgFieldCode> msgFieldCodes) {
        this.msgFieldCodes = msgFieldCodes;
    }
}
