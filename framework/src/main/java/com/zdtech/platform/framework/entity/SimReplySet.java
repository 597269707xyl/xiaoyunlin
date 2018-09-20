package com.zdtech.platform.framework.entity;

import javax.persistence.*;

/**
 * Created by ll007 on 2016/5/9.
 */
@Entity
@Table(name="sim_reply_set")
public class SimReplySet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="msg_id")
    private Long MsgId;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "message_field_id")
    private SimMessageField simMessageField;

    @Column(name="value_type")
    private String valueType;

    private String value;

    private String parameter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMsgId() {
        return MsgId;
    }

    public void setMsgId(Long msgId) {
        MsgId = msgId;
    }

    public SimMessageField getSimMessageField() {
        return simMessageField;
    }

    public void setSimMessageField(SimMessageField simMessageField) {
        this.simMessageField = simMessageField;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        valueType = valueType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
