package com.zdtech.platform.service.simulator;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.service.GenericService;
import com.zdtech.platform.framework.utils.XmlDocHelper;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * SimMessageService
 *
 * @author xiaolanli
 * @date  2016/5/9.
 */
@Service
public class SimMessageService {
    @Autowired
    private GenericService genericService;

    @Autowired
    private SimMessageDao simMessageDao;
    @Autowired
    private SimSystemDao simSystemDao;
    @Autowired
    private MsgFieldDao msgFieldDao;
    @Autowired
    private SimMessageFieldDao simMessageFieldDao;
    @Autowired
    private MsgSchemaFileDao msgSchemaFileDao;
    @Autowired
    private SimMessageFieldService simMessageFieldService;
    @Autowired
    private SimSystemInstanceDao simSystemInstanceDao;
    @Autowired
    private SimSysInsMessageFieldDao insMessageFieldDao;
    @Autowired
    private SimSysInsMessageDao insMessageDao;
    @Autowired
    private NxyFuncUsecaseDataDao usecaseDataDao;

    public Result addSimMessage(Long simid,Long generalid,SimMessage simMessage) {
        Result result = null;
        SimSystem simsystem=simSystemDao.findByid(simid);
        simMessage.setSimSystem(simsystem);
        simMessage.setMd5Flag(simsystem.getMd5Flag());
       if (simMessage.getId() == null){
           SimMessage simMessage1 = simMessageDao.findByNameAndTrs(simMessage.getName(),simMessage.getTrsCode(),simid);
           if (simMessage1!=null) {
               result = new Result(false,"该报文已经存在!");
           } else {
               try {
                   simMessageDao.save(simMessage);
                   simMessageFieldService.addfield(simid, generalid, simMessage);
                   result = new Result(true, "报文信息添加成功");
               }catch(Exception e){
                   e.printStackTrace();
               }
           }
        }else {
               SimMessage fs = simMessageDao.findOne(simMessage.getId());
               fs.setName(simMessage.getName());
               fs.setTrsCode(simMessage.getTrsCode());
               fs.setMsgTypeCode(simMessage.getMsgTypeCode());
               simMessageDao.save(fs);
               simMessageFieldService.addfield(simid,generalid,simMessage);
               result = new Result(true,"修改报文成功");
       }
        return result;
    }

    public SimMessage get(Long id) {
        return simMessageDao.findByid(id);
    }

    public SimMessage getall(Long id) {
        return simMessageDao.findOne(id);
    }

