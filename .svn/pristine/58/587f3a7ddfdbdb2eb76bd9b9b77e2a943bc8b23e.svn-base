package com.zdtech.platform.framework.entity;

import javax.persistence.*;

/**
 * SimReplyRule
 *
 * @author panli
 * @date 2016/5/11
 */
@Entity
@Table(name = "sim_reply_rule")
@AssociationOverrides({
        @AssociationOverride(name="pk.reqMessage",joinColumns = @JoinColumn(name = "req_msg_id")),
        @AssociationOverride(name = "pk.respMessage",joinColumns = @JoinColumn(name = "rep_msg_id"))
})
public class SimReplyRule {

    @EmbeddedId
    protected SimReplyRuleId pk = new SimReplyRuleId();

    public SimReplyRule() {
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    private String  parameter;
    public SimReplyRule(SimReplyRuleId pk) {
        this.pk = pk;
    }

    public SimReplyRuleId getPk() {
        return pk;
    }

    public void setPk(SimReplyRuleId pk) {
        this.pk = pk;
    }
    @Transient
    public SimMessage getReqMessage(){
        return pk.getReqMessage();
    }

    public void setReqMessage(SimMessage message){
        pk.setReqMessage(message);
    }
    @Transient
    public SimMessage getRespMessage(){
        return pk.getRespMessage();
    }

    public void setRespMessage(SimMessage message){
        pk.setRespMessage(message);
    }
}
