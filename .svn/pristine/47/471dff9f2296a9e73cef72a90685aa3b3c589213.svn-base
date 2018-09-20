package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by admin-win8 on 2018/1/22.
 */
@Entity
@Table(name = "nxy_func_all_recv_msg")
public class NxyFuncAllRecvMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "msg_code")
    private String msgCode;
    private String message;
    @Column(name = "seq_nb")
    private String seqNb;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",locale ="zh",timezone = "GMT+8")
    @Column(name = "time")
    private Date time;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeqNb() {
        return seqNb;
    }

    public void setSeqNb(String seqNb) {
        this.seqNb = seqNb;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
