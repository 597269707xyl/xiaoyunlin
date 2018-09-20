package com.zdtech.platform.framework.entity;

import javax.persistence.*;

/**
 * Created by admin-win8 on 2017/12/12.
 */
@Entity
@Table(name = "nxy_msg_acc")
public class NxyMsgAcc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String msgcode;
    private String account;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgcode() {
        return msgcode;
    }

    public void setMsgcode(String msgcode) {
        this.msgcode = msgcode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
