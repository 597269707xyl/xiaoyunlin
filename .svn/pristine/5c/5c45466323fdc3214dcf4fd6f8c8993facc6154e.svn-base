package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.SimMessage;
import com.zdtech.platform.framework.entity.SimReplyRule;
import com.zdtech.platform.framework.entity.SimReplySet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SimReplyRuleDao
 *
 * @author xiaolanli
 * @date 2016/5/12.
 */
public interface SimReplyRuleDao extends JpaRepository<SimReplyRule, Long>, JpaSpecificationExecutor<SimReplyRule> {

    @Modifying
    @Transactional
    @Query(value = "delete  from SimReplyRule t where t.pk.reqMessage.id =?1")
    void deleteByReqId(Long requestid);

    @Query(value = "select t.pk.respMessage from SimReplyRule t where t.pk.reqMessage.id = ?1")
    List<SimMessage> getResponseMsgsByRequestId(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete  from SimReplyRule t where t.pk.reqMessage.id =?1 and t.pk.respMessage.id = ?2")
    void deleteByReqIdAndRespId(Long reqId,Long respId);
}
