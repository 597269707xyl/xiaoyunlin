package com.zdtech.platform.service.simulator;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.repository.SimSystemInstanceDao;
import com.zdtech.platform.framework.service.SysCodeService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by htma on 2016/5/12.
 */
@Service
public class SimSystemIntanceService {
    private static Logger logger = LoggerFactory.getLogger(SimSystemIntanceService.class);

    @Autowired
    private SimSystemInstanceDao simSystemInstanceDao;
    @Autowired
    private SimSystemDao simSystemDao;
    @Autowired
    private SimSysInsMessageDao simSysInsMessageDao;
    @Autowired
    private SimSysInsMessageFieldDao simSysInsMessageFieldDao;
    @Autowired
    private SimSysinsMsgFieldValueDao simSysinsMsgFieldValueDao;
    @Autowired
    private SimSysInsMsgFieldCodeDao simSysInsMsgFieldCodeDao;
    @Autowired
    private SimSysInsReplyRuleDao simSysInsReplyRuleDao;
    @Autowired
    private SysCodeService codeService;
    @Autowired
    private SimSysinsConfDao simSysinsConfDao;
    @Autowired
    private SimRecvQueueDao simRecvQueueDao;
    @Autowired
    private EntityManager entityManager;

    public SimSystemInstance get(Long id) {
        return simSystemInstanceDao.findOne(id);
    }
    public Result addSimSysInstance(SimSystemInstance simSystemInstance) {
        Result ret=new Result();
        Date date = new Date();
        simSystemInstance.setCreateTime(date);
        simSystemInstance.setState(SimSystemInstance.SysInsState.Finished);
        if(simSystemInstance.getId()==null) {
            simSystemInstanceDao.save(simSystemInstance);
            //添加实例参数
            Map<String,String> map = codeService.getCategoryCodes("SIM_INS_CONF");
            if (map != null){
                for (String key:map.keySet()){
                    SimSysinsConf conf = new SimSysinsConf();
                    conf.setParamKey(key);
                    conf.setParamKeyName(map.get(key));
                    conf.setSimSysinsId(simSystemInstance.getId());
                    simSysinsConfDao.save(conf);
                }
            }
            ret =new Result(true,"实例添加成功");
        }else{
            SimSystemInstance simSystemInstance1=simSystemInstanceDao.findOne(simSystemInstance.getId());
            SimSystemInstance.SysInsState state =simSystemInstance1.getState();
            simSystemInstance.setSimRecvQueueData(null);
//            if(state== SimSystemInstance.SysInsState.Finished){
//                simSystemInstanceDao.save(simSystemInstance);
//                ret =new Result(true,"实例修改成功");
//            }else{
//                ret =new Result(false,"实例状态不是停止状态，不能修改!");
//            }
            if(simSystemInstance1.getAdapter() == null || !simSystemInstance1.getAdapter().getAdapterStatus()){
                simSystemInstanceDao.save(simSystemInstance);
                ret =new Result(true,"实例修改成功");
            }else{
                ret =new Result(false,"实例状态不是停止状态，不能修改!");
            }
        }
        return ret;
    }
    public void deleteSimSysIntances(List<Long> list) {
        for (Long id:list){
            deleteSimSysInstance(id);
        }
    }
    public void deleteSimSysInstance(Long id){
        SimSystemInstance sim = simSystemInstanceDao.findOne(id);
        if (sim.getState() == SimSystemInstance.SysInsState.Finished)
            simSystemInstanceDao.delete(id);
    }
    public Map<String,Object> getAll() {
        List<SimSystemInstance> list = simSystemInstanceDao.findAll();
        Map<String,Object> ret = new HashMap<>();
        if (list == null || list.size() < 1){
            ret.put("total",0);
            ret.put("rows",list);
            return ret;
        }
        ret.put("total",list.size());
        ret.put("rows",list);
        return ret;
    }

    public List<SimSystemInstance> getAllInstance(String type) {
        List<SimSystemInstance> lists=new ArrayList<>();
        lists=simSystemInstanceDao.fingByType(type);
        return lists;
    }

