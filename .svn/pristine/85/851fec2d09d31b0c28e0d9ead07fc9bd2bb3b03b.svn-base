package com.zdtech.platform.service.funcexec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.rbac.ShiroUser;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.service.UcodeService;
import com.zdtech.platform.framework.service.UserService;
import com.zdtech.platform.framework.utils.Bundle;
import com.zdtech.platform.framework.utils.XmlDocHelper;
import com.zdtech.platform.framework.utils.XmlJsonHelper;
import com.zdtech.platform.utils.MapUtils;
import org.dom4j.Document;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by yjli on 2017/9/6.
 */
@Service
public class FuncExecService {
    private Logger logger = LoggerFactory.getLogger(FuncExecService.class);

    @Autowired
    private NxyFuncItemDao nxyFuncItemDao;
    @Autowired
    private UserService userService;
    @Autowired
    private NxyFuncUsecaseDao nxyFuncUsecaseDao;
    @Autowired
    private NxyFuncUsecaseDataDao nxyFuncUsecaseDataDao;
    @Autowired
    private NxyFuncUsecaseExpectedDao nxyFuncUsecaseExpectedDao;
    @Autowired
    private SimSysInsMessageDao simSysInsMessageDao;
    @Autowired
    private MsgFieldDao msgFieldDao;
    @Autowired
    private UcodeService ucodeService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private SysCodeDao sysCodeDao;
    @Autowired
    private TestProjectDao testProjectDao;
    @Autowired
    private NxyFuncUsecaseExecDao nxyFuncUsecaseExecDao;
    @Autowired
    private NxyUsecaseExecBatchDao nxyUsecaseExecBatchDao;
    @Autowired
    private NxyFuncUsecaseDataRuleDao nxyFuncUsecaseDataRuleDao;
    @Value("${uno.pattern}")
    private String unoPattern;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NxyFuncConfigDao nxyFuncConfigDao;

    public String getUnoPattern() {
        return unoPattern;
    }
    @Autowired
    private SysConfDao confDao;

    private String getToolType(){
        SysConf conf = confDao.findByCategoryAndKey("SIMULATOR_SERVER", "TOOL_TYPE");
        if(conf != null){
            return conf.getKeyVal();
        }
        return "atsp";
    }

    //删除功能测试项
    public void delItems(Long[] ids) {
        nxyFuncItemDao.delByItemIds(Arrays.asList(ids));
        for(Long id : ids){
            nxyFuncConfigDao.deleteByItemId(id);
        }
    }

    //删除功能测试项及其所有子节点
    public void delByItemsId(Long id) {
        List<NxyFuncItem> items = nxyFuncItemDao.findByParentId(id);
        List<NxyFuncItem> dels = new ArrayList<NxyFuncItem>();
        dels = list(dels, items);
        NxyFuncItem item = nxyFuncItemDao.findOne(id);
        dels.add(item);
        if(item.getParentId() == null){
            nxyFuncConfigDao.deleteByItemId(id);
        }
        nxyFuncItemDao.deleteInBatch(dels);
    }

    //递归获取所有子节点信息
    private List<NxyFuncItem> list(List<NxyFuncItem> dels, List<NxyFuncItem> items) {
        for (NxyFuncItem item : items) {
            dels.add(item);
            List<NxyFuncItem> itemList = nxyFuncItemDao.findByParentId(item.getId());
            if (!itemList.isEmpty()) {
                list(dels, itemList);
            }
        }
        return dels;
    }

