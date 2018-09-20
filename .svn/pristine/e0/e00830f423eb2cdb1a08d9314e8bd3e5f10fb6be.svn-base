package com.zdtech.platform.framework.entity;

import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;

import javax.persistence.*;

/**
 * Created by yjli on 2017/9/6.
 */
@Entity
@Table(name = "nxy_func_usecase_exec_send")
@EntityListeners(value = EntityCodeListener.class)
public class NxyFuncUsecaseExecSend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "usecase_exec_id", referencedColumnName = "id")
    private NxyFuncUsecaseExec nxyFuncUsecaseExec;
    private String msg;
    private String ids;
    @ManyToOne
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    private SimSysInsMessage simMessage;
    @Column(name = "message_name")
    private String messageName;
    @Column(name = "message_code")
    private String messageCode;
    private Integer step;
    @FieldEnum(code = "UC_RESULT")
    private String result;
    @Transient
    private String resultDis;
    @Column(name = "time_stamp")
    private Long timeStamp;
    @Column(name = "result_descript")
    private String resultDescript;
    @Column(name = "epcc_ids")
    private String epccIds;
    @Column(name = "msg_id")
    private String msgId;

    public String getResultDescript() {
        return resultDescript;
    }

    public void setResultDescript(String resultDescript) {
        this.resultDescript = resultDescript;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NxyFuncUsecaseExec getNxyFuncUsecaseExec() {
        return nxyFuncUsecaseExec;
    }

    public void setNxyFuncUsecaseExec(NxyFuncUsecaseExec nxyFuncUsecaseExec) {
        this.nxyFuncUsecaseExec = nxyFuncUsecaseExec;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public SimSysInsMessage getSimMessage() {
        return simMessage;
    }

    public void setSimMessage(SimSysInsMessage simMessage) {
        this.simMessage = simMessage;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultDis() {
        return resultDis;
    }

    public void setResultDis(String resultDis) {
        this.resultDis = resultDis;
    }

    public String getEpccIds() {
        return epccIds;
    }

    public void setEpccIds(String epccIds) {
        this.epccIds = epccIds;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
