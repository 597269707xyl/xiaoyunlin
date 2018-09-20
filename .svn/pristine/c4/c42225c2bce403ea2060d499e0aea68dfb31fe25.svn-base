package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.persistence.QueryDef;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.List;

/**
 * Created by leepan on 2016/5/4.
 */
@Entity
@Table(name = "msg_field")
@QueryDef(queryTag = "msgField", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.ASC,
        daoName = "msgFieldDao")
@EntityListeners(value = EntityCodeListener.class)
public class MsgField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "field_id")
    private String fieldId;

    @Column(name = "name_zh")
    private String nameZh;

    @Column(name = "name_en")
    private String nameEn;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "data_type_id")
    private MsgDataType dataType;

    @Column(name = "start_pos")
    private Integer startPos;

    @Column(name = "length")
    private Integer length;

    @Column(name = "mo_flag")
    private boolean moFlag;

    @Column(name = "fix_flag")
    private boolean fixFlag;

    @Column(name = "length_length")
    private Integer lengthLength;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "field_type")
    @FieldEnum(code = "FIELDTYPE")
    private String fieldType;

    @Transient
    private String fieldTypeDis;

    @Column(name = "msg_type")
    @FieldEnum(code = "MSGTYPE")
    private String msgType;
    @Transient
    private String msgTypeDis;

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
    private List<MsgFieldCode> msgFieldCodes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public MsgDataType getDataType() {
        return dataType;
    }

    public void setDataType(MsgDataType dataType) {
        this.dataType = dataType;
    }

    public Integer getStartPos() {
        return startPos;
    }

    public void setStartPos(Integer startPos) {
        this.startPos = startPos;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getLengthLength() {
        return lengthLength;
    }

    public void setLengthLength(Integer lengthLength) {
        this.lengthLength = lengthLength;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldTypeDis() {
        return fieldTypeDis;
    }

    public void setFieldTypeDis(String fieldTypeDis) {
        this.fieldTypeDis = fieldTypeDis;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgTypeDis() {
        return msgTypeDis;
    }

    public void setMsgTypeDis(String msgTypeDis) {
        this.msgTypeDis = msgTypeDis;
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

    public List<MsgFieldCode> getMsgFieldCodes() {
        return msgFieldCodes;
    }

    public void setMsgFieldCodes(List<MsgFieldCode> msgFieldCodes) {
        this.msgFieldCodes = msgFieldCodes;
    }
}