    //添加功能测试用例
    public Result addUsecase(NxyFuncUsecase usecase, String usecaseAllDataList, Long itemId) throws IOException {
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
        usecase.setNxyFuncItem(item);
        usecase.setSeqNo(usecase.getSeqNo()==null?0:usecase.getSeqNo());
        usecase.setCreateUser(userService.getCurrentUser().getUserName());
        usecase.setResult("noexec");
        nxyFuncUsecaseDao.save(usecase);
        //保存用例数据
        List allData = new ObjectMapper().readValue(usecaseAllDataList, List.class);
        for (int i = 0; i < allData.size(); i++) {
            Map<String, String> map = (Map<String, String>) allData.get(i);
            String messageName = map.get("messageName");
            String messageCode = map.get("messageCode");
            String messageId = map.get("messageId");
            String usecaseDataList = map.get("usecaseDataList");
            String expectedDataList = map.get("expectedDataList");
            String replyDataList = map.get("replyDataList");
            String ruleDataList = map.get("ruleDataList");

            NxyFuncUsecaseData data = new NxyFuncUsecaseData();
            data.setResult("noexec");
            SimSysInsMessage sysInsMessage = simSysInsMessageDao.findOne(Long.valueOf(messageId));
            data.setSimMessage(sysInsMessage);
            data.setMessageName(messageName);
            data.setMessageCode(messageCode);
            if("atsp".equals(getToolType())){
                String strXml = XmlJsonHelper.JsonToXml(usecaseDataList);
                data.setMessageMessage(XmlDocHelper.xmlZuZhuang(strXml, sysInsMessage));
            } else {
                String xml = data.getSimMessage().getModelFileContent();
                Document doc = XmlDocHelper.getXmlFromStr(xml);
                Map<String, String> mapData = MapUtils.getMapByStr(usecaseDataList);
                xml = XmlDocHelper.getXmlByMap(mapData, doc);
                data.setMessageMessage(xml);
            }
            data.setNxyFuncUsecase(usecase);
            data.setSeqNo(i + 1);
            nxyFuncUsecaseDataDao.save(data);
            //添加预期值和业务回复值
            List<NxyFuncUsecaseExpected> list = new ArrayList<>();
            if(!StringUtils.isEmpty(expectedDataList)){
                List<Map<String, String>> expecteds = new ObjectMapper().readValue(expectedDataList, new TypeReference<List<Map<String, String>>>() {});
                for (Map<String, String> expected : expecteds) {
                    NxyFuncUsecaseExpected usecaseExpected = new NxyFuncUsecaseExpected();
                    usecaseExpected.setUsecaseDataId(data.getId());
                    usecaseExpected.setFieldIdName(expected.get("nameZh"));
                    usecaseExpected.setExpectedValue(expected.get("expectedValue"));
                    MsgField field = msgFieldDao.findOne(Long.valueOf(expected.get("fieldId")));
                    usecaseExpected.setMsgField(field);
                    usecaseExpected.setType("expect");
                    list.add(usecaseExpected);
                }
            }

            if(!StringUtils.isEmpty(replyDataList)){
                List<Map<String, String>> replys = new ObjectMapper().readValue(replyDataList, new TypeReference<List<Map<String, String>>>() {});
                for (Map<String, String> reply : replys) {
                    NxyFuncUsecaseExpected usecaseExpected = new NxyFuncUsecaseExpected();
                    usecaseExpected.setUsecaseDataId(data.getId());
                    usecaseExpected.setFieldIdName(reply.get("nameZh"));
                    usecaseExpected.setExpectedValue(reply.get("expectedValue"));
                    MsgField field = msgFieldDao.findOne(Long.valueOf(reply.get("fieldId")));
                    usecaseExpected.setMsgField(field);
                    usecaseExpected.setType("next");
                    list.add(usecaseExpected);
                }
            }
            nxyFuncUsecaseExpectedDao.save(list);
            if(!StringUtils.isEmpty(ruleDataList)){
                List<NxyFuncUsecaseDataRule> ruleList = new ArrayList<>();
                List<Map<String, String>> rules = new ObjectMapper().readValue(ruleDataList, new TypeReference<List<Map<String, String>>>() {});
                for (Map<String, String> rule : rules) {
                    NxyFuncUsecaseDataRule nxyFuncUsecaseDataRule = new NxyFuncUsecaseDataRule();
                    nxyFuncUsecaseDataRule.setUsecaseDataId(data.getId());
                    nxyFuncUsecaseDataRule.setDestFieldId(rule.get("destFieldId"));
                    nxyFuncUsecaseDataRule.setSrcFieldId(rule.get("srcFieldId"));
                    nxyFuncUsecaseDataRule.setSrcSendRecv(rule.get("srcSendRecv"));
                    String srcMsgId = rule.get("srcMsgId");
                    Long srcMsgIdLong = null;
                    if(srcMsgId != null){
                        srcMsgIdLong = Long.parseLong(srcMsgId);
                    }
                    nxyFuncUsecaseDataRule.setSrcMsgId(srcMsgIdLong);
                    nxyFuncUsecaseDataRule.setEvaluateType(rule.get("srcMsgIndex"));
                    ruleList.add(nxyFuncUsecaseDataRule);
                }
                nxyFuncUsecaseDataRuleDao.save(ruleList);
            }
        }
        return ret;
    }

