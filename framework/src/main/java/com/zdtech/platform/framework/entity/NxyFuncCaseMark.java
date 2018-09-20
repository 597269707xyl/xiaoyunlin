package com.zdtech.platform.framework.entity;

import com.zdtech.platform.framework.persistence.QueryDef;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;
import org.springframework.data.domain.Sort;

import javax.persistence.*;

/**
 * Created by lyj on 2018/1/9.
 */
@Entity
@Table(name = "nxy_func_case_mark")
@QueryDef(queryTag = "caseMark", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "nxyFuncCaseMarkDao")
@EntityListeners(value = EntityCodeListener.class)
public class NxyFuncCaseMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "msg_code")
    private String msgCode;
    @Column(name = "msg_name")
    private String msgName;
    @Column(name = "field_id")
    private String fieldId;
    @Column(name = "standard")
    @FieldEnum(code = "STANDARD")
    private String standard;
    @Transient
    private String standardDis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getStandardDis() {
        return standardDis;
    }

    public void setStandardDis(String standardDis) {
        this.standardDis = standardDis;
    }
}
