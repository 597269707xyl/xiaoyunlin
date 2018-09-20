package com.zdtech.platform.framework.entity;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * SimReplyRuleId
 *
 * @author panli
 * @date 2016/5/11
 */
@Embeddable
public class SimReplyRuleId implements Serializable {
    @ManyToOne
    private SimMessage reqMessage;
    @ManyToOne
    private SimMessage respMessage;

    public SimReplyRuleId() {
    }

    public SimReplyRuleId(SimMessage reqMessage, SimMessage respMessage) {
        this.reqMessage = reqMessage;
        this.respMessage = respMessage;
    }

    public SimMessage getReqMessage() {
        return reqMessage;
    }

    public void setReqMessage(SimMessage reqMessage) {
        this.reqMessage = reqMessage;
    }

    public SimMessage getRespMessage() {
        return respMessage;
    }

    public void setRespMessage(SimMessage respMessage) {
        this.respMessage = respMessage;
    }

    @Override
    public int hashCode() {
        int result;
        result = (reqMessage != null ? reqMessage.hashCode() : 0);
        result = 31 * result + (respMessage != null ? respMessage.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        SimReplyRuleId that = (SimReplyRuleId) obj;

        if (reqMessage != null ? !reqMessage.equals(that.reqMessage) : that.reqMessage != null) return false;
        if (respMessage != null ? !respMessage.equals(that.respMessage) : that.respMessage != null)
            return false;

        return true;
    }
}