    //保存用例数据
    public Result saveUsecaseData(NxyFuncUsecaseData data, String usecaseDataList, String expectedDataList, String replyDataList, String ruleDataList, Long usecaseId, Long messageId) throws IOException {
        Result ret = new Result();
        NxyFuncUsecase usecase = nxyFuncUsecaseDao.findOne(usecaseId);
        SimSysInsMessage sysInsMessage = simSysInsMessageDao.findOne(messageId);
        if("atsp".equals(getToolType())){
            String strXml = XmlJsonHelper.JsonToXml(usecaseDataList);
            data.setMessageMessage(XmlDocHelper.xmlZuZhuang(strXml, sysInsMessage));
        } else {
            String xml = sysInsMessage.getModelFileContent();
            Document doc = XmlDocHelper.getXmlFromStr(xml);
            Map<String, String> map = MapUtils.getMapByStr(usecaseDataList);
            xml = XmlDocHelper.getXmlByMap(map, doc);
            data.setMessageMessage(xml);
        }
        data.setResult("noexec");
        data.setNxyFuncUsecase(usecase);
        int count = nxyFuncUsecaseDataDao.countByUsecaseId(usecaseId);
        data.setSeqNo(count + 1);
        data.setSimMessage(sysInsMessage);
        nxyFuncUsecaseDataDao.save(data);
        //添加预期值和业务回复值
        List<NxyFuncUsecaseExpected> list = new ArrayList<>();
        if(!StringUtils.isEmpty(expectedDataList)){
            List<Map<String, String>> expecteds = new ObjectMapper().readValue(expectedDataList, new TypeReference<List<Map<String, String>>>() {});
            for (Map<String, String> expected : expecteds) {
                NxyFuncUsecaseExpected usecaseExpected = new NxyFuncUsecaseExpected();
                usecaseExpected.setUsecaseDataId(data.getId());
                usecaseExpected.setFieldIdName(expected.get("nameZh"));
                usecaseExpected.setExpectedValue(expected.get("expectedValue"));
                MsgField field = msgFieldDao.findOne(Long.valueOf(expected.get("fieldId")));
                usecaseExpected.setMsgField(field);
                usecaseExpected.setType("expect");
                list.add(usecaseExpected);
            }
        }

        if(!StringUtils.isEmpty(replyDataList)){
            List<Map<String, String>> replys = new ObjectMapper().readValue(replyDataList, new TypeReference<List<Map<String, String>>>() {});
            for (Map<String, String> reply : replys) {
                NxyFuncUsecaseExpected usecaseExpected = new NxyFuncUsecaseExpected();
                usecaseExpected.setUsecaseDataId(data.getId());
                usecaseExpected.setFieldIdName(reply.get("nameZh"));
                usecaseExpected.setExpectedValue(reply.get("expectedValue"));
                MsgField field = msgFieldDao.findOne(Long.valueOf(reply.get("fieldId")));
                usecaseExpected.setMsgField(field);
                usecaseExpected.setType("next");
                list.add(usecaseExpected);
            }
        }
        nxyFuncUsecaseExpectedDao.save(list);
        if(!StringUtils.isEmpty(ruleDataList)){
            List<NxyFuncUsecaseDataRule> ruleList = new ArrayList<>();
            List<Map<String, String>> rules = new ObjectMapper().readValue(ruleDataList, new TypeReference<List<Map<String, String>>>() {});
            for (Map<String, String> rule : rules) {
                NxyFuncUsecaseDataRule nxyFuncUsecaseDataRule = new NxyFuncUsecaseDataRule();
                nxyFuncUsecaseDataRule.setUsecaseDataId(data.getId());
                nxyFuncUsecaseDataRule.setDestFieldId(rule.get("destFieldId"));
                nxyFuncUsecaseDataRule.setSrcFieldId(rule.get("srcFieldId"));
                nxyFuncUsecaseDataRule.setSrcSendRecv(rule.get("srcSendRecv"));
                String srcMsgId = rule.get("srcMsgId");
                Long srcMsgIdLong = null;
                if(srcMsgId != null){
                    srcMsgIdLong = Long.parseLong(srcMsgId);
                }
                nxyFuncUsecaseDataRule.setSrcMsgId(srcMsgIdLong);
                nxyFuncUsecaseDataRule.setEvaluateType(rule.get("srcMsgIndex"));
                ruleList.add(nxyFuncUsecaseDataRule);
            }
            nxyFuncUsecaseDataRuleDao.save(ruleList);
        }
        return ret;
    }

