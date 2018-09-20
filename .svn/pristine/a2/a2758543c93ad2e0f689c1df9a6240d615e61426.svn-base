package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * SimSysInsReplyRuleId
 *
 * @author panli
 * @date 2016/5/17
 */
@Embeddable
public class SimSysInsReplyRuleId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private SimSysInsMessage reqMessage;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private SimSysInsMessage respMessage;

    public SimSysInsReplyRuleId() {
    }

    public SimSysInsReplyRuleId(SimSysInsMessage reqMessage, SimSysInsMessage respMessage) {
        this.reqMessage = reqMessage;
        this.respMessage = respMessage;
    }

    public SimSysInsMessage getReqMessage() {
        return reqMessage;
    }

    public void setReqMessage(SimSysInsMessage reqMessage) {
        this.reqMessage = reqMessage;
    }

    public SimSysInsMessage getRespMessage() {
        return respMessage;
    }

    public void setRespMessage(SimSysInsMessage respMessage) {
        this.respMessage = respMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimSysInsReplyRuleId that = (SimSysInsReplyRuleId) o;

        if (reqMessage != null ? !reqMessage.equals(that.reqMessage) : that.reqMessage != null) return false;
        return respMessage != null ? respMessage.equals(that.respMessage) : that.respMessage == null;

    }

    @Override
    public int hashCode() {
        int result = reqMessage != null ? reqMessage.hashCode() : 0;
        result = 31 * result + (respMessage != null ? respMessage.hashCode() : 0);
        return result;
    }
}
