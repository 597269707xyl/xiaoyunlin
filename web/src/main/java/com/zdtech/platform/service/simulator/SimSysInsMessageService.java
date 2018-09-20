package com.zdtech.platform.service.simulator;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.service.GenericService;
import com.zdtech.platform.framework.service.UcodeService;
import com.zdtech.platform.framework.utils.Bundle;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * SimSysInsMessageService
 * @author xiaolanli
 * @date 2016/5/18
 */
@Service
public class SimSysInsMessageService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private GenericService genericService;
    @Autowired
    private SimSysInsMessageDao simSysInsMessageDao;
    @Autowired
    private SimSystemInstanceDao simSystemInstanceDao;
    @Autowired
    private SimSysInsMessageFieldService simSysInsMessageFieldService;
    @Autowired
    private SimMessageDao simMessageDao;
    @Autowired
    private SimSysInsReplyRuleDao simSysInsReplyRuleDao;
    @Autowired
    private MsgFieldDao msgFieldDao;
    @Autowired
    private SimMessageFieldDao simMessageFieldDao;
    @Autowired
    private SimSysInsMessageFieldDao insMessageFieldDao;

    public Map<String,Object> findMessages(Map<String, Object> params, Pageable page) {
        Pageable p = page;
        if (page != null){
            p = new PageRequest(page.getPageNumber() < 1?0:page.getPageNumber() - 1,page.getPageSize(),page.getSort());
        }
        Map<String,Object> map = genericService.commonQuery("simSysInsMessage",params,p);
        List<SimSysInsMessage> rows = (List<SimSysInsMessage>)map.get("rows");
        List<Map<String,Object>> transRows = new ArrayList<>();
        for (SimSysInsMessage msg:rows){
            try {
                Map<String,Object> transRow = BeanUtils.describe(msg);
                transRow.put("systemInstance",BeanUtils.describe(msg.getSystemInstance()));
                if (msg.getReplyRuleSet() != null && msg.getReplyRuleSet().size() > 0){
                    StringBuilder sb1 = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    StringBuilder sb3 = new StringBuilder();
                    StringBuilder sb4 = new StringBuilder();
                    for (SimSysInsReplyRule e:msg.getReplyRuleSet()){
                        sb1.append(e.getRespMessage().getName());
                        sb1.append(",");
                        sb2.append(e.getRespMessage().getMsgTypeCode());
                        sb2.append(",");
                        sb3.append(e.getRespMessage().getTrsCode());
                        sb3.append(",");
                        sb4.append(e.getRespMessage().getMsgType());
                        sb4.append(",");
                    }
                    if (sb1.charAt(sb1.length()-1)==','){
                        sb1.deleteCharAt(sb1.length()-1);
                    }
                    if (sb2.charAt(sb2.length()-1)==','){
                        sb2.deleteCharAt(sb2.length()-1);
                    }
                    if (sb3.charAt(sb3.length()-1)==','){
                        sb3.deleteCharAt(sb3.length()-1);
                    }
                    if (sb4.charAt(sb4.length()-1)==','){
                        sb4.deleteCharAt(sb4.length()-1);
                    }
                    transRow.put("respMsgName",sb1.toString());
                    transRow.put("respMsgTrsCode",sb3.toString());
                    transRow.put("respMsgTypeCode",sb2.toString());
                    transRow.put("respMesgType",sb4.toString());
                }
                transRows.add(transRow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        map.put("rows",transRows);
        return map;
    }


    public SimSysInsMessage get(Long id) {
        return simSysInsMessageDao.findOne(id);
    }

    public Result addSimMessage(Long simid, Long generalid, SimSysInsMessage simMessage) {
        Result result = null;
        SimSystemInstance simsystem=simSystemInstanceDao.findOne(simid);
        simMessage.setSystemInstance(simsystem);
        simMessage.setMd5Flag(simsystem.getSimSystem().getMd5Flag());
        if (simMessage.getId() == null){
            SimSysInsMessage simMessage1 = simSysInsMessageDao.findByNameAndTrs(simMessage.getName(),simMessage.getTrsCode(),simMessage.getMsgType(),simid);
            if (null != simMessage1) {
                result = new Result(false,"该报文已经存在!");
            } else {
                simSysInsMessageDao.save(simMessage);
                simSysInsMessageFieldService.addMessageField(simid,generalid,simMessage);
                result = new Result(true,"报文信息添加成功");
            }
        }else {
            SimSysInsMessage fs = simSysInsMessageDao.findOne(simMessage.getId());
            fs.setName(simMessage.getName());
            fs.setTrsCode(simMessage.getTrsCode());
            fs.setMsgTypeCode(simMessage.getMsgTypeCode());
            simSysInsMessageDao.save(fs);
            simSysInsMessageFieldService.addMessageField(simid,generalid,simMessage);
            result = new Result(true,"报文信息修改成功");
        }
        return result;
    }

    public void deleteSimMessages(List<Long> list) {
        for (Long id:list){
            deleteSimMessage(id);
        }
    }

    private void deleteSimMessage(Long id) {
        simSysInsMessageDao.delete(id);
    }

    public Long addXmlMessage(Long simid, SimSysInsMessage simMessage) {
        Long id=null;
        SimSystemInstance simsystem=simSystemInstanceDao.findOne(simid);
        simMessage.setSystemInstance(simsystem);
        if (simMessage.getId() == null){
            SimSysInsMessage simMessage1 = simSysInsMessageDao.findByMsgTypeAndSimId(simMessage.getMsgType(),simid);
            if (null== simMessage1) {
                simMessage=simSysInsMessageDao.save(simMessage);
                id=simMessage.getId();
                simSysInsMessageFieldService.addMessageField(simid,null,simMessage);
            }
        }else {
            SimSysInsMessage fs = simSysInsMessageDao.findOne(simMessage.getId());
            fs.setName(simMessage.getName());
            fs.setMsgType(simMessage.getMsgType());
            fs.setSignFlag(simMessage.getSignFlag());
            simSysInsMessageDao.save(fs);
            id=simMessage.getId();
            simSysInsMessageFieldService.addMessageField(simid,null,simMessage);
        }

        return id;

    }
    public List<String> addInstanceMessage(Long systemId, List<Long> list) {
        SimSystemInstance systemInstance=simSystemInstanceDao.findOne(systemId);
        List<String> messagelist=new ArrayList<>();
        if (systemInstance != null){
            List<SimSysInsMessage> messages = new ArrayList<>();
            for (Long id:list){
                SimSysInsMessage message = new SimSysInsMessage();
                SimMessage simMessage=simMessageDao.findOne(id);
//                SimSysInsMessage simSysInsMessage1=simSysInsMessageDao.findByNameAndTrs(simMessage.getName(),simMessage.getTrsCode(),simMessage.getMesgType(),systemId);
                SimSysInsMessage simSysInsMessage1=simSysInsMessageDao.findByMsgTypeAndSimId(simMessage.getMesgType(),systemId);
                if(simSysInsMessage1==null) {
                    message.setStandard(simMessage.getStandard());
                    message.setSystemInstance(systemInstance);
                    message.setSignFlag(simMessage.getSignFlag());
                    message.setCode(simMessage.getCode());
                    message.setMacFlag(simMessage.getMacFlag());
                    message.setMd5Flag(simMessage.getMd5Flag());
                    message.setModelFileContent(simMessage.getModelFileContent());
                    message.setMsgType(simMessage.getMesgType());
                    message.setMsgTypeCode(simMessage.getMsgTypeCode());
                    message.setName(simMessage.getName());
                    message.setSchemaFileContent(simMessage.getSchemaFileContent());
                    message.setSchemaFlag(simMessage.getSchemaFlag());
                    message.setTrsCode(simMessage.getTrsCode());
                    message.setType(simMessage.getType());
                    List<SimMessageField> msgFields = simMessage.getSimMessageFields();
                    List<SimSysInsMessageField> sysInsMessageFields = new ArrayList<>();

                    for (SimMessageField field : msgFields) {
                        SimSysInsMessageField insField = new SimSysInsMessageField();
                        insField.setSimSysInsMessage(message);
                        insField.setDefaultValue(field.getDvalue());
                        insField.setRespValue(field.getValue());
                        insField.setRespValueType(field.getValueType());
                        insField.setSignFlag(field.getSflag());
                        insField.setFieldType(field.getFieldType());
                        insField.setMoFlag(field.getMflag());
                        insField.setFixFlag(field.getFflag());
                        insField.setSeqNo(field.getSeq_no());
                        insField.setMsgField(field.getMsgField());
                        List<SimSysinsMsgFieldValue> values = new ArrayList<>();
                        List<SimMsgFieldValue> ls = field.getSimMsgFieldValues();
                        for (SimMsgFieldValue e:ls){
                            SimSysinsMsgFieldValue v = new SimSysinsMsgFieldValue();
                            v.setDescript(e.getDescript());
                            v.setFlag(e.getFlag());
                            v.setSimMessageField(insField);
                            v.setValueRange(e.getValueRange());
                            v.setValueRule(e.getValueRule());
                            v.setValueType(e.getValueType());
                            values.add(v);
                        }
                        insField.setSimMsgFieldValues(values);
                        sysInsMessageFields.add(insField);
                    }
                    message.setMsgFields(sysInsMessageFields);
                    messages.add(message);
                    simSysInsMessageDao.save(message);
                }else{
                    messagelist.add(simMessage.getName());
                }
            }
            for(Long id:list){
                SimMessage simMessage=simMessageDao.findOne(id);
                Set<SimReplyRule>  replyRules=simMessage.getReplySet();
                for(SimReplyRule replyRule:replyRules){
                    SimMessage requestMessage=replyRule.getReqMessage();
                    SimMessage replyMessage=replyRule.getRespMessage();
//                    SimSysInsMessage sysInsreqMessage=simSysInsMessageDao.findByNameAndTrs(requestMessage.getName(),requestMessage.getTrsCode(),requestMessage.getMesgType(),systemId);
                    SimSysInsMessage sysInsreqMessage=simSysInsMessageDao.findByMsgTypeAndSimId(requestMessage.getMesgType(),systemId);
//                    SimSysInsMessage sysInsrepMessage=simSysInsMessageDao.findByNameAndTrs(replyMessage.getName(),replyMessage.getTrsCode(),replyMessage.getMesgType(),systemId);
                    SimSysInsMessage sysInsrepMessage=simSysInsMessageDao.findByMsgTypeAndSimId(replyMessage.getMesgType(),systemId);
                    if(sysInsrepMessage!=null) {
                        SimSysInsReplyRule simSysInsReplyRule = new SimSysInsReplyRule();
                        simSysInsReplyRule.setReqMessage(sysInsreqMessage);
                        simSysInsReplyRule.setRespMessage(sysInsrepMessage);
                        simSysInsReplyRule.setParameter(replyRule.getParameter());
                        simSysInsReplyRuleDao.save(simSysInsReplyRule);
                    }
                }
            }

        }
        return messagelist;
    }

    public Result saveFile(Long id, ArrayList<Leaf> elemList, String model, String schema) {
        Result result=null;
        ArrayList<String> s=new ArrayList<String>();
        ArrayList<Long> fieldIds=new ArrayList<Long>();
        SimSysInsMessage simMessage = simSysInsMessageDao.findOne(id);
        for(int i=0;i<elemList.size();i++){
            String path=elemList.get(i).getXpath();
            MsgField msgField=msgFieldDao.findByFieldId(path);
            fieldIds.add(msgField.getId());
        }
        simSysInsMessageFieldService.addFields(id,fieldIds);
        simMessage.setModelFileContent(model);
        simMessage.setSchemaFileContent(schema);
        simSysInsMessageDao.save(simMessage);
        result=new Result(true,"报文添加成功");
        return result;
    }
    public Result saveFile(Long id, ArrayList<Leaf> elemList, String model) {
        Result result=null;
        ArrayList<String> s=new ArrayList<String>();
            ArrayList<Long> fieldIds=new ArrayList<Long>();
            SimSysInsMessage simMessage = simSysInsMessageDao.findOne(id);
            for(int i=0;i<elemList.size();i++){
                String path=elemList.get(i).getXpath();
                MsgField msgField=msgFieldDao.findByFieldId(path);
                fieldIds.add(msgField.getId());
            }
            simSysInsMessageFieldService.addFields(id,fieldIds);
            simMessage.setModelFileContent(model);
            simSysInsMessageDao.save(simMessage);
            result=new Result(true,"报文添加成功");
        return result;
    }
    public Result updateSimMessage(Long simid, SimSysInsMessage simMessage) {
        Result result = null;
        SimSystemInstance simsystem=simSystemInstanceDao.findOne(simid);
        simMessage.setSystemInstance(simsystem);
        simMessage.setMd5Flag(simsystem.getSimSystem().getMd5Flag());
            SimSysInsMessage fs = simSysInsMessageDao.findOne(simMessage.getId());
            fs.setName(simMessage.getName());
            fs.setTrsCode(simMessage.getTrsCode());
            fs.setMsgTypeCode(simMessage.getMsgTypeCode());
            simSysInsMessageDao.save(fs);
            result = new Result(true,"报文信息修改成功");
        return result;
    }

    public Result updateXmlMessage(Long simid, SimSysInsMessage simMessage) {
        Result result=null;
        SimSystemInstance simsystem=simSystemInstanceDao.findByid(simid);
        simMessage.setSystemInstance(simsystem);
        SimSysInsMessage fs = simSysInsMessageDao.findOne(simMessage.getId());
        fs.setName(simMessage.getName());
        fs.setMsgType(simMessage.getMsgType());
        fs.setSignFlag(simMessage.getSignFlag());
        fs.setSystemInstance(simsystem);
        fs.setStandard(simMessage.getStandard());
        simSysInsMessageDao.save(fs);
        //simSysInsMessageFieldService.addMessageField(simid,null,simMessage);
        result = new Result(true, "报文信息修改成功");
        return result;
    }

    public List<SimSysInsMessage> findSystemMsgs(Long requestId) {
        Long systemId = simSysInsMessageDao.getOne(requestId).getSystemInstance().getId();
        return simSysInsMessageDao.findMsgsBySystemId(systemId);
    }

    public Result clonetBaseData(Long id) {
        Result ret = new Result();
        SimSysInsMessage insMessage = simSysInsMessageDao.findOne(id);
        SimSystem system = insMessage.getSystemInstance().getSimSystem();
        SimMessage message = simMessageDao.findByMesgTypeAndSimId(insMessage.getMsgType(), system.getId());
        List<SimMessageField> simFieldList = simMessageFieldDao.findByid(message.getId());
        List<SimSysInsMessageField> fieldList = insMessageFieldDao.findByid(id);
        Map<Long, SimSysInsMessageField> map = listToMap(fieldList);
        for (SimMessageField field : simFieldList) {
            SimSysInsMessageField messageField = map.get(field.getMsgField().getId());
            if (messageField != null) {
                field.setDvalue(messageField.getDefaultValue());
                field.setMflag(messageField.getMoFlag());
                field.setSflag(messageField.getSignFlag());
                field.setValueType(messageField.getRespValueType());
                field.setValue(messageField.getRespValue());
                field.setSeq_no(messageField.getSeqNo());
                List<SimMsgFieldValue> values = new ArrayList<>();
                List<SimSysinsMsgFieldValue> ls = messageField.getSimMsgFieldValues();
                for (SimSysinsMsgFieldValue e : ls) {
                    SimMsgFieldValue v = new SimMsgFieldValue();
                    v.setDescript(e.getDescript());
                    v.setFlag(e.getFlag());
                    v.setSimMessageField(field);
                    v.setValueRange(e.getValueRange());
                    v.setValueRule(e.getValueRule());
                    v.setValueType(e.getValueType());
                    values.add(v);
                }
                field.setSimMsgFieldValues(values);
            }
        }
        simMessageFieldDao.save(simFieldList);
        return ret;
    }

    private Map<Long, SimSysInsMessageField> listToMap(List<SimSysInsMessageField> fieldList){
        Map<Long, SimSysInsMessageField> map = new HashMap<>();
        for(SimSysInsMessageField field : fieldList){
            map.put(field.getMsgField().getId(), field);
        }
        return map;
    }
}
