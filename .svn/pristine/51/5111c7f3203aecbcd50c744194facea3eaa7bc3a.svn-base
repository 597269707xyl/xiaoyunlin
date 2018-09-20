package com.zdtech.platform.service.simulator;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.SimSysInsMessageDao;
import com.zdtech.platform.framework.repository.SimSysInsReplyRuleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SimSysInsReplyRuleService
 *
 * @author xiaolanli
 * @date 2016/5/18
 */
@Service
public class SimSysInsReplyRuleService {

    @Autowired
    private SimSysInsReplyRuleDao simSysInsReplyRuleDao;
    @Autowired
    private SimSysInsMessageDao simSysInsMessageDao;

    public void saveReplyRule(Long requestid, List<Long> list) {
        simSysInsReplyRuleDao.deleteByReqId(requestid);
        SimSysInsMessage requestMessage=simSysInsMessageDao.findOne(requestid);
        for(Long id:list) {
            SimSysInsMessage replyMessage = simSysInsMessageDao.findOne(id);
            SimSysInsReplyRule s = new SimSysInsReplyRule();
            s.setReqMessage(requestMessage);
            s.setRespMessage(replyMessage);
            s.setParameter("norule");
            simSysInsReplyRuleDao.save(s);
        }
    }

    public List<SimSysInsMessage> getResponseMsgsByRequestId(Long requestId) {
        return simSysInsReplyRuleDao.getResponseMsgsByRequestId(requestId);
    }

    public void addReplyRules(Long requestId, Long[] replyIds, String param) {
        for (Long id:replyIds){
            addReplyRule(requestId,id,param);
        }
    }
    private void addReplyRule(Long requestId, Long replyId,String param) {
        SimSysInsReplyRule rule = new SimSysInsReplyRule();
        rule.setParameter(param);
        SimSysInsReplyRuleId pk = new SimSysInsReplyRuleId(simSysInsMessageDao.findOne(requestId),simSysInsMessageDao.findOne(replyId));
        rule.setPk(pk);
        simSysInsReplyRuleDao.save(rule);
    }

    public void deleteReplyRules(Long requestId, Long[] ids) {
        for (Long id:ids){
            deleteReplyRule(requestId,id);
        }
    }
    private void deleteReplyRule(Long requestId, Long id) {
        simSysInsReplyRuleDao.deleteByReqIdAndRespId(requestId,id);
    }
}
