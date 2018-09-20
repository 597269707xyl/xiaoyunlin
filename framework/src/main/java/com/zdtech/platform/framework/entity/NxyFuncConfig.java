package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yjli on 2017/10/12.
 */
@Entity
@Table(name = "nxy_func_config")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class NxyFuncConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "variable_zh")
    private String variableZh;
    @Column(name = "variable_en")
    private String variableEn;
    @Column(name = "variable_value")
    private String variableValue;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getVariableZh() {
        return variableZh;
    }

    public void setVariableZh(String variableZh) {
        this.variableZh = variableZh;
    }

    public String getVariableEn() {
        return variableEn;
    }

    public void setVariableEn(String variableEn) {
        this.variableEn = variableEn;
    }

    public String getVariableValue() {
        return variableValue;
    }

    public void setVariableValue(String variableValue) {
        this.variableValue = variableValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