    //删除用例数据
    public int delUsecaseDatas(List<Long> ids){
        NxyFuncUsecaseData data = nxyFuncUsecaseDataDao.findOne(ids.get(0));
        Long usecaseId = data.getNxyFuncUsecase().getId();
        int count = nxyFuncUsecaseDataDao.delByIds(ids);
        List<NxyFuncUsecaseData> dataList = nxyFuncUsecaseDataDao.findByUsecaseId(usecaseId);
        for(int i=0; i<dataList.size(); i++){
            dataList.get(i).setSeqNo(i+1);
        }
        nxyFuncUsecaseDataDao.save(dataList);
        return count;
    }

    /**
     *
     * @param params
     * @param page
     * @return
     */
    public Map<String, Object> getUsecase(Map<String, Object> params, Pageable page){
        Map<String, Object> ret = new HashMap<>();
        String id = params.get("id").toString();
        String type = params.get("type").toString();
        String mark = params.get("mark").toString();
        String result = "";
        Query query;
        if("project".equals(type)){
            List<NxyFuncItem> items = nxyFuncItemDao.findByProjectId(Long.valueOf(id));
            for(NxyFuncItem item : items){
                query = entityManager.createNativeQuery(String.format("SELECT getChildList(%s)",item.getId()));
                Object obj = query.getSingleResult();
                result += obj.toString();
            }
        } else if("item".equals(type)){
            query = entityManager.createNativeQuery(String.format("SELECT getChildList(%s)",id));
            Object obj = query.getSingleResult();
            result = obj.toString();
        }
        if(result.isEmpty()){
            ret.put("rows", new ArrayList<>());
            ret.put("total",0);
            return ret;
        }
        result = result.substring(1);
        String sql = String.format("select * from nxy_func_usecase where item_id in (select id from nxy_func_item where id in (%s) and mark='%s') and 1=1",result, mark);
        if (params.containsKey("caseNumber|like")){
            sql = sql + String.format(" and case_number like '%%%s%%'",params.get("caseNumber|like"));
        }
        if (params.containsKey("purpose|like")){
            sql = sql + String.format(" and purpose like '%%%s%%'",params.get("purpose|like"));
        }
        if (params.containsKey("result")){
            String resultType = params.get("result").toString();
            if(resultType.startsWith("!")){
                resultType = resultType.substring(1);
                sql = sql + String.format(" and result != '%s'",resultType);
            } else {
                sql = sql + String.format(" and result = '%s'",params.get("result"));
            }
        }
        if(params.containsKey("search")){
            String search = params.get("search").toString();
            if("succ".equals(search)){
                sql += " and result = 'expected' ";
            } else {
                sql += " and result != 'expected' ";
            }
        }
        query = entityManager.createNativeQuery(sql + " order by seq_no DESC, id DESC ");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> list = null;
        if (page != null) {
            int pageNumber = page.getPageNumber();
            int pageSize = page.getPageSize();
            list = query.setFirstResult((pageNumber)*pageSize).setMaxResults(page.getPageSize()).getResultList();
        } else {
            list= query.getResultList();
        }
        for (Map<String,Object> row:list){
            int number = nxyFuncUsecaseExecDao.countByUsecaseId(Long.parseLong(row.get("id").toString()));
            row.put("number",number);
            SysCode code = sysCodeDao.findByCategoryAndKey("UC_RESULT",row.get("result").toString());
            row.put("resultDis",code.getValue());
        }
        sql = sql.replaceFirst("select \\*","select count(1)");
        query = entityManager.createNativeQuery(sql);
        Object o = query.getSingleResult();
        String total = o.toString();
        ret.put("rows", list);
        ret.put("total",total);
        return ret;
    }

