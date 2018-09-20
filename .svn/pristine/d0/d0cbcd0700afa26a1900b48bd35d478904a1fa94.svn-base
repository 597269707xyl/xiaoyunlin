package com.zdtech.platform.simserver.service;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.utils.SeqNoUtils;
import com.zdtech.platform.framework.utils.XmlDocHelper;
import com.zdtech.platform.framework.utils.XmlDocHelper4wl;
import com.zdtech.platform.simserver.net.config.ServerConfig;
import com.zdtech.platform.simserver.net.http.MsgSecurityHelper;
import com.zdtech.platform.simserver.net.http.XMLSignaturer;
import com.zdtech.platform.simserver.utils.Constants;
import com.zdtech.platform.simserver.jar.HttpUtil;
import com.zdtech.platform.simserver.utils.reflect.ReflectWraper;
import com.zdtech.platform.simserver.jar.EpccJarService;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * WLService
 *
 * @author panli
 * @date 2017/11/12
 */
@Service
public class EpccService {
    private static Logger logger = LoggerFactory.getLogger(EpccService.class);

    @Value("${epcc.seqno}")
    private String epccSeqnoPattern;
    @Value("${epcc.batchno}")
    private String epccBatchnoPattern;
    @Autowired
    private SysAdapterDao adapterDao;
    @Autowired
    private CommonService commonService;
    @Autowired
    private SimSysInsMessageDao simSysInsMessageDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SimSysInsReplyRuleDao replyRuleDao;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private SimSysInsMessageFieldDao simSysInsMessageFieldDao;
    @Autowired
    private NxyFuncUsecaseDataDao useCaseDataDao;
    @Autowired
    private NxyFuncConfigDao nxyFuncConfigDao;
    @Autowired
    private NxyFuncUsecaseDataRuleDao ruleDao;
    @Autowired
    private NxyFuncUsecaseExecSendDao sendDao;
    @Autowired
    private MsgFieldDao msgFieldDao;
    @Autowired
    private NxyFuncEpccMarkDao epccMarkDao;
    @Autowired
    private SimSysinsConfDao simSysinsConfDao;
    @Autowired
    private NxyFuncCaseRecvDao caseRecvDao;
    @Autowired
    private NxyFuncCaseSendDao caseSendDao;
    @Autowired
    private NxyFuncCaseMarkDao markDao;
    @Autowired
    private NxyFuncUsecaseExpectedDao expectedDao;
    @Autowired
    private NxyAsyncNoticeDao asyncNoticeDao;

    public String getDstInstgId(Long simInsId){
        String dstInstgId = simSysinsConfDao.findByInsIdAndConf(simInsId, "DstInstgId");
        return dstInstgId == null ? "" : dstInstgId;
    }

    public String buildAutoSendMsg(NxyFuncUsecaseData ucData,Long execId,Long batchId,Map<String,String> confs){
        Long itemId = ucData.getNxyFuncUsecase().getNxyFuncItem().getId();
        String msgCode = ucData.getMessageCode();
        String msgMsg = ucData.getMessageMessage();
        SimSysInsMessage msgModel = ucData.getSimMessage();
        Document doc = XmlDocHelper4wl.getXmlFromStr(msgMsg);
        //按照设置好的自动化生成规则设置报文域值，如果配置文件中配置了该域，则不修改
        List<SimSysInsMessageField> fields = getMessageFields(msgModel.getId());
        if(fields != null && fields.size() > 0){ //按照报文域设置的方法取值
            Long instanceId = msgModel.getSystemInstance().getId();
            for (SimSysInsMessageField field:fields){
                String name = field.getMsgField().getFieldId();
                String valueType = field.getRespValueType();
                String valueValue = field.getRespValue();
                if (Constants.MESSAGE_RESPONSE_FIELD_TYPE_INVOKE.equals(valueType)){
                    //如果该报文域设置了配置文件，就不按照方法获取值，用配置文件中值进行替换
                    if(XmlDocHelper4wl.getNodeValue(doc, name).startsWith("$")){
                        continue;
                    }
                    String value = ReflectWraper.invoke(valueValue, "instanceId:" + instanceId);
                    if (StringUtils.isNotEmpty(value)){
                        XmlDocHelper4wl.setNodeValue(doc,name,value);
                    }
                }
            }
            for (SimSysInsMessageField field:fields){
                String name = field.getMsgField().getFieldId();
                String valueType = field.getRespValueType();
                String valueValue = field.getRespValue();
                if(XmlDocHelper4wl.getNodeValue(doc, name).startsWith("$")){ //如果该报文域设置了配置文件，就不按照方法获取值，用配置文件中值进行替换
                    continue;
                }
                if(Constants.MESSAGE_RESPONSE_FIELD_TYPE_EQLVALUE.equals(valueType)){
                    XmlDocHelper4wl.setNodeValue(doc,name, XmlDocHelper4wl.getNodeValue(doc, valueValue));
                }
                if(Constants.MESSAGE_RESPONSE_FIELD_TYPE_DEFAULT.equals(valueType)){
                    XmlDocHelper4wl.setNodeValue(doc,name, valueValue);
                }
            }
        }
        //处理流水号：“8位日期”+“16位序列号”+“1位预留位”+“6位控制位”
        String seqno = "%s%s%s%s";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date());
        String no = nextSeqno("epccseqno");
        String y = "1";
        String ctr = "000001";
        seqno = String.format(seqno,date,no,y,ctr);
        if (msgCode.equalsIgnoreCase("epcc.101.001.01")||msgCode.equalsIgnoreCase("epcc.103.001.01")||msgCode.equalsIgnoreCase("epcc.104.001.01")||
                msgCode.equalsIgnoreCase("epcc.201.001.01")||msgCode.equalsIgnoreCase("epcc.205.001.01")||msgCode.equalsIgnoreCase("epcc.211.001.01")||
                msgCode.equalsIgnoreCase("epcc.208.001.01")||msgCode.equalsIgnoreCase("epcc.213.001.01")){
            XmlDocHelper4wl.setNodeValue(doc,"//TrxId",seqno);
        }
        //处理批次号，“B”+“8位日期”+“4位序号”
        String batchno = "%s%s%s";
        sdf = new SimpleDateFormat("yyyyMMdd");
        String date1 = sdf.format(new Date());
        String bno = nextBatchno("epccbatchno");
        if (msgCode.equalsIgnoreCase("epcc.201.001.01")||msgCode.equalsIgnoreCase("epcc.205.001.01")||msgCode.equalsIgnoreCase("epcc.211.001.01")||
                msgCode.equalsIgnoreCase("epcc.206.001.01")||msgCode.equalsIgnoreCase("epcc.207.001.01")||msgCode.equalsIgnoreCase("epcc.208.001.01")||
                msgCode.equalsIgnoreCase("epcc.213.001.01")){
            batchno = String.format(batchno,"B",date1,bno);
            XmlDocHelper4wl.setNodeValue(doc,"//BatchId",batchno);
        }
        if (msgCode.equalsIgnoreCase("epcc.324.001.01")){
            batchno = String.format(batchno,"D",date1,bno);
            XmlDocHelper4wl.setNodeValue(doc,"//DsptBatchId",batchno);
        }
        //处理两个仿真报文匹配流水号生成
        String field = epccMarkDao.getFieldIdByCodeAndStandard(msgCode,"wb");
        if("epcc.103.001.01".equals(msgCode)){
            String SgnAcctShrtId = sgnAcctShrtId();
            XmlDocHelper4wl.setNodeValue(doc,field,SgnAcctShrtId);
        } else {
            XmlDocHelper4wl.setNodeValue(doc,field,seqno);
        }
        //替换配置文件中配置的域
        String ret = doc.asXML();
        ret = buildAutoSendMsgConfig(itemId, ret);
        //更改报文业务相关数据
        ret = buildAutoSendBussMsg(ucData,execId,ret);
        //加签、加密等
        ret = encryptAndSign(ret,confs);
        return ret;
    }

    //epcc.103.001.01 设置4位的唯一标识
    public String sgnAcctShrtId(){

        EpccJarService epccJarService = new EpccJarService();
        String SgnAcctShrtId = epccJarService.getSgnAcctShrtId();
        String sql = "select count(*) from nxy_func_usecase_exec_send where epcc_ids='%s'";
        sql = String.format(sql, SgnAcctShrtId);
        Object obj = jdbcTemplate.queryForObject(sql, Object.class);
        if(obj != null && !"0".equals(obj.toString())){
            sgnAcctShrtId();
        }
        return SgnAcctShrtId;
    }

    //ucData-本步骤要发送的数据
    //execId-执行轮次
    //msg-报文数据
    public String buildAutoSendBussMsg(NxyFuncUsecaseData ucData, Long execId,String msg) {
        String ret = msg;
        //第一步报文直接返回
        if (execId == null){
            return ret;
        }
        String msgCode = ucData.getMessageCode();
        Integer step = ucData.getSeqNo();
        //本步骤用例数据是否依赖上一步数据
        List<NxyFuncUsecaseDataRule> rules = ruleDao.findByUcDataId(ucData.getId());
        if (rules == null || rules.size() < 1){
            return ret;
        }
        //找到上一步的发送和接收数据
        Integer lastSetp = step - 1;
        if (lastSetp < 1){
            return ret;
        }
        Document destDoc = XmlDocHelper4wl.getXmlFromStr(ret);
        for (NxyFuncUsecaseDataRule rule:rules){
            String sql = "SELECT id,msg,message_id FROM nxy_func_usecase_exec_send WHERE usecase_exec_id = ? AND message_id = ? AND step = ?";
            List<Map<String,Object>> sends = jdbcTemplate.queryForList(sql,execId,rule.getSrcMsgId(), rule.getEvaluateType());
            if (sends == null || sends.size() < 1){
                return ret;
            }
            Object osendId = sends.get(0).get("id");
            Object osendMsg = sends.get(0).get("msg");
            Long sendId = osendId==null?null:Long.parseLong(osendId.toString());
            String sendMsg = osendMsg==null?null:osendMsg.toString();
            if (sendId == null||sendMsg == null){
                continue;
            }
            Long sendSimMsgId = Long.parseLong(sends.get(0).get("message_id").toString());
            Document sendDoc = XmlDocHelper4wl.getXmlFromStr(sendMsg);
            sql = "SELECT message_message,message_id FROM nxy_func_usecase_exec_recv WHERE exec_send_id = ?";
            List<Map<String,Object>> recvs = jdbcTemplate.queryForList(sql,sendId);
            Document recvDoc = null;
            if (recvs != null && recvs.size() > 0){
                String recvMsg = recvs.get(0).get("message_message").toString();
                recvDoc = XmlDocHelper4wl.getXmlFromStr(recvMsg);
            }
            Long recvSimMsgId = Long.parseLong(sends.get(0).get("message_id").toString());
            String destFieldId = rule.getDestFieldId();
            String srcFieldId = rule.getSrcFieldId();
            String type = rule.getSrcSendRecv();
            String value = "";
            if (type.equalsIgnoreCase("send")){
                value = XmlDocHelper4wl.getNodeValue(sendDoc,srcFieldId);
                XmlDocHelper4wl.setNodeValue(destDoc,destFieldId,value);
            }else {
                value = XmlDocHelper4wl.getNodeValue(recvDoc,srcFieldId);
                XmlDocHelper4wl.setNodeValue(destDoc,destFieldId,value);
            }
        }
        return destDoc.asXML();
    }

