package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.persistence.QueryDef;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;
import org.springframework.data.domain.Sort;

import javax.persistence.*;

/**
 * Created by wzx on 2016/8/3.
 */
@Entity
@Table(name="sim_sysins_msg_field_value")
@QueryDef(queryTag = "simSysinsMsgFieldValue", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "simSysinsMsgFieldValueDao")
@EntityListeners(value = EntityCodeListener.class)
public class SimSysinsMsgFieldValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="field_id")
    @JsonIgnore
    private SimSysInsMessageField simMessageField;


    @Column(name="value_type")
    @FieldEnum(code = "DATATYPE_TYPE")
    private String valueType;
    @Column(name="value_rule")
    private String valueRule;
    @Column(name="value_range")
    private String valueRange;
    @Column(name="descript")
    private String descript;
    private Integer flag=0;
    @Transient
    private String valueTypeDis;
    public String getValueTypeDis() {
        return valueTypeDis;
    }

    public void setValueTypeDis(String valueTypeDis) {
        this.valueTypeDis = valueTypeDis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SimSysInsMessageField getSimMessageField() {
        return simMessageField;
    }

    public void setSimMessageField(SimSysInsMessageField simMessageField) {
        this.simMessageField = simMessageField;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getValueRule() {
        return valueRule;
    }

    public void setValueRule(String valueRule) {
        this.valueRule = valueRule;
    }

    public String getValueRange() {
        return valueRange;
    }

    public void setValueRange(String valueRange) {
        this.valueRange = valueRange;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }
    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
