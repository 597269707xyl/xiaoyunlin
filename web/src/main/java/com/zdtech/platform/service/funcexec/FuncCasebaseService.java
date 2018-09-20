package com.zdtech.platform.service.funcexec;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.service.UcodeService;
import com.zdtech.platform.framework.service.UserService;
import com.zdtech.platform.framework.utils.Bundle;
import com.zdtech.platform.framework.utils.SeqNoUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by lyj on 2018/7/30.
 */
@Service
@Transactional
public class FuncCasebaseService {
    private Logger logger = LoggerFactory.getLogger(FuncCasebaseService.class);
    @Autowired
    private NxyFuncItemDao nxyFuncItemDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UcodeService ucodeService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NxyFuncItemDao funcItemDao;
    @Autowired
    private TestProjectDao projectDao;
    @Autowired
    private NxyFuncConfigDao nxyFuncConfigDao;
    @Autowired
    private SimSysInsMessageDao simSysInsMessageDao;
    @Autowired
    private NxyFuncUsecaseDataDao nxyFuncUsecaseDataDao;
    @Autowired
    private NxyFuncUsecaseExpectedDao expectedDao;
    @Autowired
    private NxyFuncUsecaseDao nxyFuncUsecaseDao;
    @Autowired
    private NxyFuncUsecaseDataRuleDao ruleDao;
    @Value("${uno.pattern}")
    private String unoPattern;
    public String getUnoPattern() {
        return unoPattern;
    }

    //添加功能测试用例
    public Result addUsecase(NxyFuncUsecase usecase,  Long itemId) throws IOException {
        Result ret = new Result();
        //保存用例
        NxyFuncItem item = nxyFuncItemDao.findOne(itemId);
        if (item == null) {
            logger.info("功能测试项为空");
            ret.setSuccess(false);
            return ret;
        }
        String pattern = getUnoPattern();
        Bundle bundle = new Bundle();
        bundle.putString("tno", item.getTestProject().getAbb() + "");
        bundle.putString("ino", item.getName());
        String uno = ucodeService.format(pattern, bundle);
        usecase.setNo(uno);
        usecase.setSeqNo(usecase.getSeqNo()==null?0:usecase.getSeqNo());
        usecase.setNxyFuncItem(item);
        usecase.setCreateUser(userService.getCurrentUser().getUserName());
        usecase.setResult("noexec");
        usecase.setUsecaseNo(SeqNoUtils.getSeqNo("usecase_no", 10));
        nxyFuncUsecaseDao.save(usecase);
        //保存用例数据
        return ret;
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
        newItem.setType("k");
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
            newUc.setUsecaseNo(SeqNoUtils.getSeqNo("usecase_no", 10));
            newUc = nxyFuncUsecaseDao.save(newUc);
            uc.setUsecaseNo(newUc.getUsecaseNo());
            nxyFuncUsecaseDao.save(uc);
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
                    throw new RuntimeException("该项目实例下缺少" + ucData.getMessageCode() + "报文模板");
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
            newUc.setUsecaseNo(SeqNoUtils.getSeqNo("usecase_no", 10));
            newUc = nxyFuncUsecaseDao.save(newUc);
            uc.setUsecaseNo(newUc.getUsecaseNo());
            nxyFuncUsecaseDao.save(uc);
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
                    throw new RuntimeException("该项目实例下缺少" + ucData.getMessageCode() + "报文模板");
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

    public Result synchrodata(Long[] usecaseIds, String mark){
        Result ret = new Result();
        List<NxyFuncUsecase> usecaseList = nxyFuncUsecaseDao.findByUsecaseIdList(Arrays.asList(usecaseIds));
        StringBuffer sb = new StringBuffer("");
        for(NxyFuncUsecase usecase : usecaseList){
            if(StringUtils.isEmpty(usecase.getUsecaseNo())){
                sb.append(","+usecase.getNo());
            }
        }
        if(!sb.toString().isEmpty()){
            ret.setSuccess(false);
            ret.setMsg(sb.toString().substring(1)+"(备注：案例库中没有这些用例)");
            return ret;
        }
        Map<Long, SimSysInsMessage> map = new HashMap<>();
        for(NxyFuncUsecase usecase : usecaseList){
            NxyFuncUsecase usecase_k = nxyFuncUsecaseDao.findByUsecaseNo(usecase.getUsecaseNo(), mark);
            usecase_k.setCaseNumber(usecase.getCaseNumber());
            usecase_k.setPurpose(usecase.getPurpose());
            usecase_k.setSeqNo(usecase.getSeqNo());
            usecase_k.setStep(usecase.getStep());
            usecase_k.setExpected(usecase.getExpected());
            nxyFuncUsecaseDao.save(usecase_k);
            List<NxyFuncUsecaseData> dataList = nxyFuncUsecaseDataDao.findByUsecaseId(usecase.getId());
            List<NxyFuncUsecaseData> dataList_k = nxyFuncUsecaseDataDao.findByUsecaseId(usecase_k.getId());
            for(NxyFuncUsecaseData data : dataList_k){
                expectedDao.deleteByUsecaseDataId(data.getId());
                ruleDao.deleteByUsecaseDataId(data.getId());
                nxyFuncUsecaseDataDao.delete(data);
            }
            for (NxyFuncUsecaseData ucData:dataList){
                NxyFuncUsecaseData newUcData = new NxyFuncUsecaseData();
                newUcData.setResult("noexec");
                newUcData.setMessageCode(ucData.getMessageCode());
                newUcData.setMessageMessage(ucData.getMessageMessage());
                newUcData.setMessageName(ucData.getMessageName());
                newUcData.setNxyFuncUsecase(usecase_k);
                newUcData.setSeqNo(ucData.getSeqNo());
                SimSysInsMessage sm = map.get(ucData.getSimMessage().getId());
                if(sm == null){
                    sm = getSimMessage(map, ucData.getSimMessage(), usecase_k.getNxyFuncItem().getTestProject().getId(), null);
                }
                if(sm == null){
                    sm = ucData.getSimMessage();
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
                        smm = getSimMessage(map, ucData.getSimMessage(), usecase_k.getNxyFuncItem().getTestProject().getId(), null);
                    }
                    newRule.setSrcMsgId(smm.getId());
                    ruleDao.save(newRule);
                }
            }
        }
        return ret;
    }
}
