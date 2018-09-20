package com.zdtech.platform.framework.entity;


import javax.persistence.*;

/**
 * Created by lyj on 2018/1/11.
 */
@Entity
@Table(name = "nxy_func_case_send")
public class NxyFuncCaseSend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "msg_code")
    private String msgCode;
    @Column(name = "msg_name")
    private String msgName;
    private String message;
    @ManyToOne
    @JoinColumn(name = "recv_id", referencedColumnName = "id")
    private NxyFuncCaseRecv caseRecv;
    private String remark;
    private String ids;
    @Column(name = "adapter_id")
    private Long adapterId;
    @Column(name = "sysins_id")
    private Long sysinsId;


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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NxyFuncCaseRecv getCaseRecv() {
        return caseRecv;
    }

    public void setCaseRecv(NxyFuncCaseRecv caseRecv) {
        this.caseRecv = caseRecv;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Long getAdapterId() {
        return adapterId;
    }

    public void setAdapterId(Long adapterId) {
        this.adapterId = adapterId;
    }

    public Long getSysinsId() {
        return sysinsId;
    }

    public void setSysinsId(Long sysinsId) {
        this.sysinsId = sysinsId;
    }

}
