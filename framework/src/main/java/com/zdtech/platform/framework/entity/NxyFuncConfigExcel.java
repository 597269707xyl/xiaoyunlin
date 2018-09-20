package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yjli on 2017/10/12.
 */
public class NxyFuncConfigExcel {
    private String variableZh;
    private String variableEn;
    private String variableValue;

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
}
