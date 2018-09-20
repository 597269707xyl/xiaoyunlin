package com.zdtech.platform.framework.entity;

import javax.persistence.*;

/**
 * Created by yjli on 2017/9/6.
 */
@Entity
@Table(name = "nxy_func_usecase_exec_recv")
public class NxyFuncUsecaseExecRecv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "exec_send_id", referencedColumnName = "id")
    private NxyFuncUsecaseExecSend nxyFuncUsecaseExecSend;
    @ManyToOne
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    private SimSysInsMessage simMessage;
    @Column(name = "message_name")
    private String messageName;
    @Column(name = "message_code")
    private String messageCode;
    @Column(name = "message_message")
    private String messageMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NxyFuncUsecaseExecSend getNxyFuncUsecaseExecSend() {
        return nxyFuncUsecaseExecSend;
    }

    public void setNxyFuncUsecaseExecSend(NxyFuncUsecaseExecSend nxyFuncUsecaseExecSend) {
        this.nxyFuncUsecaseExecSend = nxyFuncUsecaseExecSend;
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

    public String getMessageMessage() {
        return messageMessage;
    }

    public void setMessageMessage(String messageMessage) {
        this.messageMessage = messageMessage;
    }
}
