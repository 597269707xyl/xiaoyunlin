package com.zdtech.platform.service.funcexec;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.network.Client;
import com.zdtech.platform.framework.network.entity.Message;
import com.zdtech.platform.framework.network.entity.Request;
import com.zdtech.platform.framework.network.entity.SimAutoTestMessage;
import com.zdtech.platform.framework.network.tcp.NettyClient;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.service.UcodeService;
import com.zdtech.platform.framework.service.UserService;
import com.zdtech.platform.framework.utils.Bundle;
import com.zdtech.platform.framework.utils.XmlDocHelper;
import com.zdtech.platform.web.controller.funcexec.FuncExecController;
import com.zdtech.platform.web.controller.funcexec.FuncUsecaseController;
import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by yjli on 2017/9/7.
 */
@Service
@Transactional
public class FuncUsecaseService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NxyFuncUsecaseDao nxyFuncUsecaseDao;
    @Autowired
    private NxyFuncUsecaseExecDao nxyFuncUsecaseExecDao;
    @Autowired
    private NxyFuncUsecaseDataDao nxyFuncUsecaseDataDao;
    @Autowired
    private NxyFuncUsecaseExecSendDao nxyFuncUsecaseExecSendDao;
    @Autowired
    private SimSystemInstanceDao sysInsDao;
    @Autowired
    private UcodeService ucodeService;
    @Value("${rno.pattern}")
    private String rnoPattern;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NxyFuncItemDao funcItemDao;
    @Autowired
    private TestProjectDao projectDao;
    @Autowired
    private NxyFuncUsecaseExpectedDao expectedDao;
    @Value("${uno.pattern}")
    private String unoPattern;
    @Autowired
    private NxyFuncUsecaseDataRuleDao ruleDao;
    @Autowired
    private UserService userService;
    @Autowired
    private NxyUsecaseExecBatchDao nxyUsecaseExecBatchDao;
    @Autowired
    private NxyFuncConfigDao nxyFuncConfigDao;
    public String getUnoPattern() {
        return unoPattern;
    }
    @Autowired
    private SimSysInsMessageDao simSysInsMessageDao;
    @Autowired
    private NxyFuncUsecaseExecSendDao sendDao;


    public Long sendExecMessages_bak(List<Long> idList, Long itemId, String type, Long sendInternal, String sendInternalStr) throws Exception {
        if (idList == null || idList.size() < 1){
            return null;
        }
        String userName = userService.getCurrentUser().getName();
        Long batchId = null;
        NxyUsecaseExecBatch batch = new NxyUsecaseExecBatch();
        if(itemId != null){
            batch.setItemId(itemId);
            batch.setItemType(type);
            batch.setStatus("starting");
            batch.setUserName(userName);
            batch.setUcCount(idList.size());
            batch.setExecSpeed(sendInternalStr);
            nxyUsecaseExecBatchDao.save(batch);
            batchId = batch.getId();
        }
        Map<Long,List<Long>> map = new HashMap<>();
        for (Long id : idList){
            List<NxyFuncUsecaseData> dataList = nxyFuncUsecaseDataDao.findByUsecaseId(id);
            if(dataList == null || dataList.isEmpty()){
                if(batchId != null){
                    String sql = "insert into nxy_func_usecase_exec (usecase_id,result,exec_time,round,batch_id) " +
                            " select ?,?,?,(select IFNULL(MAX(t.round),0)+1 from nxy_func_usecase_exec t where t.usecase_id=?),?";
                    jdbcTemplate.update(sql, id, "noexec", new Timestamp(new Date().getTime()), id, batchId);
                }
                continue;
            }
            SimSysInsMessage msg = dataList.get(0).getSimMessage();
            SimSystemInstance instance = dataList.get(0).getNxyFuncUsecase().getNxyFuncItem().getTestProject().getInstance();
            if(instance == null){
                throw new Exception("测试项目未指定仿真实例!");
            }
            if (msg != null){
                SimSystemInstance ssi = msg.getSystemInstance();
                if (map.containsKey(ssi.getId())){
                    map.get(ssi.getId()).add(id);
                }else {
                    List<Long> list = new ArrayList<>();
                    list.add(id);
                    map.put(ssi.getId(),list);
                }
            }
        }
        try{
            for (Long id:map.keySet()){
                SimSystemInstance ssi = sysInsDao.findOne(id);
                if(!ssi.getAdapter().getAdapterStatus()){
                    throw new Exception("适配器未启动!");
                }
                Client client = new NettyClient(ssi.getInsServerIp(),ssi.getInsServerPort());
                client.connect();
                SimAutoTestMessage testMessage = new SimAutoTestMessage(id, ssi.getAdapter().getId(), map.get(id));
                testMessage.setBatchId(batchId);
                testMessage.setSendInternal(sendInternal == null ? 0L : sendInternal);
                Message msg = testMessage;
                Request request = new Request(msg);
                client.send(request);
                client.close();
            }
        } catch (Exception e){
            e.printStackTrace();
            if(batchId != null){
                nxyUsecaseExecBatchDao.delete(batch);
            }
            throw new Exception("适配器未启动!");
        }
        return batchId;
    }

    public Result copyUsecase(Long[] usecaseIds, Long destId, Long instanceId){
        Result ret = new Result();
        NxyFuncItem item = funcItemDao.findOne(destId);
        NxyFuncUsecase u = nxyFuncUsecaseDao.findOne(usecaseIds[0]);
        logger.info("原项目Id：{},目标项目Id：{}", u.getNxyFuncItem().getTestProject().getId(), item.getTestProject().getId());
        Map<Long, SimSysInsMessage> map = new HashMap<>();
        for (int i=usecaseIds.length-1; i>=0; i--){
            Long ucId = usecaseIds[i];
            NxyFuncUsecase uc = nxyFuncUsecaseDao.findOne(ucId);
            NxyFuncUsecase newUc = new NxyFuncUsecase();
            newUc.setCreateTime(new Date());
            newUc.setCreateUser(uc.getCreateUser());
            newUc.setExpected(uc.getExpected());
            String pattern = getUnoPattern();
            Bundle bundle = new Bundle();
            bundle.putString("tno", item.getTestProject().getAbb() + "");
            bundle.putString("ino", item.getName());
            String uno = ucodeService.format(pattern, bundle);
            newUc.setNo(uno);
            newUc.setCaseNumber(uc.getCaseNumber());
            newUc.setNxyFuncItem(item);
            newUc.setPurpose(uc.getPurpose());
            newUc.setResult("noexec");
            newUc.setStep(uc.getStep());
            newUc.setSeqNo(uc.getSeqNo()==null?0:uc.getSeqNo());
            newUc.setUsecaseNo(uc.getUsecaseNo());
            nxyFuncUsecaseDao.save(newUc);
            List<NxyFuncUsecaseData> ucDatas = nxyFuncUsecaseDataDao.findByUsecaseId(uc.getId());
            for (NxyFuncUsecaseData ucData:ucDatas){
                NxyFuncUsecaseData newUcData = new NxyFuncUsecaseData();
                newUcData.setResult("noexec");
                newUcData.setMessageCode(ucData.getMessageCode());
                newUcData.setMessageMessage(ucData.getMessageMessage());
                newUcData.setMessageName(ucData.getMessageName());
                newUcData.setNxyFuncUsecase(newUc);
                newUcData.setSeqNo(ucData.getSeqNo());
                SimSysInsMessage sm = map.get(ucData.getSimMessage().getId());
                if(sm == null){
                    sm = getSimMessage(map, ucData.getSimMessage(), item.getTestProject().getId(), instanceId);
                }
                if(sm == null){
//                    ret.setSuccess(false);
//                    ret.setMsg("该项目所有实例下缺少相应报文模板!复制成功" + (usecaseIds.length-i-1) + "条。");
//                    return ret;
                    throw new RuntimeException("该项目所有实例下缺少" + ucData.getMessageCode() + "报文模板");
                }
                newUcData.setSimMessage(sm);
                nxyFuncUsecaseDataDao.save(newUcData);
                List<NxyFuncUsecaseExpected> expecteds = expectedDao.findByUsecaseDataId(ucData.getId());
                for (NxyFuncUsecaseExpected expected:expecteds){
                    NxyFuncUsecaseExpected newExpected = new NxyFuncUsecaseExpected();
                    newExpected.setExpectedValue(expected.getExpectedValue());
                    newExpected.setFieldIdName(expected.getFieldIdName());
                    newExpected.setMsgField(expected.getMsgField());
                    newExpected.setType(expected.getType());
                    newExpected.setUsecaseDataId(newUcData.getId());
                    expectedDao.save(newExpected);
                }
                List<NxyFuncUsecaseDataRule> rules = ruleDao.findByUcDataId(ucData.getId());
                for (NxyFuncUsecaseDataRule rule:rules){
                    NxyFuncUsecaseDataRule newRule = new NxyFuncUsecaseDataRule();
                    newRule.setUsecaseDataId(newUcData.getId());
                    newRule.setDestFieldId(rule.getDestFieldId());
                    newRule.setEvaluateType(rule.getEvaluateType());
                    newRule.setSrcFieldId(rule.getSrcFieldId());
                    newRule.setSrcSendRecv(rule.getSrcSendRecv());
                    SimSysInsMessage smm = map.get(rule.getSrcMsgId());
                    if(smm == null){
                        smm = getSimMessage(map, ucData.getSimMessage(), item.getTestProject().getId(), instanceId);
                    }
                    newRule.setSrcMsgId(smm.getId());
                    ruleDao.save(newRule);
                }
            }
        }
        ret.setMsg("复制成功" + usecaseIds.length + "条。");
        return ret;
    }

    private SimSysInsMessage getSimMessage(Map<Long, SimSysInsMessage> map, SimSysInsMessage simSysInsMessage, Long projectId, Long instanceId){
        if(instanceId != null){
            SimSysInsMessage sysInsMessage = simSysInsMessageDao.findByMsgTypeAndSimId(simSysInsMessage.getMsgType(), instanceId);
            if(sysInsMessage != null){
                map.put(simSysInsMessage.getId(), sysInsMessage);
                return sysInsMessage;
            }
        }
        String sql = "SELECT id FROM sim_sysins_message WHERE system_ins_id = (SELECT instance_id FROM test_project WHERE id=%s) AND mesg_type = '%s'";
        sql = String.format(sql, projectId, simSysInsMessage.getMsgType());
        List list = jdbcTemplate.queryForList(sql, BigInteger.class);
        if(list.size() > 0){
            Long id = Long.parseLong(list.get(0).toString());
            SimSysInsMessage sm = simSysInsMessageDao.findOne(id);
            map.put(simSysInsMessage.getId(), sm);
            return sm;
        }
        return null;
    }

    public Result copyItem(Long[] itemIds,Long destId,String destType, Long instanceId){
        Result ret = new Result();
        for (Long srcId : itemIds){
            String msg = doCopyItem(srcId,destId,destType, instanceId);
            if(!msg.isEmpty()){
                ret.setSuccess(false);
                ret.setMsg(msg);
                return ret;
            }
        }
        return ret;
    }

    private String doCopyItem(Long srcId, Long destId, String destType, Long instanceId) {
        String msg = "";
        Map<Long, SimSysInsMessage> map = new HashMap<>();
        NxyFuncItem srcItem = funcItemDao.findOne(srcId);
        NxyFuncItem newItem = new NxyFuncItem();
        newItem.setDescript(srcItem.getDescript());
        newItem.setName(srcItem.getName());
        newItem.setMark(srcItem.getMark());
        newItem.setType("t");
        newItem.setTestProject(destType.equalsIgnoreCase("project")?projectDao.findOne(destId):funcItemDao.findOne(destId).getTestProject());
        newItem.setParentId(destType.equalsIgnoreCase("project")?null:destId);
        logger.info("原项目Id：{},目标项目Id：{}", srcItem.getTestProject().getId(), newItem.getTestProject().getId());
        funcItemDao.save(newItem);
        if(srcItem.getParentId() == null && !"recv".equals(newItem.getMark())){
            List<NxyFuncConfig> configs = nxyFuncConfigDao.findByItemId(srcId);
            List<NxyFuncConfig> configList = new ArrayList<>();
            for(NxyFuncConfig config : configs){
                NxyFuncConfig g = new NxyFuncConfig();
                g.setItemId(newItem.getId());
                g.setVariableEn(config.getVariableEn());
                g.setVariableZh(config.getVariableZh());
                g.setVariableValue(config.getVariableValue());
                configList.add(g);
            }
            nxyFuncConfigDao.save(configList);
        }
        List<Long> ucIds = nxyFuncUsecaseDao.findByItemId(srcId);
        for (Long ucId:ucIds){
            NxyFuncUsecase uc = nxyFuncUsecaseDao.findOne(ucId);
            NxyFuncUsecase newUc = new NxyFuncUsecase();
            newUc.setCreateTime(new Date());
            newUc.setCreateUser(uc.getCreateUser());
            newUc.setExpected(uc.getExpected());
            String pattern = getUnoPattern();
            Bundle bundle = new Bundle();
            bundle.putString("tno", newItem.getTestProject().getAbb() + "");
            bundle.putString("ino", newItem.getName());
            String uno = ucodeService.format(pattern, bundle);
            newUc.setNo(uno);
            newUc.setCaseNumber(uc.getCaseNumber());
            newUc.setNxyFuncItem(newItem);
            newUc.setPurpose(uc.getPurpose());
            newUc.setResult("noexec");
            newUc.setStep(uc.getStep());
            newUc.setSeqNo(uc.getSeqNo()==null?0:uc.getSeqNo());
            newUc.setUsecaseNo(uc.getUsecaseNo());
            nxyFuncUsecaseDao.save(newUc);
            List<NxyFuncUsecaseData> ucDatas = nxyFuncUsecaseDataDao.findByUsecaseId(uc.getId());
            for (NxyFuncUsecaseData ucData:ucDatas){
                NxyFuncUsecaseData newUcData = new NxyFuncUsecaseData();
                newUcData.setResult("noexec");
                newUcData.setMessageCode(ucData.getMessageCode());
                newUcData.setMessageMessage(ucData.getMessageMessage());
                newUcData.setMessageName(ucData.getMessageName());
                newUcData.setNxyFuncUsecase(newUc);
                newUcData.setSeqNo(ucData.getSeqNo());
                SimSysInsMessage sm = map.get(ucData.getSimMessage().getId());
                if(sm == null){
                    sm = getSimMessage(map, ucData.getSimMessage(), newItem.getTestProject().getId(), instanceId);
                }
                if(sm == null){
//                    return "该项目所有实例下缺少相应报文模板!";
                    throw new RuntimeException("该项目所有实例下缺少" + ucData.getMessageCode() + "报文模板");
                }
                newUcData.setSimMessage(sm);
                nxyFuncUsecaseDataDao.save(newUcData);
                List<NxyFuncUsecaseExpected> expecteds = expectedDao.findByUsecaseDataId(ucData.getId());
                for (NxyFuncUsecaseExpected expected:expecteds){
                    NxyFuncUsecaseExpected newExpected = new NxyFuncUsecaseExpected();
                    newExpected.setExpectedValue(expected.getExpectedValue());
                    newExpected.setFieldIdName(expected.getFieldIdName());
                    newExpected.setMsgField(expected.getMsgField());
                    newExpected.setType(expected.getType());
                    newExpected.setUsecaseDataId(newUcData.getId());
                    expectedDao.save(newExpected);
                }
                List<NxyFuncUsecaseDataRule> rules = ruleDao.findByUcDataId(ucData.getId());
                for (NxyFuncUsecaseDataRule rule:rules){
                    NxyFuncUsecaseDataRule newRule = new NxyFuncUsecaseDataRule();
                    newRule.setUsecaseDataId(newUcData.getId());
                    newRule.setDestFieldId(rule.getDestFieldId());
                    newRule.setEvaluateType(rule.getEvaluateType());
                    newRule.setSrcFieldId(rule.getSrcFieldId());
                    newRule.setSrcSendRecv(rule.getSrcSendRecv());
                    SimSysInsMessage smm = map.get(rule.getSrcMsgId());
                    if(smm == null){
                        smm = getSimMessage(map, ucData.getSimMessage(), newItem.getTestProject().getId(), instanceId);
                    }
                    newRule.setSrcMsgId(smm.getId());
                    ruleDao.save(newRule);
                }
            }
        }
        List<NxyFuncItem> children = funcItemDao.findByParentId(srcId);
        for (NxyFuncItem child:children){
            doCopyItem(child.getId(),newItem.getId(),"item", instanceId);
        }
        return msg;
    }

    public boolean batch(Long batchId, int count){
        String execsql = "select count(*) from nxy_func_usecase_exec where batch_id = '%s' and result in ('execerror','resptimeout','noexpected','noexec')";
        execsql = String.format(execsql, batchId);
        int fail = jdbcTemplate.queryForObject(execsql, Integer.class);
        int succCount = 0;
        List<NxyFuncUsecaseExec> execs = nxyFuncUsecaseExecDao.findByBatchIdAndExpected(batchId);
        for(NxyFuncUsecaseExec exec : execs){
            int step = nxyFuncUsecaseDataDao.countByUsecaseId(exec.getNxyFuncUsecase().getId());  //该用例总共有多少步
            List<NxyFuncUsecaseExecSend> sends = nxyFuncUsecaseExecSendDao.findByExecIdAndStep(exec.getId(), step);
            if(sends != null && sends.size() > 0 && "expected".equals(sends.get(sends.size()-1).getResult())){
                succCount++;
            }
        }
        logger.info("批次Id：{}，失败{}条，成功{}条", batchId, fail, succCount);
        NxyUsecaseExecBatch batch = nxyUsecaseExecBatchDao.findOne(batchId);
        if(fail + succCount == count){
            String rate = "0";
            if(succCount != 0){
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                rate = numberFormat.format((float) succCount / (float) batch.getUcCount() * 100) + "%";
            }
            batch.setUcSuccCount(succCount);
            batch.setUcErrorCount(fail);
            batch.setRate(rate);
            batch.setEndTime(new Date());
            batch.setStatus("finished");
            nxyUsecaseExecBatchDao.save(batch);
            FuncUsecaseController.batchMap.remove(batchId);
            return true;
        } else {
            Date begin = batch.getBeginTime();
            long currentTime = System.currentTimeMillis();
            if(currentTime-begin.getTime()>= 3*60*60*1000){  //如果超过3个小时还没判断批量执行完成，则默认执行异常开始统计
                String rate = "0";
                if(succCount != 0){
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(2);
                    rate = numberFormat.format((float) succCount / (float) batch.getUcCount() * 100) + "%";
                }
                batch.setUcSuccCount(succCount);
                batch.setUcErrorCount(fail);
                batch.setRate(rate);
                batch.setEndTime(new Date());
                batch.setStatus("finished");
                nxyUsecaseExecBatchDao.save(batch);
                FuncUsecaseController.batchMap.remove(batchId);
                return true;
            } else {
                List<Long> idList = findTimeoutExecSendId();
                if (idList != null) {
                    for (Long id : idList) {
                        updateTimeoutState(id);
                    }
                }
            }
        }
        return false;
    }

    public boolean setBatchExecSuccess(Long batchId){
        try {
            Thread.sleep(2000);
            String execsql = "select count(*) from nxy_func_usecase_exec where batch_id = '%s' and result in ('execerror','resptimeout','noexpected','noexec','execsucc')";
            execsql = String.format(execsql, batchId);
            int fail = jdbcTemplate.queryForObject(execsql, Integer.class);
            int succCount = 0;
            List<NxyFuncUsecaseExec> execs = nxyFuncUsecaseExecDao.findByBatchIdAndExpected(batchId);
            for(NxyFuncUsecaseExec exec : execs){
                int step = nxyFuncUsecaseDataDao.countByUsecaseId(exec.getNxyFuncUsecase().getId());  //该用例总共有多少步
                List<NxyFuncUsecaseExecSend> sends = nxyFuncUsecaseExecSendDao.findByExecIdAndStep(exec.getId(), step);
                if(sends != null && sends.size() > 0 && "expected".equals(sends.get(sends.size()-1).getResult())){
                    succCount++;
                }
            }
            NxyUsecaseExecBatch batch = nxyUsecaseExecBatchDao.findOne(batchId);
            String rate = "0";
            if(succCount != 0){
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                rate = numberFormat.format((float) succCount / (float) batch.getUcCount() * 100) + "%";
            }
            batch.setUcSuccCount(succCount);
            batch.setUcErrorCount(fail);
            batch.setRate(rate);
            batch.setEndTime(new Date());
            batch.setStatus("finished");
            nxyUsecaseExecBatchDao.save(batch);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Long> findTimeoutExecSendId(){
        Long curTime = System.currentTimeMillis();
        String confSql = "select key_val from sys_conf where category='SIMULATOR_SERVER' and `key`='NEPS_TIMEOUT'";
        String threshold = jdbcTemplate.queryForObject(confSql, String.class);
        String sql = String.format("SELECT id FROM nxy_func_usecase_exec_send WHERE result = '%s' and time_stamp < (%d-%s)","execsucc",curTime,threshold);
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);

        if (list != null && list.size() > 0){
            List<Long> ret = new ArrayList<>();
            for (Map<String,Object> row:list){
                Long id = Long.parseLong(row.get("id").toString());
                ret.add(id);
            }
            return ret;
        }
        return null;
    }

    public void updateTimeoutState(Long sendId) {
        NxyFuncUsecaseExecSend sendData = sendDao.findOne(sendId);
        Document send = XmlDocHelper.getXmlFromStr(sendData.getMsg());
        String sendMsgId = XmlDocHelper.getNodeValue(send, "//MsgId");
        String msgId = "请求报文("+ XmlDocHelper.getNodeValue(send, "/transaction/header/msg/msgCd") +")标识号:" + sendMsgId;
        String seqNb = "请求报文("+ XmlDocHelper.getNodeValue(send, "/transaction/header/msg/msgCd") +")流水号:" +
                XmlDocHelper.getNodeValue(send, "/transaction/header/msg/seqNb");
        String sql = "UPDATE nxy_func_usecase_exec_send SET result=?,result_descript=? WHERE id = ?";
        jdbcTemplate.update(sql,"resptimeout","应答报文超时<br/>" + msgId + "<br/>" + seqNb,sendId);
        sql = "SELECT usecase_exec_id,step FROM nxy_func_usecase_exec_send WHERE id=?";
        List<Map<String,Object>> l = jdbcTemplate.queryForList(sql,sendId);
        if(l == null || l.size() < 1){
            return;
        }
        Long execId = Long.parseLong(l.get(0).get("usecase_exec_id").toString());
        Integer step = Integer.parseInt(l.get(0).get("step").toString());
        sql = "UPDATE nxy_func_usecase_exec SET result = ? WHERE id = ?";
        jdbcTemplate.update(sql,"resptimeout",execId);
        sql = "SELECT usecase_id,round FROM nxy_func_usecase_exec WHERE id=?";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,execId);
        if (list == null || list.size() < 1){
            return;
        }
        Long ucId = Long.parseLong(list.get(0).get("usecase_id").toString());
        Integer round = Integer.parseInt(list.get(0).get("round").toString());
        sql = "UPDATE nxy_func_usecase_data SET result = ? WHERE usecase_id = ? AND seq_no = ?";
        jdbcTemplate.update(sql,"resptimeout",ucId,step);
        sql = "UPDATE nxy_func_usecase SET result = ? WHERE (SELECT COUNT(1) FROM nxy_func_usecase_exec WHERE usecase_id = ?)=? AND id = ?";
        jdbcTemplate.update(sql,"resptimeout",ucId,round,ucId);
    }
}
