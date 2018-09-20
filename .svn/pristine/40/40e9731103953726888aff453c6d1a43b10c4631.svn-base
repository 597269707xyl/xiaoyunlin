package com.zdtech.platform.simserver.service;

import cn.com.infosec.netsign.agent.RBCAgent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.utils.XMLConverter;
import com.zdtech.platform.framework.utils.XmlDocHelper;
import com.zdtech.platform.simserver.net.config.ServerConfig;
import com.zdtech.platform.simserver.utils.PinUtil;
import com.zdtech.platform.simserver.utils.reflect.InvokedClass;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WLSimService
 *
 * @author panli
 * @date 2017/9/6
 */
@Service("WLSimService")
public class WLSimService {
    private static Logger logger = LoggerFactory.getLogger(WLSimService.class);
    public static Map<String, String> simInsMap = new HashMap<>();
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SimSysInsMessageDao messageDao;
    @Autowired
    private NxyFuncUsecaseDataDao useCaseDataDao;
    @Autowired
    private SimSysInsReplyRuleDao replyRuleDao;
    @Autowired
    private NxyFuncUsecaseExecSendDao sendDao;
    @Autowired
    private SysConfDao sysConfDao;
    @Autowired
    private SysCodeDao sysCodeDao;
    @Autowired
    private SimSysInsMessageFieldDao fieldDao;
    @Autowired
    private MsgFieldDao msgFieldDao;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private NxyFuncUsecaseDataRuleDao ruleDao;
    @Autowired
    private NxyFuncConfigDao nxyFuncConfigDao;
    @Autowired
    private SimSysInsMessageFieldDao simSysInsMessageFieldDao;
    @Autowired
    private NxyMsgAccDao nxyMsgAccDao;
    @Autowired
    private NxyFuncCaseMarkDao markDao;
    @Autowired
    private NxyFuncCaseBankDao bankDao;
    @Autowired
    private NxyFuncCaseRecvDao caseRecvDao;
    @Autowired
    private NxyFuncCaseSendDao caseSendDao;
    @Autowired
    private NxyFuncUsecaseExpectedDao expectedDao;
    @Autowired
    private NxyFuncEpccMarkDao epccMarkDao;
    @Autowired
    private SimSystemInstanceDao instanceDao;
    @Autowired
    private SysAdapterDao adapterDao;

    public String signMsg(String strMsg, ServerConfig serverConfig, Long msgId) {
        try {
            strMsg = signAndEncrypt(strMsg, serverConfig, msgId);
        } catch (Exception e){
            e.printStackTrace();
        }
        return strMsg;
    }

    //先加密再加签
    private String signAndEncrypt(String strMsg, ServerConfig serverConfig, Long msgId){
        String strSignSrc = "";
        List<SimSysInsMessageField> list = fieldDao.findByMessageId(msgId);
        Map<String, String> encrypt = new HashMap<>();
        Document doc = XmlDocHelper.getXmlFromStr(strMsg);
        //删除域值为$null的域
        deleteNull(doc.getRootElement()); //删除$null的域
        for (SimSysInsMessageField field:list){
            if(field.getEncryptFlag() != null && field.getEncryptFlag()){
                String xPath = field.getMsgField().getFieldId();
                String value = XmlDocHelper.getNodeValue(doc, xPath);
                if(!value.startsWith("$null")){
                    encrypt.put(xPath, value);
                }
            }
            if(field.getSignFlag() != null && field.getSignFlag()){
                String key = field.getMsgField().getFieldId();
                String value = XmlDocHelper.getNodeValue(doc,key);
                if (StringUtils.isEmpty(value) || value.startsWith("$null")){
                    continue;
                }
                String attrPath = key + "[@Ccy='CNY']";
                boolean hasStr = XmlDocHelper.hasAttribute(doc,attrPath);
                if (hasStr){
                    strSignSrc += "CNY";
                }
                value = value.replaceAll("/\\w*/","");
                strSignSrc += value;
                strSignSrc += "|";
            }
        }
        doc = encrypt(doc, encrypt);
        strMsg = doc.asXML();
        logger.info("加签串：{}",strSignSrc);
        if(StringUtils.isNotEmpty(strSignSrc)){
            String msgCode = XmlDocHelper.getNodeValue(doc,"/transaction/header/msg/msgCd");
            String sign = getSignStr(msgCode,strSignSrc,serverConfig.getAdapterId());
            if (!StringUtils.isEmpty(sign)){
                strMsg = strMsg.replaceFirst("</transaction>",sign);
            }
        }
        doc = XmlDocHelper.getXmlFromStr(strMsg);
        //删除域值为$null的域
//        deleteNull(doc.getRootElement()); //删除$null的域
        //外部报文修正，修正自定义的域路径
        strMsg = XMLConverter.asXmlByRestore(doc);
        Pattern p = Pattern.compile(">\\s*|\t|\r|\n");
        Matcher m = p.matcher(strMsg);
        strMsg = m.replaceAll(">");
        return strMsg;
    }

