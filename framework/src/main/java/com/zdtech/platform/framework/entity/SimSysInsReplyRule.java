package com.zdtech.platform.framework.entity;

import javax.persistence.*;

/**
 * SimSysInsReplyRule
 *
 * @author panli
 * @date 2016/5/17
 */
@Entity
@Table(name = "sim_sysins_reply_rule")
@AssociationOverrides({
        @AssociationOverride(name="pk.reqMessage",joinColumns = @JoinColumn(name = "req_msg_id")),
        @AssociationOverride(name = "pk.respMessage",joinColumns = @JoinColumn(name = "rep_msg_id"))
})
public class SimSysInsReplyRule {
    @EmbeddedId
    private SimSysInsReplyRuleId pk = new SimSysInsReplyRuleId();

    public SimSysInsReplyRule() {
    }
    private String parameter;
    public SimSysInsReplyRule(SimSysInsReplyRuleId pk) {
        this.pk = pk;
    }

    public SimSysInsReplyRuleId getPk() {
        return pk;
    }

    public void setPk(SimSysInsReplyRuleId pk) {
        this.pk = pk;
    }

    @Transient
    public SimSysInsMessage getReqMessage(){
        return pk.getReqMessage();
    }

    public void setReqMessage(SimSysInsMessage message){
        pk.setReqMessage(message);
    }
    @Transient
    public SimSysInsMessage getRespMessage(){
        return pk.getRespMessage();
    }

    public void setRespMessage(SimSysInsMessage message){
        pk.setRespMessage(message);
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
