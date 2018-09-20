package com.zdtech.platform.framework.entity;

import javax.persistence.*;

/**
 * NxyFuncUsecaseDataRule
 *
 * @author panli
 * @date 2017/9/10
 */
@Entity
@Table(name = "nxy_func_usecase_data_rule")
public class NxyFuncUsecaseDataRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="usecase_data_id")
    private Long usecaseDataId;

    @Column(name="dest_field_id")
    private String destFieldId;

    @Column(name = "src_msg_id")
    private Long srcMsgId;

    @Column(name="src_field_id")
    private String srcFieldId;

    @Column(name="src_send_recv")
    private String srcSendRecv;

    @Column(name="evaluate_type")
    private String evaluateType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsecaseDataId() {
        return usecaseDataId;
    }

    public void setUsecaseDataId(Long usecaseDataId) {
        this.usecaseDataId = usecaseDataId;
    }

    public String getDestFieldId() {
        return destFieldId;
    }

    public void setDestFieldId(String destFieldId) {
        this.destFieldId = destFieldId;
    }

    public Long getSrcMsgId() {
        return srcMsgId;
    }

    public void setSrcMsgId(Long srcMsgId) {
        this.srcMsgId = srcMsgId;
    }

    public String getSrcFieldId() {
        return srcFieldId;
    }

    public void setSrcFieldId(String srcFieldId) {
        this.srcFieldId = srcFieldId;
    }

    public String getSrcSendRecv() {
        return srcSendRecv;
    }

    public void setSrcSendRecv(String srcSendRecv) {
        this.srcSendRecv = srcSendRecv;
    }

    public String getEvaluateType() {
        return evaluateType;
    }

    public void setEvaluateType(String evaluateType) {
        this.evaluateType = evaluateType;
    }
}