    //加密
    private Document encrypt(Document doc, Map<String, String> encrypt){
        if(!encrypt.isEmpty()) {
            try {
                logger.info("加密值：{}",new ObjectMapper().writeValueAsString(encrypt));
                String bankid = XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/sndMbrCd");
                String msgcd = XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/msgCd");
                String acctno = "";
                acctno = XmlDocHelper.getNodeValue(doc, nxyMsgAccDao.getAccountByMsgCode(msgcd));
                for (String xPath : encrypt.keySet()) {
                    String value = encrypt.get(xPath);
                    value = value.replaceAll("/\\w*//*","");
                    String encryptMsg = PinUtil.UnionEncryptPin(bankid, value, acctno);
                    XmlDocHelper.setNodeValue(doc, xPath, encryptMsg);
                }
            } catch (Exception e) {
                logger.error("加密异常：{}", e.getMessage());
                e.printStackTrace();
            }
        }
        return doc;
    }
    //加签
    private String getSignStr(String msgCode,String strSignSrc,Long adapterId) {
        String ret = "";
        RBCAgent agent = new RBCAgent();
        try {
            String signIp = simInsMap.get(adapterId + "nxy_sign_ip");
            String signPort = simInsMap.get(adapterId + "nxy_sign_port");
            String signPwd = simInsMap.get(adapterId + "nxy_sign_pwd");
            String signDn = simInsMap.get(adapterId + "nxy_sign_dn");
            logger.warn("加签参数，IP:{},Port:{},Pwd:{},Dn:{}", signIp, signPort, signPwd, signDn);
            if (StringUtils.isEmpty(signIp) ||StringUtils.isEmpty(signPort) ||StringUtils.isEmpty(signPwd) ||StringUtils.isEmpty(signDn)){
                logger.warn("加签参数错误");
                return ret;
            }
            agent.Connect(signIp,signPort,signPwd);
            String signRawMsg = "";
            if (msgCode.equalsIgnoreCase("NPS.903.001.01")){
                signRawMsg = agent.detachedSign(strSignSrc.getBytes(),signDn);
            }else {
                signRawMsg = agent.rawSign(strSignSrc.getBytes(),signDn);
            }
            ret = String.format("<signature>%s</signature></transaction>",StringUtils.isEmpty(signRawMsg)?"":signRawMsg);
            logger.info("加签后：{}",ret);
        }catch (Exception e){
            logger.error("加签异常：{}",e.getMessage());
            e.printStackTrace();
        } finally {
            try{
                agent.closeSignServer();
            } catch (Exception e){}
        }
        return ret;
    }


    public void deleteNull(Element element){ //删除$null的域 和空字符的域
        List elements = element.elements();
        if(elements.size() == 0){
            if(element.getTextTrim().startsWith("$null") || element.getTextTrim().isEmpty()){
                Element e = element.getParent();
                e.remove(element);
                deleteEmpty(e);
            }
            if(element.getTextTrim().startsWith("$space")){ //设置域为空字符
                element.setText("");
            }
            String value = element.getTextTrim();
            //加n位函数
            Pattern p = Pattern.compile("\\{insert\\([\\s\\S]+\\)\\}");
            Matcher m = p.matcher(value);//进行匹配
            if (m.find()) {
                String newValue = value.substring(0, value.indexOf("{"));
                String insertStr = value.substring(value.indexOf("(")+1, value.indexOf(")"));
                logger.info("加：" + insertStr);
                value = newValue + insertStr;
                element.setText(value);
            }
            //减n位函数
            Pattern p1 = Pattern.compile("\\{erease\\(\\d+\\)\\}");
            Matcher m1 = p1.matcher(value);
            if (m1.find()) {
                String newValue = value.substring(0, value.indexOf("{"));
                String ereaseStr = value.substring(value.indexOf("(")+1, value.indexOf(")"));
                int end = Integer.valueOf(ereaseStr);
                if(end >= newValue.length()){
                    logger.info("超出范围:字符串:{},长度：{},减去长度：{}", newValue, newValue.length(), end);
                } else {
                    element.setText(newValue.substring(0, newValue.length()-end));
                }
            }
            //替换n位函数
            Pattern p2 = Pattern.compile("\\{replace\\(\\d+,\\d+,[\\s\\S]+\\)\\}");
            Matcher m2 = p2.matcher(value);
            if (m2.find()) {
                String newValue = value.substring(0, value.indexOf("{"));
                String[] arr = value.substring(value.indexOf("(")+1, value.indexOf(")")).split(",");
                int start = Integer.valueOf(arr[0].trim());
                int end = Integer.valueOf(arr[1].trim());
                if(start + end > newValue.length()){
                    logger.info("超出范围:字符串:{},长度：{},开始位置：{}, 替换长度：{}", newValue, newValue.length(), start, end);
                } else {
                    element.setText(newValue.substring(0, start)+arr[2]+newValue.substring(start+end));
                }
            }
        } else {
            Iterator iterator = elements.iterator();
            while (iterator.hasNext()){
                deleteNull((Element) iterator.next());
            }
        }
    }

