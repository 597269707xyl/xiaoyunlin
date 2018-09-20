package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdtech.platform.framework.persistence.QueryDef;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lyj on 2018/1/11.
 */
@Entity
@Table(name = "nxy_func_case_recv")
@QueryDef(queryTag = "caseRecv", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "nxyFuncCaseRecvDao")
public class NxyFuncCaseRecv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "case_no")
    private String caseNo;
    @Column(name = "bank_no")
    private String bankNo;
    @Column(name = "msg_seq_no")
    private String msgSeqNo;
    @Column(name = "msg_id")
    private String msgId;
    @Column(name = "msg_code")
    private String msgCode;
    @Column(name = "msg_name")
    private String msgName;
    private String message;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",locale ="zh",timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "epcc_ids")
    private String epccIds;
    private Integer status;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getMsgSeqNo() {
        return msgSeqNo;
    }

    public void setMsgSeqNo(String msgSeqNo) {
        this.msgSeqNo = msgSeqNo;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEpccIds() {
        return epccIds;
    }

    public void setEpccIds(String epccIds) {
        this.epccIds = epccIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