    public Map<String, Object> testresult(Map<String, Object> params){
        Map<String, Object> ret = new HashMap<>();
        String id = params.get("id").toString();
        String type = params.get("type").toString();
        //判断当前Id及其所有子和所有父是否在执行中
        String result = "";
        Query query;
        ret.put("status", "finished");
        if("project".equals(type)){
            List<NxyFuncItem> items = nxyFuncItemDao.findByProjectId(Long.valueOf(id));
            for(NxyFuncItem item : items){
                query = entityManager.createNativeQuery(String.format("SELECT getChildList(%s)",item.getId()));
                Object obj = query.getSingleResult();
                result += obj.toString();
            }
            int pcount = nxyUsecaseExecBatchDao.countByItemIdsAndTypeWithStarting(Arrays.asList(new Long[]{Long.valueOf(id)}), "project");
            if(pcount>0) {
                ret.put("status", "starting");//当前项目正在执行中
            }else {
                if(!result.isEmpty()){
                    int icount = nxyUsecaseExecBatchDao.countByItemIdsAndTypeWithStarting(strToListL(result), "item");
                    if(icount>0) ret.put("status", "starting"); //当前项目下有测试项正在执行中
                }
            }
        } else if("item".equals(type)){
            query = entityManager.createNativeQuery(String.format("SELECT getChildList(%s)",id));
            Object obj = query.getSingleResult();
            result = obj.toString();

            NxyFuncItem item = nxyFuncItemDao.findOne(Long.parseLong(id));
            int pcount = nxyUsecaseExecBatchDao.countByItemIdsAndTypeWithStarting(Arrays.asList(new Long[]{item.getTestProject().getId()}), "project");
            if(pcount>0) {
                ret.put("status", "starting");
            } else {
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
                query = entityManager.createNativeQuery(sql);
                List list = query.getResultList();
                List<Long> ids = strToListL(result);
                ids.addAll(BigIntegerListToLongList(list));
                int icount = nxyUsecaseExecBatchDao.countByItemIdsAndTypeWithStarting(ids, "item");
                if(icount>0) ret.put("status", "starting"); //当前项目下有测试项正在执行中
            }
        }
        if(result.isEmpty()){
            ret.put("successCount", 0);
            ret.put("failCount", 0);
            ret.put("total",0);
            ret.put("execTime", "0000-00-00 00:00:00");
            return ret;
        }
        result = result.substring(1);
        String sql = String.format("select count(1) from nxy_func_usecase where item_id in (%s)",result);
        query = entityManager.createNativeQuery(sql);
        Object o = query.getSingleResult();
        String total = o.toString();
        sql = String.format("select count(1) from nxy_func_usecase where item_id in (%s) and result = 'expected'",result);
        query = entityManager.createNativeQuery(sql);
        o = query.getSingleResult();
        String successCount = o.toString();
        sql = String.format("select count(1) from nxy_func_usecase where item_id in (%s) and result <> 'expected'",result);
        query = entityManager.createNativeQuery(sql);
        o = query.getSingleResult();
        String failCount = o.toString();
        ret.put("successCount", successCount);
        ret.put("failCount", failCount);
        ret.put("total", total);
        sql = String.format("select e.exec_time from nxy_func_usecase_exec e right join (select id from nxy_func_usecase where item_id in (%s)) u on e.usecase_id = u.id order by e.exec_time desc limit 0,1",result);
        query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        o = query.getSingleResult();
        Map<String, Object> map = (Map<String, Object>) o;
        ret.put("execTime", map.get("exec_time") == null ? "0000-00-00 00:00:00" : map.get("exec_time").toString().substring(0, 19));
        return ret;
    }