    public void deleteEmpty(Element element){ //$null的父节点为空时也删除
        List elements = element.elements();
        if(elements.size() == 0){
            if(element.getTextTrim().isEmpty()){
                Element e = element.getParent();
                e.remove(element);
                deleteEmpty(e);
            }
        }
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


    public void saveAutoExecData(Long batchId,Long execId,NxyFuncUsecaseData ucData, String strMsg,String result,String resultDetail) {
        //报文发送数据是保存组装之后的数据
        strMsg = XMLConverter.xmlStrByAssemble(strMsg);
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
                "(`usecase_exec_id`, `msg`, `ids`, `message_id`, `message_name`, `message_code`, `step`, `result`,`time_stamp`,`result_descript`, `msg_id`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?, ?)";
        String ids = getIdsOfMsg(strMsg,"send");
        Document send = XmlDocHelper.getXmlFromStr(strMsg);
        String sendMsgId = XmlDocHelper.getNodeValue(send, "//MsgId");
        String msgId = "请求报文("+ XmlDocHelper.getNodeValue(send, "/transaction/header/msg/msgCd") +")标识号：" + sendMsgId;
        String seqNb = "请求报文("+ XmlDocHelper.getNodeValue(send, "/transaction/header/msg/msgCd") +")流水号：" +
                XmlDocHelper.getNodeValue(send, "/transaction/header/msg/seqNb");
        resultDetail = resultDetail + "<br/>" + msgId + "<br/>" + seqNb;
        jdbcTemplate.update(sql,execId,strMsg,ids,ucData.getSimMessage().getId(),ucData.getMessageName(),ucData.getMessageCode(),ucData.getSeqNo(),result,System.currentTimeMillis(),resultDetail, sendMsgId);
    }

    public String getIdsOfMsg(String strMsg,String type){
        String ret = "";
        Document doc = XmlDocHelper.getXmlFromStr(strMsg);
        if (type.equalsIgnoreCase("send")){
            ret += XmlDocHelper.getNodeValue(doc,"/transaction/header/msg/sndMbrCd");
            ret += XmlDocHelper.getNodeValue(doc,"/transaction/header/msg/sndAppCd");
            ret += XmlDocHelper.getNodeValue(doc,"/transaction/header/msg/sndDt");
            ret += XmlDocHelper.getNodeValue(doc,"/transaction/header/msg/seqNb");
            ret += XmlDocHelper.getNodeValue(doc,"/transaction/header/msg/msgCd");
            logger.info("=========================send:{}",ret);
        }else {
            ret += XmlDocHelper.getNodeValue(doc,"/transaction/header/msg/refSndMbrCd");
            ret += XmlDocHelper.getNodeValue(doc,"/transaction/header/msg/refSndAppCd");
            ret += XmlDocHelper.getNodeValue(doc,"/transaction/header/msg/refSndDt");
            ret += XmlDocHelper.getNodeValue(doc,"/transaction/header/msg/refSeqNb");
            ret += XmlDocHelper.getNodeValue(doc,"/transaction/header/msg/refMsgCd");
            logger.info("=========================recv:{}",ret);
        }
        return ret;
    }

    public Long messageId(Long simInsId, String strMsg, Long adapterId) {
        Document doc = XmlDocHelper.getXmlFromStr(strMsg);
        String msgType = XmlDocHelper.getNodeValue(doc,"/transaction/header/msg/msgCd");
        SimSystemInstance instance = instanceDao.findByid(simInsId);
        logger.info("仿真实例：{}，报文编号：{}", instance.getName(), msgType);
        if (StringUtils.isEmpty(msgType)){
            return null;
        }
        if("NPS.801.001.01".equals(msgType)){
            String CurSysSts = XmlDocHelper.getNodeValue(doc, "/transaction/body/SysStsNtfctn/SysStsInf/CurSysSts");
            if(!"ST00".equals(CurSysSts)){ //ST00为停运
                logger.info("日切修改日期");
                String CurSysDt = XmlDocHelper.getNodeValue(doc, "/transaction/body/SysStsNtfctn/SysStsInf/CurSysDt");
                CurSysDt = CurSysDt.replaceAll("-", "");
                String updateSysDateSql = "update sim_sysins_conf set param_value = ? where sim_sysins_id = ? and param_key = ?";
                jdbcTemplate.update(updateSysDateSql, CurSysDt, simInsId, "nxy_sys_date");
                simInsMap.put(adapterId + "nxy_sys_date", CurSysDt);
            }
        }
        if("NPS.900.001.01".equals(msgType)){
            String rjctInf = XmlDocHelper.getNodeValue(doc, "//RjctInf");
            if(StringUtils.isNotEmpty(rjctInf) && rjctInf.contains("报文标识号不合法")){
                logger.info("报文标识号不合法修改日期");
                String CurSysDt = XmlDocHelper.getNodeValue(doc, "//PrcDt");
                CurSysDt = CurSysDt.replaceAll("-", "");
                String updateSysDateSql = "update sim_sysins_conf set param_value = ? where sim_sysins_id = ? and param_key = ?";
                jdbcTemplate.update(updateSysDateSql, CurSysDt, simInsId, "nxy_sys_date");
                simInsMap.put(adapterId + "nxy_sys_date", CurSysDt);
            }
        }
        List<SimSysInsMessage> list = messageDao.findMessageByMsgType(simInsId,msgType);
        if (list == null || list.size() < 1){
            return null;
        }
        return list.get(0).getId();
    }

    public boolean isRequest(Long requestId) {
        Object o = replyRuleDao.findReplys(requestId);
        return Integer.parseInt(o.toString())>0;
    }

    //返回本次执行的下一步报文：执行id_下一步报文id
    public String saveRecvMsgData(Long msgId, String strMsg,Long timeStamp) {
        //保存接收数据为组装数据
        strMsg = XMLConverter.xmlStrByAssemble(strMsg);
        String ids = getIdsOfMsg(strMsg,"recv");
        String sql = String.format("SELECT id FROM nxy_func_usecase_exec_send WHERE ids='%s'",ids);
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0){
            //是自动化执行的回执
            Long sendId = Long.parseLong(list.get(0).get("id").toString());
            sql = "SELECT usecase_exec_id FROM nxy_func_usecase_exec_send WHERE id = ?";
            Long execId = jdbcTemplate.queryForObject(sql,Long.class,sendId);
            sql = "SELECT usecase_id FROM nxy_func_usecase_exec WHERE id = ?";
            Long ucId = jdbcTemplate.queryForObject(sql,Long.class,execId);
            sql = "SELECT step FROM nxy_func_usecase_exec_send WHERE id = ?";
            Integer seqNo = jdbcTemplate.queryForObject(sql,Integer.class,sendId);
            //保存应答报文
            SimSysInsMessage model = messageDao.findOne(msgId);
            sql = "INSERT INTO `nxy_func_usecase_exec_recv` " +
                    "(`exec_send_id`, `message_id`, `message_name`, `message_code`, `message_message`) " +
                    "VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,sendId,msgId,model.getName(),model.getMsgType(),strMsg);
            //计算执行结果
            String result = getExpectStatus(sendId,strMsg,timeStamp);
            //保存本次本步骤结果
            String resultDetail = "";
            if(result.startsWith("resptimeout")){
                resultDetail = "应答报文超时" + result.substring(11);
                result = "resptimeout";
            }
            if(result.startsWith("expected")){
                resultDetail = "应答报文符合预期" + result.substring(8);
                result = "expected";
            }
            if(result.startsWith("noexpected")){
                resultDetail = "应答报文不符合预期" + result.substring(10);
                result = "noexpected";
            }
            sql = "UPDATE nxy_func_usecase_exec_send SET result = ?,result_descript=? WHERE id = ?";
            jdbcTemplate.update(sql,result,resultDetail,sendId);
            //保存本次执行结果
            sql = "UPDATE nxy_func_usecase_exec SET result = ?,descript=? WHERE id = ?";
            jdbcTemplate.update(sql,result,resultDetail,execId);
            //保存本用例本步骤结果
            sql = "UPDATE nxy_func_usecase_data SET result = ? WHERE usecase_id = ? AND seq_no = ?";
            jdbcTemplate.update(sql,result,ucId,seqNo);
            //保存本用例执行结果
            sql = "UPDATE nxy_func_usecase SET result = ? WHERE id = ?";
            logger.info("用例执行结果状态修改sql：{}", String.format("UPDATE nxy_func_usecase SET result = '%s' WHERE id = %s", result, ucId));
            int count = jdbcTemplate.update(sql,result,ucId);
            logger.info("用例执行结果状态修改count：{}", count);
            //只有本步骤符合预期，才计算下一步骤报文
            if (result.equalsIgnoreCase("expected")){
                //选择执行下一步的条件
                sql = "SELECT field_id_name,expected_value FROM nxy_func_usecase_expected WHERE type='next' AND usecase_data_id in (SELECT id FROM nxy_func_usecase_data WHERE usecase_id = ? AND seq_no = ?)";
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,ucId,seqNo);
                if (rows == null || rows.size() < 1){
                    //无条件执行下一步
                    sql = "SELECT id FROM nxy_func_usecase_data WHERE usecase_id = ? AND seq_no = ?";
                    List<Map<String,Object>> ll = jdbcTemplate.queryForList(sql,ucId,seqNo+1);
                    if (ll == null || ll.size() < 1){
                        return null;
                    }else {
                        return String.format("%d_%s",execId,ll.get(0).get("id").toString());
                    }
                }else {
                    //是否符合下一步条件
                    boolean flag = true;
                    Document doc = XmlDocHelper.getXmlFromStr(strMsg);
                    for (Map<String, Object> e : rows) {
                        String id = e.get("field_id_name").toString();
                        String value = e.get("expected_value").toString();
                        String realValue = XmlDocHelper.getNodeValue(doc,id);
                        if (StringUtils.isEmpty(realValue) || !realValue.equals(value)) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag){
                        sql = "SELECT id FROM nxy_func_usecase_data WHERE usecase_id = ? AND seq_no = ?";
                        List<Map<String,Object>> ll = jdbcTemplate.queryForList(sql,ucId,seqNo+1);
                        if (ll == null || ll.size() < 1){
                            return null;
                        }else {
                            return String.format("%d_%s",execId,ll.get(0).get("id").toString());
                        }
                    }else {
                        return null;
                    }
                }
            }
            return null;
        }else {
            return null;
        }
    }

    private String getExpectStatus(Long sendId, String strMsg,Long timeStamp) {
        Document doc = XmlDocHelper.getXmlFromStr(strMsg);
        NxyFuncUsecaseExecSend sendData = sendDao.findOne(sendId);
        Document send = XmlDocHelper.getXmlFromStr(sendData.getMsg());
        String sendMsgId = XmlDocHelper.getNodeValue(send, "//MsgId");
        String recvMsgId = XmlDocHelper.getNodeValue(doc, "//MsgId");
        String msgId = "请求报文("+ XmlDocHelper.getNodeValue(send, "/transaction/header/msg/msgCd") +")标识号：" +
                sendMsgId + "，应答报文("+ XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/msgCd") +")标识号：" + recvMsgId;
        String seqNb = "请求报文("+ XmlDocHelper.getNodeValue(send, "/transaction/header/msg/msgCd") +")流水号：" +
                XmlDocHelper.getNodeValue(send, "/transaction/header/msg/seqNb") + "，应答报文("+ XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/msgCd") +")流水号：" +
        XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/seqNb");
        if (sendData.getResult().equalsIgnoreCase("resptimeout")){
            return "resptimeout<br/>" + msgId + "<br/>" + seqNb;
        }
        Long timeStart = sendData.getTimeStamp();
        SysConf conf = sysConfDao.findByCategoryAndKey("SIMULATOR_SERVER","NEPS_TIMEOUT");
        Long timeOut = Long.parseLong(conf.getKeyVal());
        Long eclipse = timeStamp - timeStart;
        if (eclipse > timeOut){
            return "resptimeout<br/>" + msgId + "<br/>" + seqNb;
        }
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
        String RjctInfo = "";
        String BizRjctCd = "";
        for (Map<String, Object> e : list) {
            String fieldId = e.get("field_id").toString();
            MsgField msgField = msgFieldDao.findOne(Long.parseLong(fieldId));
            String nameZh = msgField.getNameZh();
            String id = e.get("field_id_name").toString();
            String value = e.get("expected_value").toString();
            logger.info("预期值：" + value);
            if(value.startsWith("/")){
                String req = XmlDocHelper.getNodeValue(send, value);
                logger.info("预期等于请求的值：{}，路径：{}", req, value);
                if(StringUtils.isNotEmpty(req)){
                    value = req;
                }
            }
            String realValue = XmlDocHelper.getNodeValue(doc,id);
            if (StringUtils.isEmpty(realValue) || !value.contains(realValue)) {
                noexpected += "<br/>预期返回结果：" + nameZh + "(" + value + "), 实际返回结果：" + nameZh + "(" + realValue + ")";
            } else {
                expected += "<br/>预期返回结果：" + nameZh + "(" + value + ")";
            }
        }
        if(!noexpected.isEmpty()){
            noexpected = "noexpected<br/>" + msgId + "<br/>" + seqNb + noexpected;
            RjctInfo = getRjct_Info(doc);
            BizRjctCd = getRjct_Code(doc);
            if(StringUtils.isNotEmpty(BizRjctCd) || StringUtils.isNotEmpty(RjctInfo)){
                noexpected = noexpected + "； 业务拒绝码是：" + BizRjctCd + "； 业务拒绝信息是：" + RjctInfo;
            }
            return noexpected;
        }
        if(!expected.isEmpty()){
            expected = "expected<br/>" + msgId + "<br/>" + seqNb + expected;
            RjctInfo = getRjct_Info(doc);
            BizRjctCd = getRjct_Code(doc);
            if(StringUtils.isNotEmpty(BizRjctCd) || StringUtils.isNotEmpty(RjctInfo)){
                expected = expected + "； 业务拒绝码是：" + BizRjctCd + "； 业务拒绝信息是：" + RjctInfo;
            }
            return expected;
        }
        return ret;
    }

    //获取拒绝信息
    private String getRjct_Info(Document doc){
        List<SysCode> sysCodes = sysCodeDao.findByCategoryOrderBySeqNoAscKeyAsc("RJCT_INFO");
        for(SysCode conf : sysCodes){
            String RjctInfo;
            if(conf.getValue().startsWith("/")){
                RjctInfo = XmlDocHelper.getNodeValue(doc, conf.getValue());
            } else {
                RjctInfo = XmlDocHelper.getNodeValue(doc, "//"+conf.getValue());
            }
            if(StringUtils.isNotEmpty(RjctInfo)){
                return RjctInfo;
            }
        }
        return "";
    }
    //获取拒绝码
    private String getRjct_Code(Document doc){
        List<SysCode> sysCodes = sysCodeDao.findByCategoryOrderBySeqNoAscKeyAsc("RJCT_CODE");
        for(SysCode conf : sysCodes){
            String RjctCode;
            if(conf.getValue().startsWith("/")){
                RjctCode = XmlDocHelper.getNodeValue(doc, conf.getValue());
            } else {
                RjctCode = XmlDocHelper.getNodeValue(doc, "//"+conf.getValue());
            }
            if(StringUtils.isNotEmpty(RjctCode)){
                return RjctCode;
            }
        }
        return "";
    }

    //获取应答报文
    public String[] getRespMsg(Long reqMsgId, String caseNo, String strMsg) {
        try {
            //根据请求报文Id获取应答报文ID
            List<Object> replyRule = replyRuleDao.findReplyMsgs(reqMsgId);
            if(replyRule.isEmpty()) return null;
            for(Object obj : replyRule){
                String newNo = caseNo;
                Long repId = Long.parseLong(obj.toString());
                SimSysInsMessage insMessage = messageDao.findOne(repId);
                //根据案例编号和报文编号获取应答报文信息
                logger.info("应答报文编号：{}，来账案例编号：{}，应答报文Id：{}", insMessage.getMsgType(), newNo, insMessage.getId());
                List<NxyFuncUsecaseData> list = useCaseDataDao.findByMessageCodeAndCaseNo(insMessage.getMsgType(), newNo, insMessage.getId());
                if(!list.isEmpty()){
                    String message = list.get(0).getMessageMessage();
                    String[] arr = new String[3];
                    arr[0] = message;
                    arr[1] = String.valueOf(repId);
                    arr[2] = String.valueOf(list.get(0).getId());
                    return arr;
                } else {
                    String[] arr = saveEpccReqMsgData(reqMsgId, strMsg);
                    if(arr != null) {
                        newNo = arr[0];
                        logger.info("应答EPCC报文编号：{}，EPCC来账案例编号：{}，EPCC应答报文Id：{}", insMessage.getMsgType(), newNo, insMessage.getId());
                        list = useCaseDataDao.findByMessageCodeAndCaseNo(insMessage.getMsgType(), newNo, insMessage.getId());
                        if(!list.isEmpty()){
                            String message = list.get(0).getMessageMessage();
                            arr[0] = message;
                            arr[1] = String.valueOf(repId);
                            arr[2] = String.valueOf(list.get(0).getId());
                            return arr;
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Long saveCaseSendMsg(Long recvId, ServerConfig serverConfig){
        NxyFuncCaseSend send = new NxyFuncCaseSend();
        send.setAdapterId(serverConfig.getAdapterId());
        send.setSysinsId(serverConfig.getSimInsId());
        NxyFuncCaseRecv recv = caseRecvDao.findOne(recvId);
        send.setCaseRecv(recv);
        send = caseSendDao.save(send);
        String sql = "update nxy_func_case_recv set status=%s where id=%s";
        jdbcTemplate.update(String.format(sql, 0, recvId));
        return send.getId();
    }

    //保存应答报文
    public void saveSendMsg(Long repId, String repMsg, Long caseSendId, int status){
        //报文应答数据为组装数据
        repMsg = XMLConverter.xmlStrByAssemble(repMsg);
        SimSysInsMessage insMessage = messageDao.findOne(repId);
        NxyFuncCaseSend send = caseSendDao.findOne(caseSendId);
        send.setMessage(repMsg);
        send.setMsgCode(insMessage.getMsgType());
        send.setMsgName(insMessage.getName());
        String ids = getIdsOfMsg(repMsg, "send");
        send.setIds(ids);
        caseSendDao.save(send);
        String sql = "update nxy_func_case_recv set status=%s where id=%s";
        jdbcTemplate.update(String.format(sql, status, send.getCaseRecv().getId()));
    }

    public boolean getRespModel(Long adapterId){
        logger.info("来账应答适配器Id:{}", adapterId);
        SysAdapter adapter = adapterDao.findOne(adapterId);
        logger.info("来账应答适配器:{}", adapter.getName());
        return adapter.getResponseModel()==null?true:adapter.getResponseModel();
    }

    //保存来账报文 返回来账表Id和来账案例编号
    public String[] saveReqMsgData(Long recvMsgId, String recvStrMsg) {
        try{
            SimSysInsMessage insMessage = messageDao.findOne(recvMsgId);
            //根据报文编号和报文标准获取来账报文标识域
            String fieldId = markDao.getFieldIdByCodeAndStandard(insMessage.getMsgType(), insMessage.getStandard());
            logger.info("来账报文为：{}, 报文标准：{}, 标识域：{}", insMessage.getMsgType(), insMessage.getStandard(), fieldId);
            Document recv = XmlDocHelper.getXmlFromStr(recvStrMsg);
            //获取来账报文的案例编号
            String no = XmlDocHelper.getNodeValue(recv, fieldId);
            logger.info("来账报文填写的案例编号:{}", no);
            String epccIds = "";
            if(StringUtils.isEmpty(no)){
                String[] arr = saveEpccReqMsgData(recvMsgId, recvStrMsg);
                if(arr != null){
                    no = arr[0];
                    epccIds = arr[1];
                }
            }
            //发起行号
            String bankNo = XmlDocHelper.getNodeValue(recv, "/transaction/header/msg/sndMbrCd");
            //流水号
            String msg_seq_no = XmlDocHelper.getNodeValue(recv, "/transaction/header/msg/seqNb");
            String msgId = XmlDocHelper.getNodeValue(recv, "//MsgId");
            //如果来账成员行表没有改成员行信息则新增一条记录
            int count = bankDao.count(bankNo, no);
            if(count == 0){
                NxyFuncCaseBank bank = new NxyFuncCaseBank();
                bank.setBankNo(bankNo);
                bank.setCaseNo(no);
                bankDao.save(bank);
            }
            //保存来账信息
            NxyFuncCaseRecv caseRecv = new NxyFuncCaseRecv();
            caseRecv.setBankNo(bankNo);
            caseRecv.setCaseNo(no);
            caseRecv.setCreateTime(new Date());
            caseRecv.setMsgSeqNo(msg_seq_no);
            caseRecv.setMsgId(msgId);
            caseRecv.setMsgName(insMessage.getName());
            caseRecv.setMsgCode(insMessage.getMsgType());
            caseRecv.setMessage(recvStrMsg);
            caseRecv.setEpccIds(epccIds);
            caseRecvDao.save(caseRecv);
            String[] arr = new String[2];
            arr[0] = no;
            arr[1] = String.valueOf(caseRecv.getId());
            return arr;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //保存Epcc来账报文 返回来账表Id和来账案例编号
    public String[] saveEpccReqMsgData(Long recvMsgId, String recvStrMsg) {
        try{
            SimSysInsMessage insMessage = messageDao.findOne(recvMsgId);
            Document recv = XmlDocHelper.getXmlFromStr(recvStrMsg);
            String epccIds = "";
            String no = "";
            String epccFieldId = epccMarkDao.getFieldIdByCodeAndStandard(insMessage.getMsgType(), insMessage.getStandard());
            logger.info("获取EPCC来帐报文编号：{}，报文标准：{}，报文唯一标识域路径：{}", insMessage.getMsgType(), insMessage.getStandard(), epccFieldId);
            if(epccFieldId != null){
                epccIds = XmlDocHelper.getNodeValue(recv, epccFieldId);
                if(StringUtils.isNotEmpty(epccIds)){ //根据流水号获取案例编号
                    String noSql = "SELECT u.case_number FROM nxy_func_usecase u LEFT JOIN nxy_func_usecase_exec c ON u.id = c.usecase_id " +
                            " LEFT JOIN nxy_func_usecase_exec_send s ON c.id = s.usecase_exec_id " +
                            "WHERE s.epcc_ids = '%s'";
                    noSql = String.format(noSql, epccIds);
                    System.out.println(noSql);
                    List<String> list = jdbcTemplate.queryForList(noSql, String.class);
                    logger.info("来帐EPCC获取案例标号：{}", new ObjectMapper().writeValueAsString(list));
                    if(list != null && list.size()>0){
                        Object nobj = list.get(0);
                        if(nobj != null){
                            no = String.valueOf(nobj);
                        }
                    }
                }
            }
            logger.info("来帐EPCC唯一标识：{}", epccIds);
            logger.info("来帐EPCC案例编号：{}", no);
            String[] arr = new String[2];
            arr[0] = no;
            arr[1] = epccIds;
            return arr;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
        Document destDoc = XmlDocHelper.getXmlFromStr(ret);
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
            Document sendDoc = XmlDocHelper.getXmlFromStr(sendMsg);
            sql = "SELECT message_message,message_id FROM nxy_func_usecase_exec_recv WHERE exec_send_id = ?";
            List<Map<String,Object>> recvs = jdbcTemplate.queryForList(sql,sendId);
            Document recvDoc = null;
            if (recvs != null && recvs.size() > 0){
                String recvMsg = recvs.get(0).get("message_message").toString();
                recvDoc = XmlDocHelper.getXmlFromStr(recvMsg);
            }
            String destFieldId = rule.getDestFieldId();
            String srcFieldId = rule.getSrcFieldId();
            String type = rule.getSrcSendRecv();
            String value = "";
            if (type.equalsIgnoreCase("send")){
                value = XmlDocHelper.getNodeValue(sendDoc,srcFieldId);
                XmlDocHelper.setNodeValue(destDoc,destFieldId,value);
            }else {
                value = XmlDocHelper.getNodeValue(recvDoc,srcFieldId);
                XmlDocHelper.setNodeValue(destDoc,destFieldId,value);
            }
        }
        return destDoc.asXML();
    }

    public String buildAutoSendBussMsg(Long ucDataId, String msg, String reqMsg, Long simInsId, Long adapterId) {
        String ret = msg;
        //取值规则
        List<NxyFuncUsecaseDataRule> rules = ruleDao.findByUcDataId(ucDataId);
        Document destDoc = XmlDocHelper.getXmlFromStr(ret);
        Document sendDoc = XmlDocHelper.getXmlFromStr(reqMsg);
        for (NxyFuncUsecaseDataRule rule:rules){
            String destFieldId = rule.getDestFieldId();
            String srcFieldId = rule.getSrcFieldId();
            String value = "";
            value = XmlDocHelper.getNodeValue(sendDoc,srcFieldId);
            XmlDocHelper.setNodeValue(destDoc,destFieldId,value);
        }
        //结果值设置
        List<NxyFuncUsecaseExpected> expectedList = expectedDao.findByUsecaseDataId(ucDataId);
        for(NxyFuncUsecaseExpected expected : expectedList){
            String fieldId = expected.getMsgField().getFieldId();
            XmlDocHelper.setNodeValue(destDoc, fieldId, expected.getExpectedValue());
        }
        //设置清算日期
        return buildAutoSendMsgClearDate(destDoc.asXML(),  adapterId);
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
        if(!"execsucc".equals(sendData.getResult())){
            return;
        }
        Document send = XmlDocHelper.getXmlFromStr(sendData.getMsg());
        String sendMsgId = XmlDocHelper.getNodeValue(send, "//MsgId");
        String msgId = "请求报文("+ XmlDocHelper.getNodeValue(send, "/transaction/header/msg/msgCd") +")标识号:" + sendMsgId;
        String seqNb = "请求报文("+ XmlDocHelper.getNodeValue(send, "/transaction/header/msg/msgCd") +")流水号:" +
                XmlDocHelper.getNodeValue(send, "/transaction/header/msg/seqNb");
        logger.info("sendId:{},sendResult:{}", sendId, sendData.getResult());
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
        logger.info("修改nxy_func_usecase状态sql：{}", String.format(
                "UPDATE nxy_func_usecase SET result = %s WHERE (SELECT COUNT(1) FROM nxy_func_usecase_exec WHERE usecase_id = %s)=%s AND id = %s", "resptimeout", ucId, round, ucId));
        int count = jdbcTemplate.update(sql,"resptimeout",ucId,round,ucId);
        logger.info("修改nxy_func_usecase超时状态count：{}", count);
    }

//    public String buildAutoSendMsgConfig(Long itemId, String msg){
//        itemId = getItemIdByItemId(itemId);
//        if(itemId != null){
//            List<NxyFuncConfig> configList = nxyFuncConfigDao.findByItemId(itemId);
//            for(NxyFuncConfig config : configList){
//                String mark = config.getVariableEn();
//                if(StringUtils.isNotEmpty(mark) && mark.startsWith("$")){
//                    msg = msg.replaceAll("\\" + mark+"</", config.getVariableValue()+"</");
//                    msg = msg.replaceAll("\\" + mark+"\\{", config.getVariableValue()+"{");//包含函数的替换
//                }
//            }
//        }
//        return msg;
//    }

    public String buildAutoSendMsgConfig(Long itemId, String msg){ //删除$null的域 和空字符的域
        itemId = getItemIdByItemId(itemId);
        if(itemId != null){
            List<NxyFuncConfig> configList = nxyFuncConfigDao.findByItemId(itemId);
            Document doc = XmlDocHelper.getXmlFromStr(msg);
            Map<String, String> configMap = configToMap(configList);
            replaceConfig(doc.getRootElement(), configMap);
            return doc.asXML();
        }
        return msg;
    }

    private Map<String, String> configToMap(List<NxyFuncConfig> configList){
        Map<String, String> map = new HashMap<>();
        for(NxyFuncConfig config : configList){
            map.put(config.getVariableEn(), config.getVariableValue()==null?"":config.getVariableValue());
        }
        return map;
    }

    private void replaceConfig(Element element, Map<String, String> configMap){
        List elements = element.elements();
        if(elements.size() == 0){
            String value = element.getTextTrim();
            if(!value.startsWith("$")) return;
            String hs = "";
            if(value.contains("{")){
                int index = value.indexOf("{");
                hs = value.substring(index);
                value = value.substring(0, index);
            }
            String arr[] = value.split("\\|");
            boolean flag = true;
            for(String str : arr){
                if(StringUtils.isNotEmpty(configMap.get(str))){
                    element.setText(configMap.get(str)+hs);
                    flag = false;
                    return;
                }
            }
            if(flag && configMap.get(arr[0]) != null){
                element.setText(configMap.get(arr[0])+hs);
            }
        } else {
            Iterator iterator = elements.iterator();
            while (iterator.hasNext()){
                replaceConfig((Element) iterator.next(), configMap);
            }
        }
    }

    //设置清算日期
    public String buildAutoSendMsgClearDate(String msg, Long adapterId){
        String clearDate = simInsMap.get(adapterId + "$clearDate");
        if(StringUtils.isNotEmpty(clearDate)){
            msg = msg.replaceAll("\\$clearDate", clearDate);
        } else {
            clearDate = simInsMap.get(adapterId + "nxy_sys_date");
            if(StringUtils.isNotEmpty(clearDate)){
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    Date date = sdf.parse(clearDate);
                    SimpleDateFormat sdfn = new SimpleDateFormat("yyyy-MM-dd");
                    clearDate = sdfn.format(date);
                    msg = msg.replaceAll("\\$clearDate", clearDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return msg;
    }

    public List<SimSysInsMessageField> getMessageFields(Long simSysInsMessageId){
        return simSysInsMessageFieldDao.findByid(simSysInsMessageId);
    }

    //根据测试Id获取顶级测试项Id
    private Long getItemIdByItemId(Long id){
        String sql = "SELECT T2.id  \n" +
                "FROM (  \n" +
                "    SELECT  \n" +
                "        @r AS _id,  \n" +
                "        (SELECT @r \\:= parent_id FROM nxy_func_item WHERE id = _id) AS parent_id,  \n" +
                "        @l \\:= @l + 1 AS lvl  \n" +
                "    FROM  \n" +
                "        (SELECT @r \\:= %s, @l \\:= 0) vars,\n" +
                "        nxy_func_item h  \n" +
                "    WHERE @r <> 0) T1  \n" +
                "JOIN nxy_func_item T2  \n" +
                "ON T1._id = T2.id  \n" +
                "ORDER BY T1.lvl DESC";
        sql = String.format(sql, id);
        Query query = entityManager.createNativeQuery(sql);
        List list = query.getResultList();
        if(list.size()>0){
            Object o = list.get(0);
            return Long.parseLong(o.toString());
        }
        return null;
    }

    public String[] get990Xml(Document doc, Long msgId){
        String[] arr = new String[2];
        SimSysInsMessage simSysInsMessage = messageDao.findOne(msgId);
        simSysInsMessage = messageDao.findByMsgTypeAndSimId("NPS.990.001.01", simSysInsMessage.getSystemInstance().getId());
        if(simSysInsMessage == null){
            logger.info("当前实例模板下没有990报文模板!");
            return null;
        }
        Long simMsgId = simSysInsMessage.getId();
        arr[0] =  simMsgId+"";
        Document simMsg = XmlDocHelper.getXmlFromStr(simSysInsMessage.getModelFileContent());
        InvokedClass ic = new InvokedClass();
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/sndMbrCd", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/refSndMbrCd"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/sndDt", ic.date(""));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/sndTm", ic.time(""));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/rcvMbrCd", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/sndMbrCd"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/seqNb", ic.nextSeqNb(""));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/refSeqNb", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/seqNb"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/refMsgCd", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/msgCd"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/refSndMbrCd", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/sndMbrCd"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/refSndDt", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/sndDt"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/ComConf/ConfInf/OrigSndr", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/sndMbrCd"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/ComConf/ConfInf/OrigSndDt", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/sndDt"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/ComConf/ConfInf/MT", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/msgCd"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/ComConf/ConfInf/MsgId", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/seqNb"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/ComConf/ConfInf/MsgRefId", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/refSeqNb"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/ComConf/ConfInf/MsgPrcCd", "0000");
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/ComConf/ConfInf/Remark", "备注");
        arr[1] = simMsg.asXML();
        return arr;
    }

    public String[] get301Xml(Document doc, Long msgId, Long adapterId){
        String[] arr = new String[2];
        SimSysInsMessage simSysInsMessage = messageDao.findOne(msgId);
        simSysInsMessage = messageDao.findByMsgTypeAndSimId("NPS.301.001.01", simSysInsMessage.getSystemInstance().getId());
        if(simSysInsMessage == null){
            logger.info("当前实例模板下没有301报文模板!");
            return null;
        }
        Long simMsgId = simSysInsMessage.getId();
        arr[0] =  simMsgId+"";
        Document simMsg = XmlDocHelper.getXmlFromStr(simSysInsMessage.getModelFileContent());
        InvokedClass ic = new InvokedClass();
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/sndMbrCd", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/rcvMbrCd"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/sndDt", ic.date(""));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/sndTm", ic.time(""));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/sndAppCd", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/rcvAppCd"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/rcvAppCd", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/sndAppCd"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/rcvMbrCd", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/sndMbrCd"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/seqNb", ic.nextSeqNb(""));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/refSndAppCd", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/sndAppCd"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/refSeqNb", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/seqNb"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/refCallTyp", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/callTyp"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/refMsgCd", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/msgCd"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/refSndMbrCd", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/sndMbrCd"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/header/msg/refSndDt", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/sndDt"));

        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/MsgId", ic.nextMsgid(adapterId+""));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/CreDtTm", ic.dateTime2(""));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/InstgPty/InstgDrctPty", XmlDocHelper.getNodeValue(doc, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/InstdPty/InstdDrctPty"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/InstgPty/InstgPty", XmlDocHelper.getNodeValue(doc, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/InstdPty/InstdPty"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/InstdPty/InstdDrctPty", XmlDocHelper.getNodeValue(doc, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/InstgPty/InstgDrctPty"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/InstdPty/InstdPty", XmlDocHelper.getNodeValue(doc, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/InstgPty/InstgPty"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/BizTyp", XmlDocHelper.getNodeValue(doc, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/BizTyp"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/BizKind", XmlDocHelper.getNodeValue(doc, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/BizKind"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/TranChnlTyp", XmlDocHelper.getNodeValue(doc, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/TranChnlTyp"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/Rmk", "备注");
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/SttlmDt", XmlDocHelper.getNodeValue(doc, "/transaction/body/FIToFIPmtCxlReq/SttlmDt"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/PrcSts", "PR02");
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/PrcCd", "$null");
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/Assgnmt/OrgnlGrpHdr/OrgnlMsgId", XmlDocHelper.getNodeValue(doc, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/MsgId"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/Assgnmt/OrgnlGrpHdr/OrgnlInstgPty", XmlDocHelper.getNodeValue(doc, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/InstgPty/InstgPty"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/Assgnmt/OrgnlGrpHdr/OrgnlMT", XmlDocHelper.getNodeValue(doc, "/transaction/header/msg/msgCd"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/Assgnmt/OrgnlGrpHdr/OrgnlBizTyp", XmlDocHelper.getNodeValue(doc, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/BizTyp"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/Assgnmt/OrgnlGrpHdr/OrgnlBizKind", XmlDocHelper.getNodeValue(doc, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/BizKind"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/Assgnmt/OrgnlGrpHdr/OrgnlTranChnlTyp", XmlDocHelper.getNodeValue(doc, "/transaction/body/FIToFIPmtCxlReq/GrpHdr/TranChnlTyp"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/Assgnmt/OrgnlInstrId", XmlDocHelper.getNodeValue(doc, "/transaction/body/FIToFIPmtCxlReq/Assgnmt/OrgnlGrpHdr/OrgnlMsgId"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/Assgnmt/MsgResnCd", XmlDocHelper.getNodeValue(doc, "/transaction/body/FIToFIPmtCxlReq/Assgnmt/MsgResnCd"));
        XmlDocHelper.setNodeValue(simMsg, "/transaction/body/FIToFIPmtCxlReq/Assgnmt/AddtlInf", "附言");
        deleteNull(simMsg.getRootElement());
        arr[1] = simMsg.asXML();
        return arr;
    }

    public String[] getCaseSendMsg(Long caseSendId, Long simInsId){
        NxyFuncCaseSend send = caseSendDao.findOne(caseSendId);
        if(send != null){
            SimSysInsMessage insMessage = messageDao.findByMsgTypeAndSimId(send.getMsgCode(), simInsId);
            if(insMessage != null){
                String[] arr = new String[2];
                arr[0] = insMessage.getId().toString();
                arr[1] = send.getMessage();
                return arr;
            } else {
                logger.info("实例下面没有该手动应答报文!实例Id：{}，报文编号：{}", simInsId, send.getMsgCode());
            }
        } else {
            logger.info("获取来账手动应答为空!");
        }
        return null;
    }

}
