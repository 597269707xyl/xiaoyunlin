package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.ui.FieldEnum;

import javax.persistence.*;

/**
 * Created by yjli on 2017/9/6.
 */
@Entity
@Table(name = "nxy_func_usecase_expected")
public class NxyFuncUsecaseExpected {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="field_id")
    @JsonIgnore
    private MsgField msgField;

    @Column(name="field_id_name")
    private String fieldIdName;

    @Column(name="expected_value")
    private String expectedValue;

    @Column(name="usecase_data_id")
    private Long usecaseDataId;
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MsgField getMsgField() {
        return msgField;
    }

    public void setMsgField(MsgField msgField) {
        this.msgField = msgField;
    }

    public String getFieldIdName() {
        return fieldIdName;
    }

    public void setFieldIdName(String fieldIdName) {
        this.fieldIdName = fieldIdName;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public Long getUsecaseDataId() {
        return usecaseDataId;
    }

    public void setUsecaseDataId(Long usecaseDataId) {
        this.usecaseDataId = usecaseDataId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
