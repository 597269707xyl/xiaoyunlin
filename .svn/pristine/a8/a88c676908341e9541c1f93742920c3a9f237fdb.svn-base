package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;

import javax.persistence.*;
import java.util.List;

/**
 * SimSysInsMessageField
 *
 * @author panli
 * @date 2016/5/17
 */
@Entity
@Table(name="sim_sysins_message_field")
@EntityListeners(value = EntityCodeListener.class)
public class SimSysInsMessageField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="sysins_message_id")
    @JsonIgnore
    private SimSysInsMessage simSysInsMessage;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "message_field_id")
    private MsgField msgField;


    @Column(name="field_type")
    @FieldEnum(code="MESSAGEFIELDTYPE")
    private String fieldType;
    @Transient
    private String fieldTypeDis;

    @Column(name="mo_flag")
    private Boolean moFlag;

    @Column(name="fix_flag")
    private Boolean fixFlag;

    @Column(name="default_value")
    private String defaultValue;

    @Column(name="sign_flag")
    private Boolean signFlag;

    @Column(name="encrypt_flag")
    private Boolean encryptFlag;

    @Column(name="seq_no")
    private Integer seqNo;

    @Column(name="resp_value_type")
    @FieldEnum(code="FIELDSETVALUEMETHOD")
    private String respValueType;
    @Transient
    private String respValueTypeDis;

    @Column(name="resp_value")
    private String respValue;

    @Column(name="resp_parameter")
    private String respParameter;

    @OneToMany(mappedBy = "simMessageField",fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnore
    private List<SimSysinsMsgFieldValue> simMsgFieldValues;

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
    private List<SimSysInsMsgFieldCode> msgFieldCodes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SimSysInsMessage getSimSysInsMessage() {
        return simSysInsMessage;
    }

    public void setSimSysInsMessage(SimSysInsMessage simSysInsMessage) {
        this.simSysInsMessage = simSysInsMessage;
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

    public Boolean getMoFlag() {
        return moFlag;
    }

    public void setMoFlag(Boolean moFlag) {
        this.moFlag = moFlag;
    }

    public Boolean getFixFlag() {
        return fixFlag;
    }

    public void setFixFlag(Boolean fixFlag) {
        this.fixFlag = fixFlag;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getSignFlag() {
        return signFlag;
    }

    public void setSignFlag(Boolean signFlag) {
        this.signFlag = signFlag;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getRespValueType() {
        return respValueType;
    }

    public void setRespValueType(String respValueType) {
        this.respValueType = respValueType;
    }

    public String getRespValue() {
        return respValue;
    }

    public void setRespValue(String respValue) {
        this.respValue = respValue;
    }

    public String getRespParameter() {
        return respParameter;
    }

    public void setRespParameter(String respParameter) {
        this.respParameter = respParameter;
    }

    public String getFieldTypeDis() {
        return fieldTypeDis;
    }

    public void setFieldTypeDis(String fieldTypeDis) {
        this.fieldTypeDis = fieldTypeDis;
    }

    public String getRespValueTypeDis() {
        return respValueTypeDis;
    }

    public void setRespValueTypeDis(String respValueTypeDis) {
        this.respValueTypeDis = respValueTypeDis;
    }

    public List<SimSysinsMsgFieldValue> getSimMsgFieldValues() {
        return simMsgFieldValues;
    }

    public void setSimMsgFieldValues(List<SimSysinsMsgFieldValue> simMsgFieldValues) {
        this.simMsgFieldValues = simMsgFieldValues;
    }

    public String getPadFlag() {
        return padFlag;
    }

    public void setPadFlag(String padFlag) {
        this.padFlag = padFlag;
    }

    public String getPadChar() {
        return padChar;
    }

    public void setPadChar(String padChar) {
        this.padChar = padChar;
    }

    public String getFieldValueType() {
        return fieldValueType;
    }

    public void setFieldValueType(String fieldValueType) {
        this.fieldValueType = fieldValueType;
    }

    public String getFieldValueTypeParam() {
        return fieldValueTypeParam;
    }

    public void setFieldValueTypeParam(String fieldValueTypeParam) {
        this.fieldValueTypeParam = fieldValueTypeParam;
    }

    public List<SimSysInsMsgFieldCode> getMsgFieldCodes() {
        return msgFieldCodes;
    }

    public void setMsgFieldCodes(List<SimSysInsMsgFieldCode> msgFieldCodes) {
        this.msgFieldCodes = msgFieldCodes;
    }

    public Boolean getEncryptFlag() {
        return encryptFlag;
    }

    public void setEncryptFlag(Boolean encryptFlag) {
        this.encryptFlag = encryptFlag;
    }
}