    private List<Long> strToListL(String str){
        List<Long> list = new ArrayList<>();
        if(StringUtils.isEmpty(str)) return list;
        String[] strs = str.split(",");
        for(String s : strs){
            if(!s.isEmpty()){
                list.add(Long.valueOf(s));
            }
        }
        return list;
    }

    private List<Long> BigIntegerListToLongList(List blist){
        List<Long> list = new ArrayList<>();
        if(blist == null) return list;
        for(int i=0; i<blist.size(); i++){
            Object o = blist.get(i);
            list.add(Long.parseLong(o.toString()));
        }
        return list;
    }

    public String getCurrentUserRoleType(){
        ShiroUser user = userService.getCurrentUser();
        String sql = "select a.role from sys_user_role b left join sys_roles a on b.role_id = a.id where b.sys_user_id = %s";
        Query query = entityManager.createNativeQuery(String.format(sql, user.getUserId()));
        List<Object> objects = query.getResultList();
        String s = "";
        for (Object object:objects){
            if (object.toString().equals("admin")){
                s = object.toString();
                break;
            }else if (s.equals("manager")){
                continue;
            }
            s = object.toString();
        }
        //Object o = query.getSingleResult();
        return s;
    }

    public List<TestProject> getProject(){
        ShiroUser user = userService.getCurrentUser();
        return testProjectDao.findByUser(user.getUserId());
    }

    public List<NxyFuncItem> getItemByProjectId(Long projectId){
        List<NxyFuncItem> itemList = new ArrayList<>();
        ShiroUser user = userService.getCurrentUser();
        String countSql = "select count(*) from nxy_func_item_user where item_type = 'project' and item_id = %s and user_id = %s";
        countSql = String.format(countSql, projectId, user.getUserId());
        Query query = entityManager.createNativeQuery(countSql);
        Object o = query.getSingleResult();
        int count = Integer.parseInt(o.toString());
        if(count != 0){
            return nxyFuncItemDao.findByProjectId(projectId);
        } else {
            Map<Long, Long> only = new HashMap<>();
            String itemUserSql = "select item_id from nxy_func_item_user where item_type='item' and project_id = %s and user_id = %s";
            itemUserSql = String.format(itemUserSql, projectId, user.getUserId());
            query = entityManager.createNativeQuery(itemUserSql);
            List list = query.getResultList();
            if(list != null && !list.isEmpty()){
                for(int i=0; i<list.size(); i++){
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
                    sql = String.format(sql, list.get(i));
                    query = entityManager.createNativeQuery(sql);
                    List itemIds = query.getResultList();
                    if(itemIds != null && !itemIds.isEmpty()){
                        NxyFuncItem item = nxyFuncItemDao.findOne(Long.parseLong(itemIds.get(0).toString()));
                        if(only.get(item.getId()) == null){
                            only.put(item.getId(), item.getId());
                            itemList.add(item);
                        }
                    }
                }
            }
        }
        return itemList;
    }

    public List<NxyFuncItem> getItemByItemId(Long itemId){
        List<NxyFuncItem> itemList = new ArrayList<>();
        ShiroUser user = userService.getCurrentUser();
        String countSql = "select count(*) from nxy_func_item_user where item_type = 'item' and item_id = %s and user_id = %s";
        countSql = String.format(countSql, itemId, user.getUserId());
        Query query = entityManager.createNativeQuery(countSql);
        Object o = query.getSingleResult();
        int count = Integer.parseInt(o.toString());
        if(count != 0){
            return nxyFuncItemDao.findByParentId(itemId);
        } else {
            Map<Long, Long> only = new HashMap<>();
            NxyFuncItem funcItem = nxyFuncItemDao.findOne(itemId);
            String itemUserSql = "select item_id from nxy_func_item_user where item_type='item' and project_id = %s and user_id = %s";
            itemUserSql = String.format(itemUserSql, funcItem.getTestProject().getId(), user.getUserId());
            query = entityManager.createNativeQuery(itemUserSql);
            List list = query.getResultList();
            if(list != null && !list.isEmpty()){
                for(int i=0; i<list.size(); i++){
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
                            "ON T1._id = T2.id WHERE T2.id > %s \n" +
                            "ORDER BY T1.lvl DESC";
                    sql = String.format(sql, list.get(i), itemId);
                    query = entityManager.createNativeQuery(sql);
                    List itemIds = query.getResultList();
                    if(itemIds != null && !itemIds.isEmpty()){
                        NxyFuncItem item = nxyFuncItemDao.findOne(Long.parseLong(itemIds.get(0).toString()));
                        if(only.get(item.getId()) == null){
                            only.put(item.getId(), item.getId());
                            itemList.add(item);
                        }
                    }
                }
            }
        }
        return itemList;
    }