    public List<Map<String,String>> getAllSimSystemOrder(String type) {
        List<Map<String, String>> ret = new ArrayList<>();
        List<Object[]> rows = null;
        if (StringUtils.isEmpty(type)){
            rows = simSystemInstanceDao.findSimSystemOrder();
        }else {
            rows = simSystemInstanceDao.findSimSystemOrder(type);
        }
        if (rows == null){
            return ret;
        }
        for (Object[] row : rows) {
            Map<String,String> map = new HashMap<>();
            map.put("id",row[0].toString());
            map.put("text", row[1].toString());
            ret.add(map);
        }
        return ret;
    }

    public Result cloneSimInstance(Long srcId,SimSystemInstance baseInfo){
        Result ret = new Result();
        try {
            SimSystemInstance srcSim = simSystemInstanceDao.findOne(srcId);
            if (srcSim.getName().equals(baseInfo.getName())){
                ret.setSuccess(false);
                ret.setMsg("请修改仿真实例名称");
                return ret;
            }
            baseInfo.setConnectStatus(SimSystemInstance.SysInsConnectStatus.DisConnect);
            baseInfo.setState(SimSystemInstance.SysInsState.Finished);
            baseInfo.setCreateTime(new Date());
            baseInfo.setInsServerIp(srcSim.getInsServerIp());
            baseInfo.setInsServerPort(srcSim.getInsServerPort());
            baseInfo.setSimSystem(srcSim.getSimSystem());
            baseInfo.setTcpMode(srcSim.getTcpMode());
            baseInfo.setHeartbeatFlag(srcSim.getHeartbeatFlag());
            baseInfo.setHeartbeatRecvData(srcSim.getHeartbeatRecvData());
            baseInfo.setHeartbeatSendData(srcSim.getHeartbeatSendData());
            baseInfo.setConnectFlag(srcSim.getConnectFlag());
            simSystemInstanceDao.save(baseInfo);
            Long newId = baseInfo.getId();
            //复制实例参数
            List<SimSysinsConf> confs = simSysinsConfDao.findBySimSysinsId(srcId);
            if (confs != null){
                for (SimSysinsConf conf:confs){
                    SimSysinsConf c = new SimSysinsConf();
                    c.setSimSysinsId(newId);
                    c.setParamKey(conf.getParamKey());
                    c.setParamKeyName(conf.getParamKeyName());
                    c.setParamValue(conf.getParamValue());
                    simSysinsConfDao.save(c);
                }
            }
            List<SimSysInsMessage> msgs = simSysInsMessageDao.findMsgsBySystemId(srcId);
            //保存目标id和源id关系
            Map<Long,Long> destSrcMap = new HashMap<>();
            //保存源id和目标id关系
            Map<Long,Long> srcDestMap = new HashMap<>();
            if (msgs != null && msgs.size() > 0){
                for (SimSysInsMessage msg:msgs){
                    SimSysInsMessage newMsg = cloneSimMessage(newId,msg);
                    destSrcMap.put(newMsg.getId(),msg.getId());
                    srcDestMap.put(msg.getId(),newMsg.getId());
                }
            }
            cloneMsgReplyRule(destSrcMap,srcDestMap);
            ret.setSuccess(true);
            ret.setMsg("操作成功");
        }catch (Exception e){
            logger.error("实例复制异常：srcid：{},message:{}",srcId,e.getMessage());
            ret.setSuccess(false);
            ret.setMsg("操作失败");
        }
        return ret;
    }

    private void cloneMsgReplyRule(Map<Long, Long> destSrcMap,Map<Long, Long> srcDestMap) {
        if (destSrcMap == null || srcDestMap == null){
            return;
        }
        for (Long destReqId:destSrcMap.keySet()){
            Long srcId = destSrcMap.get(destReqId);
            SimSysInsMessage srcMsg = simSysInsMessageDao.findOne(srcId);
            if (srcMsg != null){
                Set<SimSysInsReplyRule> set = srcMsg.getReplyRuleSet();
                if (set != null){
                    for (SimSysInsReplyRule rule:set){
                        Long respId = rule.getRespMessage().getId();
                        Long destRespId = srcDestMap.get(respId);
                        if (destRespId != null){
                            SimSysInsReplyRule r = new SimSysInsReplyRule();
                            SimSysInsReplyRuleId id = new SimSysInsReplyRuleId();
                            id.setReqMessage(simSysInsMessageDao.findOne(destReqId));
                            id.setRespMessage(simSysInsMessageDao.findOne(destRespId));
                            r.setPk(id);
                            r.setParameter(rule.getParameter());
                            simSysInsReplyRuleDao.save(r);
                        }
                    }
                }
            }
        }
    }

