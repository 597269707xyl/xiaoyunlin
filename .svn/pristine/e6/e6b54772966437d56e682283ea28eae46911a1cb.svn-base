package com.zdtech.platform.framework.entity;

import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;

import javax.persistence.*;

/**
 * Created by yjli on 2017/9/6.
 */
@Entity
@Table(name = "nxy_func_usecase_data")
@EntityListeners(value = EntityCodeListener.class)
public class NxyFuncUsecaseData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "usecase_id", referencedColumnName = "id")
    private NxyFuncUsecase nxyFuncUsecase;
    @ManyToOne
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    private SimSysInsMessage simMessage;
    @Column(name = "message_name")
    private String messageName;
    @Column(name = "message_code")
    private String messageCode;
    @Column(name = "message_message")
    private String messageMessage;
    @Column(name = "seq_no")
    private Integer seqNo;
    @FieldEnum(code = "UC_RESULT")
    private String result;
    @Transient
    private String resultDis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NxyFuncUsecase getNxyFuncUsecase() {
        return nxyFuncUsecase;
    }

    public void setNxyFuncUsecase(NxyFuncUsecase nxyFuncUsecase) {
        this.nxyFuncUsecase = nxyFuncUsecase;
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

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
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
}