    public List<Map<String, Object>> getUnselectedUser(Long itemId){
        NxyFuncItem item = nxyFuncItemDao.findOne(itemId);;
        String sql;
        if(item.getParentId() == null){
            sql = "SELECT s.id,s.name FROM (SELECT p.user_id FROM(\n" +
                    "\tSELECT\n" +
                    "\t\tuser_id\n" +
                    "\tFROM\n" +
                    "\t\ttest_project_user\n" +
                    "\tWHERE\n" +
                    "\t\tproject_id = %s\n" +
                    ") p LEFT JOIN (\n" +
                    "\tSELECT\n" +
                    "\t\tuser_id\n" +
                    "\tFROM\n" +
                    "\t\tnxy_func_item_user\n" +
                    "\tWHERE\n" +
                    "\t\titem_id = %s\n" +
                    ") t ON p.user_id = t.user_id WHERE t.user_id IS NULL) ex LEFT JOIN sys_users s ON ex.user_id = s.id";
            sql = String.format(sql, item.getTestProject().getId(), itemId);
        } else {
            sql = "SELECT s.id,s.name FROM (SELECT p.user_id FROM(\n" +
                    "\tSELECT\n" +
                    "\t\tuser_id\n" +
                    "\tFROM\n" +
                    "\t\tnxy_func_item_user\n" +
                    "\tWHERE\n" +
                    "\t\titem_id = %s\n" +
                    ") p LEFT JOIN (\n" +
                    "\tSELECT\n" +
                    "\t\tuser_id\n" +
                    "\tFROM\n" +
                    "\t\tnxy_func_item_user\n" +
                    "\tWHERE\n" +
                    "\t\titem_id = %s\n" +
                    ") t ON p.user_id = t.user_id WHERE t.user_id IS NULL) ex LEFT JOIN sys_users s ON ex.user_id = s.id";
            sql = String.format(sql, item.getParentId(), itemId);
        }
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public List<Map<String, Object>> getSelectedUser(Long itemId){
        String sql = "SELECT s.id,s.name FROM nxy_func_item_user i left join sys_users s on s.id = i.user_id where i.item_id = " + itemId;
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public void insertNxyFuncItemUser(Long[] ids, final Long itemId){
//        final List<Long> userIds = Arrays.asList(ids);
//        String sql = "insert into nxy_func_item_user (user_id, item_id) values (?, ?)";
//        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//            public void setValues(PreparedStatement ps, int i)throws SQLException {
//                ps.setLong(1, userIds.get(i));
//                ps.setLong(2, itemId);
//            }
//            public int getBatchSize() {
//                return userIds.size();
//            }
//        });
    }

    /**
     * 获取农信银域列表信息
     *
     * @param params
     * @param page
     * @return
     */
    public Map<String, Object> queryNxyFieldList(Long id, Map<String, Object> params, Pageable page) {
        Map<String, Object> result = new HashMap();
        String sql = "SELECT * " +
                "FROM msg_field x WHERE x.field_type = 'BODY' AND x.msg_type = 'XML'";

        String countSql = "SELECT count(*) " +
                "FROM msg_field x WHERE x.field_type = 'BODY' AND x.msg_type = 'XML'";
        if(id != 0){
            String sqlRecv = "SELECT rep_msg_id FROM sim_sysins_reply_rule WHERE req_msg_id = " + id;
            List<BigInteger> list = jdbcTemplate.queryForList(sqlRecv, BigInteger.class);
            String ids = "";
            if(list != null && list.size() > 0){
                for(BigInteger b : list){
                    ids += b + ",";
                }
                ids = ids.substring(0, ids.length()-1);
                sql += " AND x.id IN (SELECT message_field_id FROM sim_sysins_message_field WHERE sysins_message_id IN ("+ids+"))";
                countSql += " AND x.id IN (SELECT message_field_id FROM sim_sysins_message_field WHERE sysins_message_id IN ("+ids+"))";
            }
            sql = "select x.*,m.mesg_type as msgCode from sim_sysins_message m LEFT JOIN sim_sysins_message_field f ON m.id=f.sysins_message_id LEFT JOIN msg_field x ON f.message_field_id=x.id where m.id in ("+ids+")";
            countSql = "SELECT count(*) from sim_sysins_message m LEFT JOIN sim_sysins_message_field f ON m.id=f.sysins_message_id LEFT JOIN msg_field x ON f.message_field_id=x.id where m.id in ("+ids+")";
        } else {
            result.put("rows", new ArrayList<>());
            result.put("total", 0);
            return result;
        }
        if (params.get("fieldId") != null) {
            sql += " AND x.field_id like '%" + params.get("fieldId") + "%'";
            countSql += " AND x.field_id like '%" + params.get("fieldId") + "%'";
        }
        if (params.get("nameZh") != null) {
            sql += " AND x.name_zh like '%" + params.get("nameZh") + "%'";
            countSql += " AND x.name_zh like '%" + params.get("nameZh") + "%'";
        }

        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = null;
        if (page != null) {
            int pageNumber = page.getPageNumber();
            int pageSize = page.getPageSize();
            list = query.setFirstResult((pageNumber - 1) * pageSize).setMaxResults(page.getPageSize()).getResultList();
        } else {
            list = query.getResultList();
        }

        result.put("rows", list);
        query = entityManager.createNativeQuery(countSql);
        BigInteger count = (BigInteger) query.getSingleResult();
        result.put("total", count.longValue());
        return result;
    }

    /**
     * 获取农信银域列表信息
     *
     * @param params
     * @param page
     * @return
     */
    public Map<String, Object> queryNxyFieldListForResp(Long id, Map<String, Object> params, Pageable page) {
        Map<String, Object> result = new HashMap();
        String sql = "SELECT * " +
                "FROM msg_field x WHERE x.field_type = 'BODY' AND x.msg_type = 'XML'";

        String countSql = "SELECT count(*) " +
                "FROM msg_field x WHERE x.field_type = 'BODY' AND x.msg_type = 'XML'";
        if(id != 0){
            sql += " AND x.id IN (SELECT message_field_id FROM sim_sysins_message_field WHERE sysins_message_id = "+id+")";
            countSql += " AND x.id IN (SELECT message_field_id FROM sim_sysins_message_field WHERE sysins_message_id = "+id+")";
            sql = "select x.*,m.mesg_type as msgCode from sim_sysins_message m LEFT JOIN sim_sysins_message_field f ON m.id=f.sysins_message_id LEFT JOIN msg_field x ON f.message_field_id=x.id where m.id = "+id+"";
            countSql = "SELECT count(*) from sim_sysins_message m LEFT JOIN sim_sysins_message_field f ON m.id=f.sysins_message_id LEFT JOIN msg_field x ON f.message_field_id=x.id where m.id = "+id+"";
        } else {
            result.put("rows", new ArrayList<>());
            result.put("total", 0);
            return result;
        }
        if (params.get("fieldId") != null) {
            sql += " AND x.field_id like '%" + params.get("fieldId") + "%'";
            countSql += " AND x.field_id like '%" + params.get("fieldId") + "%'";
        }
        if (params.get("nameZh") != null) {
            sql += " AND x.name_zh like '%" + params.get("nameZh") + "%'";
            countSql += " AND x.name_zh like '%" + params.get("nameZh") + "%'";
        }
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = null;
        if (page != null) {
            int pageNumber = page.getPageNumber();
            int pageSize = page.getPageSize();
            list = query.setFirstResult((pageNumber - 1) * pageSize).setMaxResults(page.getPageSize()).getResultList();
        } else {
            list = query.getResultList();
        }

        result.put("rows", list);

        query = entityManager.createNativeQuery(countSql);
        BigInteger count = (BigInteger) query.getSingleResult();
        result.put("total", count.longValue());
        return result;
    }
}