//    public String buildAutoSendMsgConfig(Long itemId, String msg){
//        itemId = getItemIdByItemId(itemId);
//        if(itemId != null){
//            List<NxyFuncConfig> configList = nxyFuncConfigDao.findByItemId(itemId);
//            for(NxyFuncConfig config : configList){
//                msg = msg.replaceAll("\\" + config.getVariableEn()+"</", config.getVariableValue()+"</");
//            }
//        }
//        return msg;
//    }

    public String buildAutoSendMsgConfig(Long itemId, String msg){
        EpccJarService epccJarService = new EpccJarService();
        itemId = epccJarService.getItemIdByItemId(itemId);
        if(itemId != null){
            List<NxyFuncConfig> configList = nxyFuncConfigDao.findByItemId(itemId);
            Document doc = XmlDocHelper.getXmlFromStr(msg);
            Map<String, String> configMap = epccJarService.configToMap(configList);
            epccJarService.replaceConfig(doc.getRootElement(),configMap);
            return doc.asXML();
        }
        return msg;
    }


    //加签加密
    private String encryptAndSign(String src,Map<String,String> confs){
        if(!"true".equals(confs.get("nxy_is_sign"))){ //判断整体是否要加签加密
            return src;
        }
        String ret = src;
        try {
            Document document = XmlDocHelper4wl.getXmlFromStr(ret);
            ret = encrypt(document,confs);
            String signStr = sign(ret,confs);
            if (StringUtils.isNotEmpty(signStr)){
                Document doc = XmlDocHelper4wl.getXmlFromStr(ret);
                XmlDocHelper4wl.setNodeValue(doc,"/root/MsgHeader/SignSN",confs.get("ssn"));
                ret = String.format("%s{S:%s}",doc.asXML(),signStr);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("epcc加密错：{}",e.getMessage());
        }
        return ret;
    }


    private String encrypt(Document src,Map<String,String> confs){
        SimSysInsMessage ssim = getSysInsMessage(src);
        List<SimSysInsMessageField> fields = ssim.getMsgFields();
        //保存所有加密域，list<fieldId>
        List<String> list = new ArrayList<>();
        for (SimSysInsMessageField field:fields){
            if (field.getSignFlag() != null && field.getSignFlag()){
                String fieldId = field.getMsgField().getFieldId();
                if (fieldId.equalsIgnoreCase("/root/MsgHeader/DgtlEnvlp")){
                    continue;
                }
                list.add(fieldId);
            }
        }
        //未涉及敏感字段的报文，两个域不应有值
        if (list.size() < 1){
            XmlDocHelper4wl.setNodeValue(src,"root/MsgHeader/NcrptnSN","");
            XmlDocHelper4wl.setNodeValue(src,"root/MsgHeader/DgtlEnvlp","");
            return src.asXML();
        }
        return doEncrypt(src,list,confs);
    }

    private String doEncrypt(Document document, List<String> fieldList,Map<String,String> confs) {
        List<String> values = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        for (String fieldId:fieldList){
            String value = XmlDocHelper4wl.getNodeValue(document,fieldId);
            if (StringUtils.isNotEmpty(value)){
                values.add(value);
                ids.add(fieldId);
            }
        }
        String ip = confs.get("nxy_sign_ip");
        String sport = confs.get("nxy_sign_port");
        String pwd = confs.get("nxy_sign_pwd");
        String dn = confs.get("nxy_encrypt_dn");
        String alg = confs.get("alg");
        if (StringUtils.isEmpty(ip)||StringUtils.isEmpty(sport)||StringUtils.isEmpty(pwd)||StringUtils.isEmpty(dn)){
            return document.asXML();
        }
        Integer port = Integer.parseInt(sport);
        List<String> ret = MsgSecurityHelper.encrypt(values,ip,port,pwd,dn,alg);
        if (ret.size() > values.size()){
            XmlDocHelper4wl.setNodeValue(document,"/root/MsgHeader/DgtlEnvlp",ret.get(0));
            XmlDocHelper4wl.setNodeValue(document,"/root/MsgHeader/NcrptnSN",dn);
            for (int i = 0; i < values.size(); i++){
                XmlDocHelper4wl.setNodeValue(document,ids.get(i),ret.get(i+1));
            }
        }
        return document.asXML();
    }


    public String sign(String src,Map<String,String> confs) {
        String ip = confs.get("nxy_sign_ip");
        String sport = confs.get("nxy_sign_port");
        String pwd = confs.get("nxy_sign_pwd");
        String dn = confs.get("nxy_sign_dn");
        String alg = confs.get("alg");
        if (StringUtils.isEmpty(ip)||StringUtils.isEmpty(sport)||StringUtils.isEmpty(pwd)||StringUtils.isEmpty(dn)){
            return "";
        }
        Integer port = Integer.parseInt(sport);
        String ret = MsgSecurityHelper.sign(src,ip,port,pwd,dn,alg);
        return ret;
    }

    //删除签名串
    public String removeSignStr(String src){
        int index = src.indexOf("{S:");
        if (index > 0){
            src = src.substring(0,index);
        }
        return src;
    }

    public String saveSendData(Long batchId,Long execId,NxyFuncUsecaseData ucData,
                             String reqMsg,String result,String resultDetail){
        //更改用例状态
        String sql = "update nxy_func_usecase set result = ? where id = ?";
        jdbcTemplate.update(sql,result,ucData.getNxyFuncUsecase().getId());
        //更改用例本步骤状态
        sql = "UPDATE nxy_func_usecase_data SET result = ? WHERE id = ?";
        jdbcTemplate.update(sql,result,ucData.getId());
        //添加本轮次执行记录
        if (execId == null){
            sql = "insert into nxy_func_usecase_exec (usecase_id,result,exec_time,round,batch_id) " +
                    " select ?,?,?,(select IFNULL(MAX(t.round),0)+1 from nxy_func_usecase_exec t where t.usecase_id=?),?";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            final String finalSql = sql;
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(finalSql, PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setLong(1,ucData.getNxyFuncUsecase().getId());
                    ps.setString(2,result);
                    Date date = new Date();
                    Timestamp time = new Timestamp(date.getTime());
                    ps.setObject(3,time);
                    ps.setLong(4,ucData.getNxyFuncUsecase().getId());
                    ps.setObject(5,batchId);
                    return ps;
                }
            }, keyHolder);
            execId = keyHolder.getKey().longValue();
        }else {

        }
        //添加本轮次本步骤发送报文记录
        sql = "INSERT INTO `nxy_func_usecase_exec_send` " +
                "(`usecase_exec_id`, `msg`, `ids`, `message_id`, `message_name`, `message_code`, `step`, `result`,`time_stamp`,`result_descript`,`epcc_ids`, `msg_id`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?,?, ?)";
        String ids = getIdsOfMsg(reqMsg,"send");
        String epccIds = getEpccIdsOfMsg(reqMsg);
        Document send = XmlDocHelper4wl.getXmlFromStr(reqMsg);

        String trxId = "请求报文("+ XmlDocHelper4wl.getNodeValue(send, "/root/MsgHeader/MsgTp") +")流水号：" + ids;
        resultDetail = resultDetail + "<br/>" + trxId;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String finalSql1 = sql;
        final Long finalExecId = execId;
        final String finalReqMsg = reqMsg;
        final String finalResultDetail = resultDetail;
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(finalSql1, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setLong(1, finalExecId);
                ps.setString(2, finalReqMsg);
                ps.setString(3,ids);
                ps.setLong(4,ucData.getSimMessage().getId());
                ps.setString(5,ucData.getMessageName());
                ps.setString(6,ucData.getMessageCode());
                ps.setInt(7,ucData.getSeqNo());
                ps.setString(8,result);
                ps.setLong(9,System.currentTimeMillis());
                ps.setString(10, finalResultDetail);
                ps.setString(11,epccIds);
                ps.setString(12,ids);
                return ps;
            }
        }, keyHolder);
        Long sendId = keyHolder.getKey().longValue();
        return execId + ":" + sendId;
    }

    public String saveAutoExecData(Long simInsId,Long execId,NxyFuncUsecaseData ucData,
                                 String reqMsg,String respMsg,String result,String resultDetail, Long sendId) {

        respMsg = removeSignStr(respMsg);
        //只有发送成功并受到应答后才做如下处理
        //sendId,execId,ucId=ucData.getNxyFuncUsecase().getId(),step=ucData.getSeqNo()
        if (result.equalsIgnoreCase("execsucc")){
            //保存应答报文
            SimSysInsMessage respModel = getSysInsMessage(simInsId,respMsg);
            Long respMsgId = respModel==null?null:respModel.getId();
            String respMsgName = respModel==null?"":respModel.getName();
            String respMsgCode = respModel==null?"":respModel.getMsgType();
            String sql = "INSERT INTO `nxy_func_usecase_exec_recv` " +
                    "(`exec_send_id`, `message_id`, `message_name`, `message_code`, `message_message`) " +
                    "VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,sendId,respMsgId,respMsgName,respMsgCode,respMsg);
            //计算执行结果
            String expectStatus = getExpectStatus(sendId,respMsg);
            //更新nxy_func_usecase，nxy_func_usecase_data，nxy_func_usecase_exec，nxy_func_usecase_exec_send
            String detail = "";
            String rst = "";
            if(expectStatus.startsWith("expected")){
                detail = "应答报文符合预期" + expectStatus.substring(8);
                rst = "expected";
            }
            if(expectStatus.startsWith("noexpected")){
                detail = "应答报文不符合预期" + expectStatus.substring(10);
                rst = "noexpected";
            }
            sql = "UPDATE nxy_func_usecase_exec_send SET result = ?,result_descript=? WHERE id = ?";
            jdbcTemplate.update(sql,rst,detail,sendId);
            //保存本次执行结果
            sql = "UPDATE nxy_func_usecase_exec SET result = ? WHERE id = ?";
            jdbcTemplate.update(sql,rst,execId);
            //保存本用例本步骤结果
            sql = "UPDATE nxy_func_usecase_data SET result = ? WHERE usecase_id = ? AND seq_no = ?";
            jdbcTemplate.update(sql,rst,ucData.getNxyFuncUsecase().getId(),ucData.getSeqNo());
            //保存本用例执行结果
            sql = "UPDATE nxy_func_usecase SET result = ? WHERE id = ?";
            jdbcTemplate.update(sql,rst,ucData.getNxyFuncUsecase().getId());
            //本步骤符合预期，返回下一步报文：执行id_下一步报文id
            if (rst.equalsIgnoreCase("expected")){
                sql = "SELECT id FROM nxy_func_usecase_data WHERE usecase_id = ? AND seq_no = ?";
                List<Map<String,Object>> ll = jdbcTemplate.queryForList(sql,ucData.getNxyFuncUsecase().getId(),ucData.getSeqNo()+1);
                if (ll == null || ll.size() < 1){
                    return null;
                }else {
                    return String.format("%d_%s",execId,ll.get(0).get("id").toString());
                }
            }
        } else {
            reqMsg = removeSignStr(reqMsg);
            //更改用例状态
            String sql = "update nxy_func_usecase set result = ? where id = ?";
            jdbcTemplate.update(sql,result,ucData.getNxyFuncUsecase().getId());
            //更改用例本步骤状态
            sql = "UPDATE nxy_func_usecase_data SET result = ? WHERE id = ?";
            jdbcTemplate.update(sql,result,ucData.getId());
            //添加本轮次执行记录
            sql = "UPDATE nxy_func_usecase_exec SET result = ? WHERE id = ?";
            jdbcTemplate.update(sql,result,execId);
            sql = "UPDATE nxy_func_usecase_exec_send SET result = ?, result_descript = ? WHERE id = ?";
            String ids = getIdsOfMsg(reqMsg,"send");
            String epccIds = getEpccIdsOfMsg(reqMsg);
            Document send = XmlDocHelper4wl.getXmlFromStr(reqMsg);

            String trxId = "请求报文("+ XmlDocHelper4wl.getNodeValue(send, "/root/MsgHeader/MsgTp") +")流水号：" + ids;
            resultDetail = resultDetail + "<br/>" + trxId;
            jdbcTemplate.update(sql, result, resultDetail, sendId);
        }
        return null;
    }

    private String getExpectStatus(Long sendId, String respMsg) {
        Document recv = XmlDocHelper4wl.getXmlFromStr(respMsg);
        NxyFuncUsecaseExecSend sendData = sendDao.findOne(sendId);
        Document send = XmlDocHelper4wl.getXmlFromStr(sendData.getMsg());

        String seqNb = "请求报文("+ XmlDocHelper4wl.getNodeValue(send, "/root/MsgHeader/MsgTp") +")流水号：" +
                XmlDocHelper4wl.getNodeValue(send, "//TrxId") + "，应答报文("+ XmlDocHelper4wl.getNodeValue(recv, "/root/MsgHeader/MsgTp") +")流水号：" +
                XmlDocHelper4wl.getNodeValue(send, "//TrxId");

        Integer step = sendData.getStep();
        Long ucId = sendData.getNxyFuncUsecaseExec().getNxyFuncUsecase().getId();
        String sql = "SELECT field_id,field_id_name,expected_value FROM nxy_func_usecase_expected WHERE type='expect' AND usecase_data_id in (SELECT id FROM nxy_func_usecase_data WHERE usecase_id=? AND seq_no=?)";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,ucId,step);
        if (list == null || list.size() < 1) {
            return "expected";
        }
        String ret = "expected";
        String noexpected = "";
        String expected = "";
        for (Map<String, Object> e : list) {
            String fieldId = e.get("field_id").toString();
            MsgField msgField = msgFieldDao.findOne(Long.parseLong(fieldId));
            String nameZh = msgField.getNameZh();
            String id = e.get("field_id_name").toString();
            String value = e.get("expected_value").toString();
            String realValue = XmlDocHelper4wl.getNodeValue(recv,id);
            if (StringUtils.isEmpty(realValue) || !value.contains(realValue)) {
                noexpected += "<br/>预期返回结果：" + nameZh + "(" + value + "), 实际返回结果：" + nameZh + "(" + realValue + ")";
            } else {
                expected += "<br/>预期返回结果：" + nameZh + "(" + value + ")";
            }

        }
        if(!noexpected.isEmpty()){
            noexpected = "noexpected<br/>" + seqNb + noexpected;
            return noexpected;
        }
        if(!expected.isEmpty()){
            expected = "expected<br/>" + seqNb + expected;
            return expected;
        }
        return ret;
    }
    public void saveAutoExecData(Long batchId,Long execId,NxyFuncUsecaseData ucData, String strMsg,String result,String resultDetail) {
        //更改用例状态
        String sql = "update nxy_func_usecase set result = ? where id = ?";
        jdbcTemplate.update(sql,result,ucData.getNxyFuncUsecase().getId());
        //更改用例本步骤状态
        sql = "UPDATE nxy_func_usecase_data SET result = ? WHERE id = ?";
        jdbcTemplate.update(sql,result,ucData.getId());
        //添加本轮次执行记录
        if (execId == null){
            sql = "insert into nxy_func_usecase_exec (usecase_id,result,exec_time,round,batch_id) " +
                    " select ?,?,?,(select IFNULL(MAX(t.round),0)+1 from nxy_func_usecase_exec t where t.usecase_id=?),?";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            final String finalSql = sql;
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(finalSql, PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setLong(1,ucData.getNxyFuncUsecase().getId());
                    ps.setString(2,result);
                    Date date = new Date();
                    Timestamp time = new Timestamp(date.getTime());
                    ps.setObject(3,time);
                    ps.setLong(4,ucData.getNxyFuncUsecase().getId());
                    ps.setObject(5,batchId);
                    return ps;
                }
            }, keyHolder);
            execId = keyHolder.getKey().longValue();
        }else {
            sql = "UPDATE nxy_func_usecase_exec SET result = ? WHERE id = ?";
            jdbcTemplate.update(sql,result,execId);
        }
        //添加本轮次本步骤发送报文记录
        sql = "INSERT INTO `nxy_func_usecase_exec_send` " +
                "(`usecase_exec_id`, `msg`, `ids`, `message_id`, `message_name`, `message_code`, `step`, `result`,`time_stamp`,`result_descript`,`epcc_ids`, `msg_id`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?,?, ?)";
        String ids = getIdsOfMsg(strMsg,"send");
        Document send = XmlDocHelper4wl.getXmlFromStr(strMsg);

        String trxId = "请求报文("+ XmlDocHelper4wl.getNodeValue(send, "/root/MsgHeader/MsgTp") +")流水号：" + ids;
        resultDetail = resultDetail + "<br/>" + trxId;
        jdbcTemplate.update(sql,execId,strMsg,ids,ucData.getSimMessage().getId(),ucData.getMessageName(),ucData.getMessageCode(),ucData.getSeqNo(),result,System.currentTimeMillis(),resultDetail,ids);
    }

    public String getIdsOfMsg(String strMsg,String type){
        String ret = "";
        Document doc = XmlDocHelper4wl.getXmlFromStr(strMsg);
        if (type.equalsIgnoreCase("send")){
            ret += XmlDocHelper4wl.getNodeValue(doc,"//TrxId");
            logger.info("========================={}",ret);
        }else {
            ret += XmlDocHelper4wl.getNodeValue(doc,"//TrxId");
            logger.info("========================={}",ret);
        }
        return ret;
    }
    public String getEpccIdsOfMsg(String strMsg){
        Document doc = XmlDocHelper4wl.getXmlFromStr(strMsg);
        String msgCode = XmlDocHelper4wl.getNodeValue(doc,"/root/MsgHeader/MsgTp");
        String field = epccMarkDao.getFieldIdByCodeAndStandard(msgCode,"wb");
        String ret = XmlDocHelper4wl.getNodeValue(doc,field);
        logger.info("========================={}",ret);
        return ret;
    }

    public SimSysInsMessage getSysInsMessage(Long simInsId, String msg) {
        if (StringUtils.isEmpty(msg)){
            return null;
        }
        Document doc = XmlDocHelper4wl.getXmlFromStr(msg);
        String code = XmlDocHelper4wl.getNodeValue(doc,"root/MsgHeader/MsgTp");
        List<SimSysInsMessage> list = simSysInsMessageDao.findMessageByMsgType(simInsId,code);
        return list.size()>0?list.get(0):null;
    }

    public SimSysInsMessage getSysInsMessage(String msg) {
        Document doc = XmlDocHelper4wl.getXmlFromStr(msg);
        String code = XmlDocHelper4wl.getNodeValue(doc,"root/MsgHeader/MsgTp");
        List<SimSysInsMessage> list = simSysInsMessageDao.findMessageByMsgType(code);
        return list.size()>0?list.get(0):null;
    }

    public SimSysInsMessage getSysInsMessage(Document msgDoc) {
        String code = XmlDocHelper4wl.getNodeValue(msgDoc,"root/MsgHeader/MsgTp");
        List<SimSysInsMessage> list = simSysInsMessageDao.findMessageByMsgType(code);
        return list.size()>0?list.get(0):null;
    }

    public SimSysInsMessage getSysInsMessageByMsgCode(String code){
        List<SimSysInsMessage> list = simSysInsMessageDao.findMessageByMsgType(code);
        return list.size()>0?list.get(0):null;
    }

    public void logNotify(Long id){
        String url = commonService.getLogUrl();
        url = url+String.format("?id=%d",id);
        final String finalUrl = url;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.doPost(finalUrl);
            }
        });
        t.start();
    }

    public NxyFuncUsecaseData getUseCaseData(Long ucId, Integer seqNo) {
        List<NxyFuncUsecaseData> list = useCaseDataDao.findNextUseCaseData(ucId,seqNo);
        if (list == null || list.size() < 1){
            return null;
        }
        return list.get(0);
    }

    public NxyFuncUsecaseData getUseCaseData(Long id){
        return useCaseDataDao.findOne(id);
    }

    public List<SimSysInsMessageField> getMessageFields(Long simSysInsMessageId){
        return simSysInsMessageFieldDao.findByid(simSysInsMessageId);
    }
    private String nextSeqno(String key){
//        synchronized (EpccService.class) {
//            Bundle bundle = new Bundle();
//            bundle.putString(key, key);
//            String pattern = epccSeqnoPattern;
//            String no = ucodeService.format(pattern, bundle);
//            int length = no.length();
//            String no1 = no.trim();
//            int length1 = no1.length();
//            if (length != length1) {
//                no = StringUtils.leftPad(no1, length, '0');
//            }
//            return no;
//        }
        String no = SeqNoUtils.getSeqNo("epccseqno", 10);
        return no;
    }

    private String nextBatchno(String key) {
//        Bundle bundle = new Bundle();
//        bundle.putString(key,key);
//        String pattern = epccBatchnoPattern;
//        String no = ucodeService.format(pattern,bundle);
//        return no;
        String no = SeqNoUtils.getSeqNo("epccbatchno", 4);
        return no;
    }

    public Object[] getReplyMessage(Long msgId, String caseNo) {
        Object[] arr = new Object[3];
        List<SimSysInsMessage> replys = replyRuleDao.findRespMsgsByReqMsg(msgId);
        if(replys.isEmpty()) return null;
        for(SimSysInsMessage insMessage : replys){
            //根据案例编号和报文编号获取应答报文信息
            logger.info("应答报文编号：{}，来账案例编号：{}，应答报文Id：{}", insMessage.getMsgType(), caseNo, insMessage.getId());
            List<NxyFuncUsecaseData> list = useCaseDataDao.findByMessageCodeAndCaseNo(insMessage.getMsgType(), caseNo, insMessage.getId());
            if(!list.isEmpty()){
                arr[0] = list.get(0).getMessageMessage();
                arr[1] = insMessage;
                arr[2] = list.get(0).getId();
                return arr;
            }
        }
        return null;
    }


    private String getFixLenthString(int strLength) {
        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }

    //支付宝、财付通业务报文组装
    public String buildZTAutoSendMsg(NxyFuncUsecaseData ucData,Long execId,Long batchId,Map<String,String> confs){
        Long itemId = ucData.getNxyFuncUsecase().getNxyFuncItem().getId();
        String msgCode = ucData.getMessageCode();
        String msgMsg = ucData.getMessageMessage();
        SimSysInsMessage msgModel = ucData.getSimMessage();
        Document doc = XmlDocHelper4wl.getXmlFromStr(msgMsg);
        //按照设置好的自动化生成规则设置报文域值，如果配置文件中配置了该域，则不修改
        List<SimSysInsMessageField> fields = getMessageFields(msgModel.getId());
        if(fields != null && fields.size() > 0){ //按照报文域设置的方法取值
            Long instanceId = msgModel.getSystemInstance().getId();
            for (SimSysInsMessageField field:fields){
                String name = field.getMsgField().getFieldId();
                String valueType = field.getRespValueType();
                String valueValue = field.getRespValue();
                if (Constants.MESSAGE_RESPONSE_FIELD_TYPE_INVOKE.equals(valueType)){
                    //如果该报文域设置了配置文件，就不按照方法获取值，用配置文件中值进行替换
                    if(XmlDocHelper4wl.getNodeValue(doc, name).startsWith("$")){
                        continue;
                    }
                    String value = ReflectWraper.invoke(valueValue, "instanceId:" + instanceId);
                    if (StringUtils.isNotEmpty(value)){
                        XmlDocHelper4wl.setNodeValue(doc,name,value);
                    }
                }
            }
            for (SimSysInsMessageField field:fields){
                String name = field.getMsgField().getFieldId();
                String valueType = field.getRespValueType();
                String valueValue = field.getRespValue();
                if(XmlDocHelper4wl.getNodeValue(doc, name).startsWith("$")){ //如果该报文域设置了配置文件，就不按照方法获取值，用配置文件中值进行替换
                    continue;
                }
                if(Constants.MESSAGE_RESPONSE_FIELD_TYPE_EQLVALUE.equals(valueType)){
                    XmlDocHelper4wl.setNodeValue(doc,name, XmlDocHelper4wl.getNodeValue(doc, valueValue));
                }
                if(Constants.MESSAGE_RESPONSE_FIELD_TYPE_DEFAULT.equals(valueType)){
                    XmlDocHelper4wl.setNodeValue(doc,name, valueValue);
                }
            }
        }
        //处理流水号：“8位日期”+“16位序列号”
        String seqno = "%s%s";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date());
        String no = nextSeqno("epccseqno");
        seqno = String.format(seqno,date,no);
        List<Element> elements = XmlDocHelper4wl.getNodeElements(doc, "//Message");
        Element element = elements.get(0);
        Attribute attribute = element.attribute("id");
        attribute.setValue(seqno);
        elements = XmlDocHelper4wl.getNodeElements(doc, "//serialNo");
        if(!elements.isEmpty()){ //存在流水号域
            no = nextSeqno("epccseqno");
            seqno = String.format(seqno,date,no);
            XmlDocHelper4wl.setNodeValue(doc, "//serialNo", seqno);
        }
        //处理两个仿真报文匹配流水号生成
        String standard = ucData.getSimMessage().getStandard();
        String field = epccMarkDao.getFieldIdByCodeAndStandard(msgCode,standard);
        XmlDocHelper4wl.setNodeValue(doc,field,seqno);
        //替换配置文件中配置的域
        String ret = doc.asXML();
        ret = buildAutoSendMsgConfig(itemId, ret);
        //更改报文业务相关数据
        ret = buildAutoSendBussMsg(ucData,execId,ret);
        //加签、加密等
        ret = encryptAndSignZT(ret, msgCode, confs);
        return ret;
    }

    //加签加密
    private String encryptAndSignZT(String src, String msgCode, Map<String,String> confs){
        if(!"true".equals(confs.get("nxy_is_sign"))){ //判断整体是否要加签加密
            return src;
        }
        String ret = src;
        try {
            String privateKey = confs.get("private_key");
            String password = confs.get("private_password");
            if(StringUtils.isEmpty(privateKey) || StringUtils.isEmpty(password)){
                logger.info("支付宝或者财付通加签参数错误：privateKey={}，password={}", privateKey, password);
                return ret;
            }
            ret = XMLSignaturer.sign(ret, msgCode, privateKey, password);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("支付宝或者财付通加签错：{}",e.getMessage());
        }
        return ret;
    }

    public void saveZTAutoExecData(Long batchId,Long execId,NxyFuncUsecaseData ucData, String strMsg,String result,String resultDetail) {
        //更改用例状态
        String sql = "update nxy_func_usecase set result = ? where id = ?";
        jdbcTemplate.update(sql,result,ucData.getNxyFuncUsecase().getId());
        //更改用例本步骤状态
        sql = "UPDATE nxy_func_usecase_data SET result = ? WHERE id = ?";
        jdbcTemplate.update(sql,result,ucData.getId());
        //添加本轮次执行记录
        if (execId == null){
            sql = "insert into nxy_func_usecase_exec (usecase_id,result,exec_time,round,batch_id) " +
                    " select ?,?,?,(select IFNULL(MAX(t.round),0)+1 from nxy_func_usecase_exec t where t.usecase_id=?),?";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            final String finalSql = sql;
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(finalSql, PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setLong(1,ucData.getNxyFuncUsecase().getId());
                    ps.setString(2,result);
                    Date date = new Date();
                    Timestamp time = new Timestamp(date.getTime());
                    ps.setObject(3,time);
                    ps.setLong(4,ucData.getNxyFuncUsecase().getId());
                    ps.setObject(5,batchId);
                    return ps;
                }
            }, keyHolder);
            execId = keyHolder.getKey().longValue();
        }else {
            sql = "UPDATE nxy_func_usecase_exec SET result = ? WHERE id = ?";
            jdbcTemplate.update(sql,result,execId);
        }
        //添加本轮次本步骤发送报文记录
        sql = "INSERT INTO `nxy_func_usecase_exec_send` " +
                "(`usecase_exec_id`, `msg`, `ids`, `message_id`, `message_name`, `message_code`, `step`, `result`,`time_stamp`,`result_descript`,`epcc_ids`, `msg_id`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?,?, ?)";
        String ids = getIdsOfMsgZT(strMsg,"send");
        Document send = XmlDocHelper4wl.getXmlFromStr(strMsg);

        String trxId = "请求报文("+ ucData.getSimMessage().getMsgType() +") Id号：" + ids;
        resultDetail = resultDetail + "<br/>" + trxId;
        jdbcTemplate.update(sql,execId,strMsg,ids,ucData.getSimMessage().getId(),ucData.getMessageName(),ucData.getMessageCode(),ucData.getSeqNo(),result,System.currentTimeMillis(),resultDetail,ids);
    }

    public String getIdsOfMsgZT(String strMsg,String type){
        String ret = "";
        Document doc = XmlDocHelper4wl.getXmlFromStr(strMsg);
        List<Element> elements = XmlDocHelper4wl.getNodeElements(doc, "//Message");
        Element element = elements.get(0);
        Attribute attribute = element.attribute("id");
        if (type.equalsIgnoreCase("send")){
            ret += attribute.getValue();
            logger.info("========================={}",ret);
        }else {
            ret += attribute.getValue();
            logger.info("========================={}",ret);
        }
        return ret;
    }

    public String saveZTSendData(Long batchId,Long execId,NxyFuncUsecaseData ucData,
                               String reqMsg,String result,String resultDetail){
        //更改用例状态
        String sql = "update nxy_func_usecase set result = ? where id = ?";
        jdbcTemplate.update(sql,result,ucData.getNxyFuncUsecase().getId());
        //更改用例本步骤状态
        sql = "UPDATE nxy_func_usecase_data SET result = ? WHERE id = ?";
        jdbcTemplate.update(sql,result,ucData.getId());
        //添加本轮次执行记录
        if (execId == null){
            sql = "insert into nxy_func_usecase_exec (usecase_id,result,exec_time,round,batch_id) " +
                    " select ?,?,?,(select IFNULL(MAX(t.round),0)+1 from nxy_func_usecase_exec t where t.usecase_id=?),?";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            final String finalSql = sql;
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(finalSql, PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setLong(1,ucData.getNxyFuncUsecase().getId());
                    ps.setString(2,result);
                    Date date = new Date();
                    Timestamp time = new Timestamp(date.getTime());
                    ps.setObject(3,time);
                    ps.setLong(4,ucData.getNxyFuncUsecase().getId());
                    ps.setObject(5,batchId);
                    return ps;
                }
            }, keyHolder);
            execId = keyHolder.getKey().longValue();
        }else {

        }
        //添加本轮次本步骤发送报文记录
        sql = "INSERT INTO `nxy_func_usecase_exec_send` " +
                "(`usecase_exec_id`, `msg`, `ids`, `message_id`, `message_name`, `message_code`, `step`, `result`,`time_stamp`,`result_descript`,`epcc_ids`, `msg_id`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?,?, ?)";
        String ids = getIdsOfMsgZT(reqMsg,"send");
        String msgCode = ucData.getSimMessage().getMsgType();
        String standard = ucData.getSimMessage().getStandard();
        String epccIds = getEpccIdsOfMsgZT(reqMsg, msgCode, standard);
        Document send = XmlDocHelper4wl.getXmlFromStr(reqMsg);

        String trxId = "请求报文("+ msgCode +") Id号：" + ids;
        resultDetail = resultDetail + "<br/>" + trxId;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String finalSql1 = sql;
        final Long finalExecId = execId;
        final String finalReqMsg = reqMsg;
        final String finalResultDetail = resultDetail;
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(finalSql1, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setLong(1, finalExecId);
                ps.setString(2, finalReqMsg);
                ps.setString(3,ids);
                ps.setLong(4,ucData.getSimMessage().getId());
                ps.setString(5,ucData.getMessageName());
                ps.setString(6,ucData.getMessageCode());
                ps.setInt(7,ucData.getSeqNo());
                ps.setString(8,result);
                ps.setLong(9,System.currentTimeMillis());
                ps.setString(10, finalResultDetail);
                ps.setString(11,epccIds);
                return ps;
            }
        }, keyHolder);
        Long sendId = keyHolder.getKey().longValue();
        return execId + ":" + sendId;
    }

    public String getEpccIdsOfMsgZT(String strMsg, String msgCode, String stand){
        Document doc = XmlDocHelper4wl.getXmlFromStr(strMsg);
        String field = epccMarkDao.getFieldIdByCodeAndStandard(msgCode,stand);
        String ret = XmlDocHelper4wl.getNodeValue(doc,field);
        logger.info("========================={}",ret);
        return ret;
    }

    public String saveZTAutoExecData(Long simInsId,Long execId,NxyFuncUsecaseData ucData,
                                   String reqMsg,String respMsg,String result,String resultDetail, Long sendId) {

        //只有发送成功并受到应答后才做如下处理
        String msgCode = ucData.getSimMessage().getMsgType();
        if (result.equalsIgnoreCase("execsucc")){
            //保存应答报文
            SimSysInsMessage respModel = getSysInsMessage(simInsId,respMsg);
            Long respMsgId = respModel==null?null:respModel.getId();
            String respMsgName = respModel==null?"":respModel.getName();
            String respMsgCode = respModel==null?"":respModel.getMsgType();
            if(StringUtils.isEmpty(respMsgCode)){
                respMsgCode = msgCode.substring(0, msgCode.length()-1) + "s";
            }
            String sql = "INSERT INTO `nxy_func_usecase_exec_recv` " +
                    "(`exec_send_id`, `message_id`, `message_name`, `message_code`, `message_message`) " +
                    "VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,sendId,respMsgId,respMsgName,respMsgCode,respMsg);
            //计算执行结果
            String expectStatus = getZTExpectStatus(sendId,respMsg,msgCode, respMsgCode);
            //更新nxy_func_usecase，nxy_func_usecase_data，nxy_func_usecase_exec，nxy_func_usecase_exec_send
            String detail = "";
            String rst = "";
            if(expectStatus.startsWith("expected")){
                detail = "应答报文符合预期" + expectStatus.substring(8);
                rst = "expected";
            }
            if(expectStatus.startsWith("noexpected")){
                detail = "应答报文不符合预期" + expectStatus.substring(10);
                rst = "noexpected";
            }
            sql = "UPDATE nxy_func_usecase_exec_send SET result = ?,result_descript=? WHERE id = ?";
            jdbcTemplate.update(sql,rst,detail,sendId);
            //保存本次执行结果
            sql = "UPDATE nxy_func_usecase_exec SET result = ? WHERE id = ?";
            jdbcTemplate.update(sql,rst,execId);
            //保存本用例本步骤结果
            sql = "UPDATE nxy_func_usecase_data SET result = ? WHERE usecase_id = ? AND seq_no = ?";
            jdbcTemplate.update(sql,rst,ucData.getNxyFuncUsecase().getId(),ucData.getSeqNo());
            //保存本用例执行结果
            sql = "UPDATE nxy_func_usecase SET result = ? WHERE id = ?";
            jdbcTemplate.update(sql,rst,ucData.getNxyFuncUsecase().getId());
            //本步骤符合预期，返回下一步报文：执行id_下一步报文id
            if (rst.equalsIgnoreCase("expected")){
                sql = "SELECT id FROM nxy_func_usecase_data WHERE usecase_id = ? AND seq_no = ?";
                List<Map<String,Object>> ll = jdbcTemplate.queryForList(sql,ucData.getNxyFuncUsecase().getId(),ucData.getSeqNo()+1);
                if (ll == null || ll.size() < 1){
                    return null;
                }else {
                    return String.format("%d_%s",execId,ll.get(0).get("id").toString());
                }
            }
        } else {
            //更改用例状态
            String sql = "update nxy_func_usecase set result = ? where id = ?";
            jdbcTemplate.update(sql,result,ucData.getNxyFuncUsecase().getId());
            //更改用例本步骤状态
            sql = "UPDATE nxy_func_usecase_data SET result = ? WHERE id = ?";
            jdbcTemplate.update(sql,result,ucData.getId());
            //添加本轮次执行记录
            sql = "UPDATE nxy_func_usecase_exec SET result = ? WHERE id = ?";
            jdbcTemplate.update(sql,result,execId);
            sql = "UPDATE nxy_func_usecase_exec_send SET result = ?, result_descript = ? WHERE id = ?";
            String ids = getIdsOfMsgZT(reqMsg,"send");
            String trxId = "请求报文("+ msgCode +") Id号：" + ids;
            resultDetail = resultDetail + "<br/>" + trxId;
            jdbcTemplate.update(sql, result, resultDetail, sendId);
        }
        return null;
    }

    private String getZTExpectStatus(Long sendId, String respMsg, String msgCode, String respMsgCode) {
        Document recv = XmlDocHelper4wl.getXmlFromStr(respMsg);
        NxyFuncUsecaseExecSend sendData = sendDao.findOne(sendId);
        Document send = XmlDocHelper4wl.getXmlFromStr(sendData.getMsg());
        List<Element> elements = XmlDocHelper4wl.getNodeElements(send, "//Message");
        Element element = elements.get(0);
        Attribute attribute = element.attribute("id");
        String ids = attribute.getValue();
        String seqNb = "请求报文("+ msgCode +") Id号：" + ids + "，应答报文("+respMsgCode +") Id水号：" + ids;

        Integer step = sendData.getStep();
        Long ucId = sendData.getNxyFuncUsecaseExec().getNxyFuncUsecase().getId();
        String sql = "SELECT field_id,field_id_name,expected_value FROM nxy_func_usecase_expected WHERE type='expect' AND usecase_data_id in (SELECT id FROM nxy_func_usecase_data WHERE usecase_id=? AND seq_no=?)";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,ucId,step);
        if (list == null || list.size() < 1) {
            return "expected";
        }
        String ret = "expected";
        String noexpected = "";
        String expected = "";
        for (Map<String, Object> e : list) {
            String fieldId = e.get("field_id").toString();
            MsgField msgField = msgFieldDao.findOne(Long.parseLong(fieldId));
            String nameZh = msgField.getNameZh();
            String id = e.get("field_id_name").toString();
            String value = e.get("expected_value").toString();
            String realValue = XmlDocHelper4wl.getNodeValue(recv,id);
            if (StringUtils.isEmpty(realValue) || !value.contains(realValue)) {
                noexpected += "<br/>预期返回结果：" + nameZh + "(" + value + "), 实际返回结果：" + nameZh + "(" + realValue + ")";
            } else {
                expected += "<br/>预期返回结果：" + nameZh + "(" + value + ")";
            }

        }
        if(!noexpected.isEmpty()){
            noexpected = "noexpected<br/>" + seqNb + noexpected;
            return noexpected;
        }
        if(!expected.isEmpty()){
            expected = "expected<br/>" + seqNb + expected;
            return expected;
        }
        return ret;
    }

    public Long saveRecvMessage(SimSysInsMessage insMessage, Document recvDoc, String no){
        //保存来账信息
        NxyFuncCaseRecv caseRecv = new NxyFuncCaseRecv();
        caseRecv.setBankNo(XmlDocHelper4wl.getNodeValue(recvDoc, "//IssrId"));
        caseRecv.setCaseNo(no);
        caseRecv.setCreateTime(new Date());
        caseRecv.setMsgSeqNo(XmlDocHelper4wl.getNodeValue(recvDoc, "//TrxId"));
        caseRecv.setMsgName(insMessage.getName());
        caseRecv.setMsgCode(insMessage.getMsgType());
        caseRecv.setMessage(recvDoc.asXML());
        caseRecvDao.save(caseRecv);
        return caseRecv.getId();
    }

    public void updateRecvMessage(Long recvId, String no, int status){
        //保存来账信息
        no = no==null?"":no;
        String sql = "update nxy_func_case_recv set case_no=?, status=? where id=?";
        System.out.println(String.format("update nxy_func_case_recv set case_no='%s', status=%s where id=%s", no, status, recvId));
        jdbcTemplate.update(sql, no, status, recvId);
    }

    public String getFuncCaseMark(String msgCode){
        return markDao.getFieldIdByCodeAndStandard(msgCode, "wb");
    }

    public String getReplyMessageStr(Object caseMsgObj, Object insMessage, Object ucDataIdObj, Document recvDoc){
        String caseMsg = (String) caseMsgObj;
        SimSysInsMessage model = (SimSysInsMessage) insMessage;
        Long ucDataId = (Long) ucDataIdObj;
        List<SimSysInsMessageField> fields = simSysInsMessageFieldDao.findByMessageId(model.getId());
        Document doc = XmlDocHelper4wl.getXmlFromStr(caseMsg);
        Document modelDoc = XmlDocHelper4wl.getXmlFromStr(model.getModelFileContent());
        for (SimSysInsMessageField field:fields){ //相同域值赋值
            String fieldId = field.getMsgField().getFieldId();
            if (fieldId.equalsIgnoreCase("/root/MsgBody/CtrlNbInf/CtrlNbInfLst/CtrlNbF")){
                XmlDocHelper4wl.setNodeValue(modelDoc,fieldId, "10");
                continue;
            }
            if (fieldId.equalsIgnoreCase("/root/MsgBody/CtrlNbInf/CtrlNbInfLst/CtrlNbL")){
                String s = "";
                for (int i = 0; i < 5; i++){
                    s+=getFixLenthString(4);
                    s+="|";
                }
                XmlDocHelper4wl.setNodeValue(modelDoc,fieldId, s.substring(0,s.length()-1));
                continue;
            }
            XmlDocHelper4wl.setNodeValue(modelDoc, fieldId, XmlDocHelper4wl.getNodeValue(doc, fieldId));
        }
        //取值规则
        List<NxyFuncUsecaseDataRule> rules = ruleDao.findByUcDataId(ucDataId);
        for (NxyFuncUsecaseDataRule rule:rules){
            String destFieldId = rule.getDestFieldId();
            String srcFieldId = rule.getSrcFieldId();
            String value = "";
            value = XmlDocHelper4wl.getNodeValue(recvDoc,srcFieldId);
            XmlDocHelper4wl.setNodeValue(modelDoc,destFieldId,value);
        }

        //结果值设置
        List<NxyFuncUsecaseExpected> expectedList = expectedDao.findByUsecaseDataId(ucDataId);
        for(NxyFuncUsecaseExpected expected : expectedList){
            String fieldId = expected.getMsgField().getFieldId();
            XmlDocHelper4wl.setNodeValue(modelDoc, fieldId, expected.getExpectedValue());
        }
        return modelDoc.asXML();
    }

    public Long saveSendMessage(Long recvId, Object obj, String sendMsg, ServerConfig config){
        SimSysInsMessage insMessage = (SimSysInsMessage) obj;
        NxyFuncCaseSend send = new NxyFuncCaseSend();
        send.setMessage(sendMsg);
        send.setMsgCode(insMessage.getMsgType());
        send.setMsgName(insMessage.getName());
        NxyFuncCaseRecv recv = caseRecvDao.findOne(recvId);
        send.setCaseRecv(recv);
        send.setAdapterId(config.getAdapterId());
        send.setSysinsId(config.getSimInsId());
        send = caseSendDao.save(send);
        return send.getId();
    }

    public void saveAsyncNoticeMessage(String recvMsg){
        String seqNb = "";
        String name = "";
        String code = "";
        try{
            recvMsg = removeSignStr(recvMsg);
            Document recvDoc = XmlDocHelper4wl.getXmlFromStr(recvMsg);
            code = XmlDocHelper4wl.getNodeValue(recvDoc,"root/MsgHeader/MsgTp");
            if(StringUtils.isEmpty(code)){
                List<Element> elements = XmlDocHelper4wl.getNodeElements(recvDoc, "//Message");
                code = elements.get(0).getName();
            }
            seqNb = XmlDocHelper4wl.getNodeValue(recvDoc, "//TrxId");
            if(StringUtils.isEmpty(seqNb)){
                seqNb = XmlDocHelper4wl.getNodeValue(recvDoc, "//serialNo");
            }
            if(StringUtils.isNotEmpty(code)){
                String sql = "select name from sim_message where mesg_type=? limit 0,1";
                name = jdbcTemplate.queryForObject(sql, String.class, code);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        //保存来账信息
        NxyAsyncNotice asyncNotice = new NxyAsyncNotice();
        asyncNotice.setCreateTime(new Date());
        asyncNotice.setSeqNo(seqNb);
        asyncNotice.setName(name==null?"":name);
        asyncNotice.setCode(code);
        asyncNotice.setMsg(recvMsg);
        asyncNoticeDao.save(asyncNotice);
    }

    public boolean getRespModel(Long adapterId){
        logger.info("来账应答适配器Id:{}", adapterId);
        SysAdapter adapter = adapterDao.findOne(adapterId);
        logger.info("来账应答适配器:{}", adapter.getName());
        return adapter.getResponseModel()==null?true:adapter.getResponseModel();
    }

    public String getSendMessage(Long sendId){
        NxyFuncCaseSend send = caseSendDao.findOne(sendId);
        return send.getMessage();
    }

    public boolean getSendResponse(Long recvId){
        NxyFuncCaseRecv recv = caseRecvDao.findOne(recvId);
        return recv.getStatus()==2?true:false;
    }

    public void updateSendMessage(Long sendId){
        NxyFuncCaseSend send = caseSendDao.findOne(sendId);
        send.setMessage("");
        caseSendDao.save(send);
    }

    public static void main(String[] args) {
        String relativelyPath=System.getProperty("user.dir");
        System.out.println(relativelyPath);
    }
}

