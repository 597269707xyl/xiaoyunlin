package com.zdtech.platform.service.simulator;

import com.zdtech.platform.framework.entity.SimMessage;
import com.zdtech.platform.framework.entity.SimReplyRule;
import com.zdtech.platform.framework.entity.SimReplyRuleId;
import com.zdtech.platform.framework.repository.SimMessageDao;
import com.zdtech.platform.framework.repository.SimReplyRuleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * * SimReplyRuleService
 *
 * @author xiaolanli
 * @date 2016/5/12.
 */
@Service
public class SimReplyRuleService {
    @Autowired
    private SimReplyRuleDao simReplyRuleDao;
    @Autowired
    private SimMessageDao simMessageDao;

    public void saveReplyRule(Long requestid, List<Long> replyids) {
        simReplyRuleDao.deleteByReqId(requestid);
        SimMessage requestMessage=simMessageDao.findOne(requestid);
        for(Long id:replyids) {
            SimMessage replyMessage = simMessageDao.findOne(id);
            SimReplyRule s = new SimReplyRule();
            s.setReqMessage(requestMessage);
            s.setRespMessage(replyMessage);
            s.setParameter("norule");
            simReplyRuleDao.save(s);
        }
    }

    public void saveXmlReplyRule(Long requestid, String replymesgType) {
        String[]strs=replymesgType.split(",");
        ArrayList<SimMessage> replyMessages=new ArrayList<SimMessage>();
        for(int i=0;i<strs.length;i++){
            SimMessage s=simMessageDao.findByMesgType(strs[i]);
            replyMessages.add(s);
        }
        SimMessage requestMessage=simMessageDao.findOne(requestid);
        for(SimMessage replymessage:replyMessages){
            SimReplyRule simReplyRule = new SimReplyRule();
            simReplyRule.setReqMessage(requestMessage);
            simReplyRule.setRespMessage(replymessage);
            simReplyRuleDao.save(simReplyRule);
        }
    }

    public List<SimMessage> getResponseMsgsByRequestId(Long id){
        return simReplyRuleDao.getResponseMsgsByRequestId(id);
    }

    public void addReplyRules(Long requestId, Long[] replyIds,String param) {
        for (Long id:replyIds){
            addReplyRule(requestId,id,param);
        }
    }

    private void addReplyRule(Long requestId, Long replyId,String param) {
        SimReplyRule rule = new SimReplyRule();
        rule.setParameter(param);
        SimReplyRuleId pk = new SimReplyRuleId(simMessageDao.findOne(requestId),simMessageDao.findOne(replyId));
        rule.setPk(pk);
        simReplyRuleDao.save(rule);
    }

    public void deleteReplyRules(Long requestId, Long[] ids) {
        for (Long id:ids){
            deleteReplyRule(requestId,id);
        }
    }

    private void deleteReplyRule(Long requestId, Long id) {
        simReplyRuleDao.deleteByReqIdAndRespId(requestId,id);
    }
}
