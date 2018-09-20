package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;

import javax.persistence.*;

/**
 * Created by leepan on 2016/5/4.
 */
@Entity
@Table(name = "msg_data_type_enum")
@EntityListeners(value = EntityCodeListener.class)
public class MsgDataTypeEnum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="type")
    @FieldEnum(code = "DATATYPE_TYPE")
    private String dataSetType;

    @Transient
    private String dataSetTypeDis;

    private String value1;

    private String spliter;

    private String value2;

    @ManyToOne
    @JoinColumn(name="data_type_id")
    @JsonIgnore
    private MsgDataType dataType;
    @Column(name="generat_rule")
    private String generatRule;

    private String descript;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataSetType() {
        return dataSetType;
    }

    public void setDataSetType(String dataSetType) {
        this.dataSetType = dataSetType;
    }

    public String getDataSetTypeDis() {
        return dataSetTypeDis;
    }

    public void setDataSetTypeDis(String dataSetTypeDis) {
        this.dataSetTypeDis = dataSetTypeDis;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getSpliter() {
        return spliter;
    }

    public void setSpliter(String spliter) {
        this.spliter = spliter;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public MsgDataType getDataType() {
        return dataType;
    }

    public void setDataType(MsgDataType dataType) {
        this.dataType = dataType;
    }

    public String getGeneratRule() {
        return generatRule;
    }

    public void setGeneratRule(String generatRule) {
        this.generatRule = generatRule;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }
}
