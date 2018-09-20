package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.SimSysInsMessage;
import com.zdtech.platform.framework.entity.SimSysInsMessageField;
import com.zdtech.platform.framework.entity.SimSysInsReplyRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SimSysInsReplyRuleDao
 *
 * @author xiaolanli
 * @date 2016/5/18
 */
public interface SimSysInsReplyRuleDao extends JpaRepository<SimSysInsReplyRule, Long>, JpaSpecificationExecutor<SimSysInsReplyRule> {
    @Modifying
    @Transactional
    @Query(value = "delete  from SimSysInsReplyRule t where t.pk.reqMessage.id =?1")
    void deleteByReqId(Long requestid);

    @Query(value = "select t.pk.respMessage from SimSysInsReplyRule t where t.pk.reqMessage.id = ?1")
    List<SimSysInsMessage> findRespMsgsByReqMsg(Long reqId);
    @Query(value = "select t from SimSysInsReplyRule t where t.pk.reqMessage.id = ?1")
    List<SimSysInsReplyRule> findReplyRulesByReqMsg(Long reqId);

    @Query(value = "select t.pk.respMessage from SimSysInsReplyRule t where t.pk.reqMessage.id = ?1")
    List<SimSysInsMessage> getResponseMsgsByRequestId(Long requestId);

    @Modifying
    @Transactional
    @Query(value = "delete  from SimSysInsReplyRule t where t.pk.reqMessage.id =?1 and t.pk.respMessage.id = ?2")
    void deleteByReqIdAndRespId(Long requestId, Long id);

    @Query(value = "select count(1) from sim_sysins_reply_rule t where t.req_msg_id=?1",nativeQuery = true)
    Object findReplys(Long requestId);

    @Query(value = "select t.rep_msg_id from sim_sysins_reply_rule t where t.req_msg_id=?1",nativeQuery = true)
    List<Object> findReplyMsgs(Long id);

    @Query(value = "select t.req_msg_id from sim_sysins_reply_rule t where t.rep_msg_id=?1",nativeQuery = true)
    List<Object> findReqMsgs(Long id);
}