    public Map<String,Object> findMessages(Map<String, Object> params, Pageable page) {
        Pageable p = page;
        if (page != null){
            p = new PageRequest(page.getPageNumber() < 1?0:page.getPageNumber() - 1,page.getPageSize(),page.getSort());
        }
        Map<String,Object> map = genericService.commonQuery("simMessage",params,p);
        List<SimMessage>  rows = (List<SimMessage>)map.get("rows");
        List<Map<String,Object>> transRows = new ArrayList<>();
        for (SimMessage msg:rows){
            try {
                Map<String,Object> transRow = BeanUtils.describe(msg);
                transRow.put("simSystem",BeanUtils.describe(msg.getSimSystem()));
                if (msg.getReplySet() != null && msg.getReplySet().size() > 0){
                    StringBuilder sb1 = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    StringBuilder sb3 = new StringBuilder();
                    StringBuilder sb4 = new StringBuilder();
                    for (SimReplyRule e:msg.getReplySet()){
                        sb1.append(e.getRespMessage().getName());
                        sb1.append(",");
                        sb2.append(e.getRespMessage().getMsgTypeCode());
                        sb2.append(",");
                        sb3.append(e.getRespMessage().getTrsCode());
                        sb3.append(",");
                        sb4.append(e.getRespMessage().getMesgType());
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

    public List<SimMessage> getAllmessage(String type) {
        return simMessageDao.findByType(type);
    }

    public List<MsgSchemaFile> getAllSchemaFile() {
        return msgSchemaFileDao.findAll();
    }

    /*public Result addXmlMessage(Long simid, Long schemaFileid, SimMessage simMessage) {
        Result result = null;
        SimSystem simsystem=simSystemDao.findByid(simid);
        MsgSchemaFile file=msgSchemaFileDao.findOne(schemaFileid);
        simMessage.setSchemaFile(file);
        simMessage.setSimSystem(simsystem);
        if (simMessage.getId() == null){
            SimMessage simMessage1 = simMessageDao.findByMesgType(simMessage.getMesgType());
            if (null != simMessage1) {
                result = new Result(false,"该报文名已经存在!");
            } else {
                simMessageDao.save(simMessage);
                result = new Result(true,"报文信息添加成功");
            }
        }else {
            SimMessage fs = simMessageDao.findOne(simMessage.getId());
            fs.setName(simMessage.getName());
            fs.setMesgType(simMessage.getMesgType());
            fs.setSignFlag(simMessage.getSignFlag());
            fs.setSchemaFile(file);
            fs.setSimSystem(simsystem);
            simMessageDao.save(fs);
            result = new Result(true,"报文信息修改成功");
        }
        return result;
    }*/
    public Long addXmlMessage(Long simid,  SimMessage simMessage) {
        Long id = null;
        SimSystem simsystem = simSystemDao.findByid(simid);
        simMessage.setSimSystem(simsystem);
        // TODO: 2016/11/25  
            SimMessage simMessage1 = simMessageDao.findByMesgTypeAndSimId(simMessage.getMesgType(),simid);
            if (null == simMessage1) {
                simMessage=simMessageDao.save(simMessage);
                id=simMessage.getId();
                simMessageFieldService.addfield(simid,null,simMessage);
            }

        return id;
    }
    public  void deleteSimMessages(List<Long> list) {
        for (Long id:list){
            deleteSimMessage(id);
        }
    }
    public  void deleteSimMessage(Long id){
        simMessageDao.delete(id);
    }

    public List<SimMessage> getMessages(Long id) {

        SimSystemInstance systemInstance=simSystemInstanceDao.findOne(id);
        return simMessageDao.findBySystemid(systemInstance.getSimSystem().getId());


    }

    public Result saveFile(Long id, ArrayList<Leaf> elemList, String model, String schema) {
        Result rs=null;
        ArrayList<String> s=new ArrayList<String>();
        SimMessage simMessage = simMessageDao.findOne(id);
        ArrayList<Long> fieldIds=new ArrayList<Long>();
        for(int i=0;i<elemList.size();i++){
            String path=elemList.get(i).getXpath();
            MsgField msgField=msgFieldDao.findByFieldId(path);
            fieldIds.add(msgField.getId());
        }
        simMessageFieldService.addFields(id,fieldIds, elemList);
        simMessage.setModelFileContent(model);
        simMessage.setSchemaFileContent(schema);
        simMessageDao.save(simMessage);
        rs=new Result(true,"报文添加成功");
        return rs;
    }

    public Result saveFile(Long id, ArrayList<Leaf> elemList, String model) {
        Result rs=null;
        ArrayList<String> s=new ArrayList<String>();
        SimMessage simMessage = simMessageDao.findOne(id);
        ArrayList<Long> fieldIds=new ArrayList<Long>();
        for(int i=0;i<elemList.size();i++){
            String path=elemList.get(i).getXpath();
            MsgField msgField=msgFieldDao.findByFieldId(path);
            fieldIds.add(msgField.getId());
        }
        simMessageFieldService.addFields(id,fieldIds, elemList);
        simMessage.setModelFileContent(model);
        simMessageDao.save(simMessage);
        rs=new Result(true,"报文添加成功");
        return rs;
    }

    public Result updateXmlMessage(Long simid, SimMessage simMessage) {
        Result result=null;
        SimSystem simsystem=simSystemDao.findByid(simid);
        simMessage.setSimSystem(simsystem);
            SimMessage fs = simMessageDao.findOne(simMessage.getId());
            fs.setName(simMessage.getName());
            fs.setMesgType(simMessage.getMesgType());
            fs.setSignFlag(simMessage.getSignFlag());
            fs.setStandard(simMessage.getStandard());
            fs.setSimSystem(simsystem);
            simMessageDao.save(fs);
           // simMessageFieldService.addfield(simid,null,simMessage);
        result = new Result(true, "报文信息修改成功");
        return result;
    }

    public Result updateSimMessage(Long simid, SimMessage simMessage) {
        Result result = null;
        SimSystem simsystem=simSystemDao.findByid(simid);
        simMessage.setSimSystem(simsystem);
        simMessage.setMd5Flag(simsystem.getMd5Flag());
            SimMessage fs = simMessageDao.findOne(simMessage.getId());
            fs.setName(simMessage.getName());
            fs.setTrsCode(simMessage.getTrsCode());
            fs.setMsgTypeCode(simMessage.getMsgTypeCode());
            simMessageDao.save(fs);
            result = new Result(true,"修改报文成功");
        return result;
    }

    public List<SimMessage> findSystemMsgs(Long requestId) {
        Long systemId = simMessageDao.getOne(requestId).getSimSystem().getId();
        return simMessageDao.findMsgsBySystemId(systemId);
    }

    public Result clonetBaseData(Long simId, Long[] instanceIds){
        Result ret = new Result();
        SimMessage message = simMessageDao.findOne(simId);
        List<SimMessageField> simFieldList = simMessageFieldDao.findByid(simId);
        Map<Long, SimMessageField> map = listToMap(simFieldList);
        List<Long> fieldIds = new ArrayList<>();
        for(Long instanceId : instanceIds){
            SimSysInsMessage insMessage = insMessageDao.findByMsgTypeAndSimId(message.getMesgType(), instanceId);
            if(insMessage == null){
                SimSystemInstance instance = simSystemInstanceDao.findByid(instanceId);
                ret.setMsg("所选仿真实例\"" + instance.getName() + "\"里面没有改报文！");
                ret.setSuccess(false);
                return ret;
            }
            List<SimSysInsMessageField> instanceFieldList = insMessageFieldDao.findByid(insMessage.getId());
            List<SimSysInsMessageField> updateInstanceFieldList = new ArrayList<>();
            for(SimSysInsMessageField field : instanceFieldList){
                SimMessageField messageField = map.get(field.getMsgField().getId());
                if(messageField != null){
                    field.setDefaultValue(messageField.getDvalue());
                    field.setMoFlag(messageField.getMflag());
                    field.setSignFlag(messageField.getSflag());
                    field.setRespValueType(messageField.getValueType());
                    field.setRespValue(messageField.getValue());
                    field.setSeqNo(messageField.getSeq_no());
                    List<SimSysinsMsgFieldValue> values = new ArrayList<>();
                    List<SimMsgFieldValue> ls = messageField.getSimMsgFieldValues();
                    for (SimMsgFieldValue e:ls){
                        SimSysinsMsgFieldValue v = new SimSysinsMsgFieldValue();
                        v.setDescript(e.getDescript());
                        v.setFlag(e.getFlag());
                        v.setSimMessageField(field);
                        v.setValueRange(e.getValueRange());
                        v.setValueRule(e.getValueRule());
                        v.setValueType(e.getValueType());
                        values.add(v);
                    }
                    field.setSimMsgFieldValues(values);
                    fieldIds.add(field.getMsgField().getId());
                    updateInstanceFieldList.add(field);
                } else {
                    insMessageFieldDao.delete(field);
                }
            }
            if(map.size() != fieldIds.size()){
                for(Long id : map.keySet()){
                    if(fieldIds.contains(id)) continue;
                    SimMessageField messageField = map.get(id);
                    SimSysInsMessageField newField = new SimSysInsMessageField();
                    newField.setDefaultValue(messageField.getDvalue());
                    newField.setMoFlag(messageField.getMflag());
                    newField.setSignFlag(messageField.getSflag());
                    newField.setRespValueType(messageField.getValueType());
                    newField.setRespValue(messageField.getValue());
                    newField.setSeqNo(messageField.getSeq_no());
                    newField.setSimSysInsMessage(insMessage);
                    newField.setFieldType(messageField.getFieldType());
                    newField.setFixFlag(messageField.getFflag());
                    newField.setMsgField(messageField.getMsgField());
                    List<SimSysinsMsgFieldValue> values = new ArrayList<>();
                    List<SimMsgFieldValue> ls = messageField.getSimMsgFieldValues();
                    for (SimMsgFieldValue e:ls){
                        SimSysinsMsgFieldValue v = new SimSysinsMsgFieldValue();
                        v.setDescript(e.getDescript());
                        v.setFlag(e.getFlag());
                        v.setSimMessageField(newField);
                        v.setValueRange(e.getValueRange());
                        v.setValueRule(e.getValueRule());
                        v.setValueType(e.getValueType());
                        values.add(v);
                    }
                    newField.setSimMsgFieldValues(values);
                    updateInstanceFieldList.add(newField);
                }
            }
            if(updateInstanceFieldList.size()>0){
                insMessageFieldDao.save(updateInstanceFieldList);
            }
        }
        return ret;
    }

    private Map<Long, SimMessageField> listToMap(List<SimMessageField> simFieldList){
        Map<Long, SimMessageField> map = new HashMap<>();
        for(SimMessageField field : simFieldList){
            map.put(field.getMsgField().getId(), field);
        }
        return map;
    }

    public Result clonetBaseMsg(Long simId, Long[] instanceIds){
        Result ret = new Result();
        SimMessage message = simMessageDao.findOne(simId);
        for(Long instanceId : instanceIds){
            SimSysInsMessage insMessage = insMessageDao.findByMsgTypeAndSimId(message.getMesgType(), instanceId);
            if(insMessage == null){
                SimSystemInstance instance = simSystemInstanceDao.findByid(instanceId);
                ret.setMsg("所选仿真实例\"" + instance.getName() + "\"里面没有改报文！");
                ret.setSuccess(false);
                return ret;
            }
            insMessage.setModelFileContent(message.getModelFileContent());
            List<NxyFuncUsecaseData> dataList = usecaseDataDao.findByMessageId(insMessage.getId());
            for(NxyFuncUsecaseData data : dataList){
                data.setMessageMessage(XmlDocHelper.xmlZuZhuang(data.getMessageMessage(), insMessage));
            }
            insMessageDao.save(insMessage);
            usecaseDataDao.save(dataList);
        }
        return ret;
    }
}