    private SimSysInsMessage cloneSimMessage(Long simInsId, SimSysInsMessage msg) {
        SimSysInsMessage newMsg = new SimSysInsMessage();
        newMsg.setCode(msg.getCode());
        newMsg.setMacFlag(msg.getMacFlag());
        newMsg.setMd5Flag(msg.getMd5Flag());
        newMsg.setModelFileContent(msg.getModelFileContent());
        newMsg.setModelFileName(msg.getModelFileName());
        newMsg.setModelFilePath(msg.getModelFilePath());
        newMsg.setMsgType(msg.getMsgType());
        newMsg.setMsgTypeCode(msg.getMsgTypeCode());
        newMsg.setName(msg.getName());
        newMsg.setSchemaFile(msg.getSchemaFile());
        newMsg.setSchemaFileContent(msg.getSchemaFileContent());
        newMsg.setSchemaFlag(msg.getSchemaFlag());
        newMsg.setSignFlag(msg.getSignFlag());
        newMsg.setSystemInstance(simSystemInstanceDao.findOne(simInsId));
        newMsg.setTrsCode(msg.getTrsCode());
        newMsg.setType(msg.getType());
        newMsg.setStandard(msg.getStandard());
        simSysInsMessageDao.save(newMsg);
        List<SimSysInsMessageField> fields = msg.getMsgFields();
        for (SimSysInsMessageField field:fields){
            SimSysInsMessageField newField = new SimSysInsMessageField();
            newField.setDefaultValue(field.getDefaultValue());
            newField.setFieldType(field.getFieldType());
            newField.setFieldValueType(field.getFieldValueType());
            newField.setFieldValueTypeParam(field.getFieldValueTypeParam());
            newField.setFixFlag(field.getFixFlag());
            newField.setMoFlag(field.getMoFlag());
            newField.setMsgField(field.getMsgField());
            newField.setPadChar(field.getPadChar());
            newField.setPadFlag(field.getPadFlag());
            newField.setRespParameter(field.getRespParameter());
            newField.setRespValue(field.getRespValue());
            newField.setRespValueType(field.getRespValueType());
            newField.setSeqNo(field.getSeqNo());
            newField.setSignFlag(field.getSignFlag());
            newField.setSimSysInsMessage(newMsg);
            simSysInsMessageFieldDao.save(newField);
            List<SimSysinsMsgFieldValue> values = field.getSimMsgFieldValues();
            for (SimSysinsMsgFieldValue value:values){
                SimSysinsMsgFieldValue  newValue = new  SimSysinsMsgFieldValue();
                newValue.setFlag(value.getFlag());
                newValue.setValueType(value.getValueType());
                newValue.setValueRule(value.getValueRule());
                newValue.setValueRange(value.getValueRange());
                newValue.setDescript(value.getDescript());
                newValue.setSimMessageField(newField);
                simSysinsMsgFieldValueDao.save(newValue);
            }
            List<SimSysInsMsgFieldCode> codes = field.getMsgFieldCodes();
            for (SimSysInsMsgFieldCode code:codes){
                SimSysInsMsgFieldCode newCode = new SimSysInsMsgFieldCode();
                newCode.setKey(code.getKey());
                newCode.setMsgField(newField);
                newCode.setSeqNo(code.getSeqNo());
                newCode.setValue(code.getValue());
                simSysInsMsgFieldCodeDao.save(newCode);
            }
        }
        return newMsg;
    }

    public List fingAllByInstanceId(Long instanceId){
        String countSql = "select count(*) from sim_recv_queue where instance_id=" + instanceId;
        Query query = entityManager.createNativeQuery(countSql);
        BigInteger count = (BigInteger) query.getSingleResult();
        if(count.intValue() == 0){
            return simRecvQueueDao.fingAllDistinct();
        }
        String sql = "SELECT s.queue_name as queueNameRecv, s.queue_manager as queueManagerRecv," +
                " s.host_name as hostNameRecv, s.port as portRecv, s.channel as channelRecv, s.CCSID as ccsidRecv," +
                " s.user_id as userIdRecv, s.password as passwordRecv, s.receive_queue as receiveQueueRecv" +
                " FROM sim_recv_queue s WHERE s.queue_name NOT IN " +
                "(SELECT queue_name FROM sim_recv_queue WHERE instance_id=" + instanceId + ") GROUP BY s.queue_name";
        query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }
}