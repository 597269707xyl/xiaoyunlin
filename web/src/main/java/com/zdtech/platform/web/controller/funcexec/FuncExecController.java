package com.zdtech.platform.web.controller.funcexec;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.service.GenericService;
import com.zdtech.platform.framework.utils.XmlDocHelper;
import com.zdtech.platform.framework.utils.XmlJsonHelper;
import com.zdtech.platform.service.funcexec.FuncExecService;
import com.zdtech.platform.service.funcexec.FuncUsecaseService;
import com.zdtech.platform.utils.Constants;
import com.zdtech.platform.utils.MapUtils;
import com.zdtech.platform.utils.reflect.ReflectWraper;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.*;

/**
 * Created by yjli on 2017/9/6.
 */
@Controller
@RequestMapping("/func/exec")
public class FuncExecController {
    private static Logger log = LoggerFactory.getLogger(FuncExecController.class);

    @Autowired
    private TestProjectDao testProjectDao;
    @Autowired
    private NxyFuncItemDao nxyFuncItemDao;
    @Autowired
    private NxyFuncUsecaseDao nxyFuncUsecaseDao;
    @Autowired
    private NxyFuncUsecaseDataDao nxyFuncUsecaseDataDao;
    @Autowired
    private SimSysInsMessageDao simSysInsMessageDao;
    @Autowired
    private NxyFuncUsecaseExpectedDao nxyFuncUsecaseExpectedDao;
    @Autowired
    private FuncExecService funcExecService;
    @Autowired
    private GenericService genericService;
    @Autowired
    private NxyFuncUsecaseExecDao nxyFuncUsecaseExecDao;
    @Autowired
    private FuncUsecaseService funcUsecaseService;
    @Autowired
    private MsgFieldDao msgFieldDao;
    @Autowired
    private NxyFuncUsecaseDataRuleDao ruleDao;
    @Autowired
    private SimSysInsReplyRuleDao simSysInsReplyRuleDao;
    @Autowired
    private NxyFuncConfigDao nxyFuncConfigDao;
    @Autowired
    private SysConfDao confDao;

    private String getToolType(){
        SysConf conf = confDao.findByCategoryAndKey("SIMULATOR_SERVER", "TOOL_TYPE");
        if(conf != null){
            return conf.getKeyVal();
        }
        return "atsp";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String tolist(Model model) {
        model.addAttribute("permissions", 0);
        String roleType = funcExecService.getCurrentUserRoleType();
        if("admin".equals(roleType) || "manager".equals(roleType)){
            model.addAttribute("permissions", 1);
        }
        model.addAttribute("toolType", getToolType());
        return "/funcexec/usecase/func-item-list";
    }


    @RequestMapping(value = "/copyItemPage", method = RequestMethod.GET)
    public String copyItemPage() {
        return "/funcexec/usecase/copy-select";
    }

    @RequestMapping(value = "/copyUsecasePage", method = RequestMethod.GET)
    public String copyUsecasePage() {
        return "/funcexec/usecase/usecase-copy-select";
    }

    //功能测试项树形列表数据
    @RequestMapping(value = "/tree")
    @ResponseBody
    public List<TreeNode> tree(@RequestParam("id") Long id, String type, String mark, String caseType) {
        if (id.intValue() == -1) {
            List<TestProject> testProjects;
            if("t".equals(caseType)){
                if("send".equals(mark)){
                    testProjects = testProjectDao.findSendProject();
                } else {
                    testProjects = testProjectDao.findRecvProject();
                }
            } else {
                if("send".equals(mark)){
                    testProjects = testProjectDao.findCaseSendProject();
                } else {
                    testProjects = testProjectDao.findCaseRecvProject();
                }
            }
            return projectToTreeNodes(testProjects);
        } else if ("project".equals(type)) {
            List<NxyFuncItem> items = nxyFuncItemDao.findByProjectIdAndMark(id,mark, caseType);
            return itemToTreeNodes(items);
        } else if ("item".equals(type)) {
            List<NxyFuncItem> items = nxyFuncItemDao.findByParentId(id);
            return itemToTreeNodes(items);
        }
        return new ArrayList<>();
    }

    private List<TreeNode> projectToTreeNodes(List<TestProject> testProjects) {
        List<TreeNode> nodes = new ArrayList<>();
        if (testProjects != null) {
            for (TestProject testProject : testProjects) {
                TreeNode node = new TreeNode(testProject.getId(), testProject.getName(), "closed", "project");
                nodes.add(node);
            }
        }
        return nodes;
    }

    private List<TreeNode> itemToTreeNodes(List<NxyFuncItem> items) {
        List<TreeNode> nodes = new ArrayList<>();
        if (items != null) {
            for (NxyFuncItem item : items) {
                String state = "closed";
                int count = nxyFuncItemDao.countByParentId(item.getId());
                if(count == 0) state = "open";
                TreeNode node = new TreeNode(item.getId(), item.getName(), state, "item");
                nodes.add(node);
            }
        }
        return nodes;
    }

    /**
     * 添加或修改功能测试项页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/addItemPage", method = RequestMethod.GET)
    public String addItemPage(Model model, String type){
        model.addAttribute("type", type);
        return "/funcexec/usecase/func-item-add";
    }

    /**
     * 根据Id获取功能测试项信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/item/{id}")
    @ResponseBody
    public NxyFuncItem get(@PathVariable("id") Long id) {
        NxyFuncItem nxyFuncItem = nxyFuncItemDao.findOne(id);
        return nxyFuncItem;
    }

    /**
     * 添加或修改功能测试项信息
     * @param nxyFuncItem
     * @param projectId
     * @param itemId
     * @return
     */
    @RequestMapping(value = "/itemAdd")
    @ResponseBody
    public Result addItem(NxyFuncItem nxyFuncItem, Long projectId, Long itemId) {
        Result result = null;
        try {
            //修改
            if (null != nxyFuncItem.getId()) {
                NxyFuncItem old = nxyFuncItemDao.findOne(nxyFuncItem.getId());
                nxyFuncItem.setParentId(old.getParentId());
                nxyFuncItem.setTestProject(old.getTestProject());
                nxyFuncItemDao.save(nxyFuncItem);
                log.info("@A|true|修改功能测试项成功!");
            } else {
                //项目下添加
                if (projectId != null) {
                    TestProject testProject = testProjectDao.findOne(projectId);
                    nxyFuncItem.setTestProject(testProject);
                    nxyFuncItemDao.save(nxyFuncItem);
                    if(!"recv".endsWith(nxyFuncItem.getMark())){
                        List<NxyFuncConfig> configs = nxyFuncConfigDao.findByItemId((long)-1);
                        List<NxyFuncConfig> configList = new ArrayList<>();
                        for(NxyFuncConfig config : configs){
                            NxyFuncConfig g = new NxyFuncConfig();
                            g.setItemId(nxyFuncItem.getId());
                            g.setVariableEn(config.getVariableEn());
                            g.setVariableZh(config.getVariableZh());
                            g.setVariableValue(config.getVariableValue());
                            configList.add(g);
                        }
                        nxyFuncConfigDao.save(configList);
                    }
                    log.info("@A|true|项目下添加功能测试项成功!");
                } else if (itemId != null) {  //功能测试项下添加
                    int count = nxyFuncUsecaseDao.countByItemId(itemId);
                    if(count == 0){  //当前功能测试项下没有用例是可以添加，否则不添加。
                        nxyFuncItem.setParentId(itemId);
                        NxyFuncItem parent = nxyFuncItemDao.findOne(itemId);
                        nxyFuncItem.setTestProject(parent.getTestProject());
                        nxyFuncItemDao.save(nxyFuncItem);
                        log.info("@A|true|功能测试项下添加功能测试项成功!");
                    } else {
                        result = new Result(false, "当前节点下有用例数据,添加子节点失败!");
                        return result;
                    }
                }
            }
            result = new Result(true, "");
        } catch (Exception e) {
            log.error("添加功能测试项失败！", e);
            result = new Result(false, "");
        }

        return result;
    }

    /**
     * 删除功能测试项
     * @param ids
     * @return
     */
    @RequestMapping(value = "/item/delete")
    @ResponseBody
    public Result delItem(@RequestParam("ids") Long[] ids) {
        Result result = null;
        try {
            if (null != ids && ids.length > 0) {
                funcExecService.delItems(ids);
            }
            result = new Result(true, "");
        } catch (Exception e) {
            log.error("删除测试项失败！", e);
            result = new Result(false, "");
        }
        return result;
    }

    /**
     * 删除功能测试项及其所有子节点
     * @param id
     * @return
     */
    @RequestMapping(value = "/item/del")
    @ResponseBody
    public Result delItem(@RequestParam("id") Long id) {
        Result result = null;
        try {
            if (null != id) {
                funcExecService.delByItemsId(id);
                log.info("@A|true|删除测试项成功!");
            }
            result = new Result(true, "");
        } catch (Exception e) {
            log.error("删除测试项失败！", e);
            result = new Result(false, "");
        }

        return result;
    }

    /**
     * 添加用例页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/addUsecasePage/{itemId}", method = RequestMethod.GET)
    public String addUsecasePage(Model model, @PathVariable("itemId") Long itemId){
        model.addAttribute("itemId", itemId);
        model.addAttribute("toolType", getToolType());
        return "/funcexec/usecase/func-usecase-add";
    }

    /**
     * 用例数据列表页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/usecaseDataListPage", method = RequestMethod.GET)
    public String usecaseListPage(Model model){
        return "/funcexec/usecase/func-usecase-data-list";
    }

    @RequestMapping("/usecaseDataList/{id}")
    @ResponseBody
    public List<NxyFuncUsecaseData> usecaseDataList(@PathVariable("id") Long id){
        if(id == 0){
            return new ArrayList<>();
        }
        List<NxyFuncUsecaseData> dataList = nxyFuncUsecaseDataDao.findByUsecaseId(id);
        return dataList;
    }

    /**
     * 报文列表页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/msgList", method = RequestMethod.GET)
    public String msgList(Model model, Long itemId, Long usecaseId){
        SimSystemInstance instance;
        if(itemId==0){
            NxyFuncUsecase usecase = nxyFuncUsecaseDao.findOne(usecaseId);
            instance = usecase.getNxyFuncItem().getTestProject().getInstance();
        } else {
            NxyFuncItem item = nxyFuncItemDao.findOne(itemId);
            instance = item.getTestProject().getInstance();
        }
        model.addAttribute("instanceName", instance.getName());
        model.addAttribute("instanceId", instance.getId());
        return "/funcexec/usecase/msg-list";
    }

    /**
     * 用例数据添加测试数据、预期值、业务回复值页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/usecaseDataPage", method = RequestMethod.GET)
    public String usecaseDataPage(Model model) {
        return "/funcexec/usecase/func-usecase-data";
    }

    /**
     * 用例测试数据展示页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/msgPage", method = RequestMethod.GET)
    public String msgPage(Model model){
        if("atsp".equals(getToolType())) {
            return "/funcexec/usecase/func-usecase-data-detail";
        }
        return "/funcexec/func-usecase-data-detail-form";
    }

    /**
     * 获取用例测试数据
     * @param messageId
     * @param usecaseDataId
     * @return
     */
    @RequestMapping(value = "/msgData", method = RequestMethod.POST)
    @ResponseBody
    public String msgData(Long messageId, Long usecaseDataId){
        String ret = "";
        if(messageId != null){
            SimSysInsMessage simSysInsMessage = simSysInsMessageDao.findOne(messageId);
            Long adapterId = simSysInsMessage.getSystemInstance().getAdapter().getId();
            String xml = simSysInsMessage.getModelFileContent();
            if(StringUtils.isEmpty(xml)) return ret;
            List<SimSysInsMessageField> fields = simSysInsMessage.getMsgFields();
            Document doc = XmlDocHelper.getXmlFromStr(xml);
            for (SimSysInsMessageField field:fields){
                String name = field.getMsgField().getFieldId();
                String valueType = field.getRespValueType();
                String valueValue = field.getRespValue();
                String defaultValue = field.getDefaultValue();
                if (StringUtils.isEmpty(valueType) || StringUtils.isEmpty(valueValue)){
                    XmlDocHelper.setNodeValue(doc,name,defaultValue);
                }else {
                    if (valueType.equals(Constants.MESSAGE_RESPONSE_FIELD_TYPE_DEFAULT)) {
                        XmlDocHelper.setNodeValue(doc,name,valueValue);
                    }else if (valueType.equals(Constants.MESSAGE_RESPONSE_FIELD_TYPE_INVOKE)){
                        String value = ReflectWraper.invoke(valueValue, "adapterId:" + adapterId);
                        if (StringUtils.isEmpty(value)){
                            XmlDocHelper.setNodeValue(doc,name,defaultValue);
                        }else {
                            XmlDocHelper.setNodeValue(doc,name,value);
                        }
                    }else {
                        XmlDocHelper.setNodeValue(doc,name,defaultValue);
                    }
                }
            }
            return XmlJsonHelper.XmltoJson(doc.asXML());
        }
        String modelXml = nxyFuncUsecaseDataDao.findOne(usecaseDataId).getMessageMessage();
        if (StringUtils.isEmpty(modelXml)){
            return ret;
        }
        ret = XmlJsonHelper.XmltoJson(modelXml);
        return ret;
    }

    @RequestMapping(value = "/msgDataNew", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,String>> msgDataNew(Long messageId, Long usecaseDataId){
        List<Map<String,String>> mapList = new ArrayList<>();
        if(messageId != null){
            SimSysInsMessage simSysInsMessage = simSysInsMessageDao.findOne(messageId);
            Long adapterId = simSysInsMessage.getSystemInstance().getAdapter().getId();
            String xml = simSysInsMessage.getModelFileContent();
            if(StringUtils.isEmpty(xml)) return mapList;
            List<SimSysInsMessageField> fields = simSysInsMessage.getMsgFields();
            for (SimSysInsMessageField field:fields){
                String name = field.getMsgField().getFieldId();
                String valueType = field.getRespValueType();
                String valueValue = field.getRespValue();
                String defaultValue = field.getDefaultValue();
                Map<String, String> map = new HashMap<>();
                map.put("fieldId", name);
                map.put("fieldName", field.getMsgField().getNameZh());
                map.put("moFlag", field.getMoFlag()==null?"0":field.getMoFlag()?"1":"0");
                if (StringUtils.isEmpty(valueType) || StringUtils.isEmpty(valueValue)){
                    map.put("fieldValue", defaultValue);
                }else {
                    if (valueType.equals(Constants.MESSAGE_RESPONSE_FIELD_TYPE_DEFAULT)) {
                        map.put("fieldValue", valueValue);
                    }else if (valueType.equals(Constants.MESSAGE_RESPONSE_FIELD_TYPE_INVOKE)){
                        String value = ReflectWraper.invoke(valueValue, "adapterId:" + adapterId);
                        if (StringUtils.isEmpty(value)){
                            map.put("fieldValue", defaultValue);
                        }else {
                            map.put("fieldValue", value);
                        }
                    }else {
                        map.put("fieldValue", defaultValue);
                    }
                }
                mapList.add(map);
            }
            return mapList;
        }
        NxyFuncUsecaseData data = nxyFuncUsecaseDataDao.findOne(usecaseDataId);
        SimSysInsMessage simSysInsMessage = data.getSimMessage();
        List<SimSysInsMessageField> fields = simSysInsMessage.getMsgFields();
        String modelXml = data.getMessageMessage();
        Document doc = XmlDocHelper.getXmlFromStr(modelXml);
        for (SimSysInsMessageField field:fields){
            String name = field.getMsgField().getFieldId();
            Map<String, String> map = new HashMap<>();
            map.put("fieldId", name);
            map.put("fieldName", field.getMsgField().getNameZh());
            map.put("fieldValue", XmlDocHelper.getNodeValue(doc, name));
            map.put("moFlag", field.getMoFlag()==null?"0":field.getMoFlag()?"1":"0");
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 预期值列表页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/expected", method = RequestMethod.GET)
    public String expected(Model model){
        return "/funcexec/usecase/expected-list";
    }

    /**
     * 返回预期值列表信息
     * @param usecaseDataId
     * @return
     */
    @RequestMapping(value = "/expectedList/{usecaseDataId}", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> expectedList(@PathVariable("usecaseDataId") Long usecaseDataId, String type) {
        List<NxyFuncUsecaseExpected> list = nxyFuncUsecaseExpectedDao.queryExpectedList(usecaseDataId, type);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for(NxyFuncUsecaseExpected expected : list){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", expected.getId());
            map.put("fieldId", expected.getMsgField().getFieldId());
            map.put("nameZh", expected.getMsgField().getNameZh());
            map.put("expectedValue", expected.getExpectedValue());
            result.add(map);
        }
        return result;
    }

    @RequestMapping(value = "/rules/{usecaseDataId}", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> rules(@PathVariable("usecaseDataId") Long usecaseDataId){
        List<NxyFuncUsecaseDataRule> list = ruleDao.findByUcDataId(usecaseDataId);
        List<Map<String,Object>> ret = new ArrayList<>();
        for (NxyFuncUsecaseDataRule rule:list){
            Map<String,Object> map = new HashMap<>();
            map.put("id",rule.getId());
            map.put("destFieldId",rule.getDestFieldId());
            MsgField field = msgFieldDao.findByFieldId(rule.getDestFieldId());
            map.put("destFieldZh",field.getNameZh());
            map.put("srcFieldId",rule.getSrcFieldId());
            NxyFuncUsecaseData data = nxyFuncUsecaseDataDao.findOne(rule.getUsecaseDataId());
            map.put("destMsgCode", data.getSimMessage().getMsgType());
            SimSysInsMessage message = simSysInsMessageDao.findOne(rule.getSrcMsgId());
            map.put("srcMsgCode", message.getMsgType());
            if(StringUtils.isNotEmpty(rule.getSrcFieldId())){
                map.put("srcFieldZh",msgFieldDao.findByFieldId(rule.getSrcFieldId()).getNameZh());
            }
            map.put("from",rule.getSrcSendRecv().equalsIgnoreCase("send")?"发送报文":"应答报文");
            ret.add(map);
        }
        return ret;
    }

    /**
     * 取值规则添加页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/ruleAddPage", method = RequestMethod.GET)
    public String ruleAddPage(Model model){
        return "/funcexec/usecase/rule-add";
    }

    @RequestMapping(value = "rules/addRule", method = RequestMethod.POST)
    @ResponseBody
    public Result addRule(NxyFuncUsecaseDataRule rule){
        Result ret = new Result();
        try{
            if (rule.getId() == null){
                ruleDao.save(rule);
            }else {
                NxyFuncUsecaseDataRule r = ruleDao.findOne(rule.getId());
                r.setSrcSendRecv(rule.getSrcSendRecv());
                r.setSrcFieldId(rule.getSrcFieldId());
                r.setEvaluateType(rule.getEvaluateType());
                r.setDestFieldId(rule.getDestFieldId());
                ruleDao.save(r);
            }
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
        }
        return ret;
    }

    @RequestMapping(value = "/deleteRules", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteRules(@RequestParam("ids[]") Long[] ids){
        Result ret = new Result();
        try{
            if(ids != null && ids.length>0){
                ruleDao.delByIds(Arrays.asList(ids));
            }
        } catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("删除失败!");
        }
        return ret;
    }

    /**
     * 删除预期值或业务返回值
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteExcepted", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteExcepted(@RequestParam("ids[]") Long[] ids){
        Result ret = new Result();
        try{
            if(ids != null && ids.length>0){
                nxyFuncUsecaseExpectedDao.delByIds(Arrays.asList(ids));
            }
        } catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("删除失败!");
        }
        return ret;
    }

    /**
     * 删除预期值或业务返回值
     * @return
     */
    @RequestMapping(value = "/saveExpected", method = RequestMethod.POST)
    @ResponseBody
    public Result saveExpected(Long usecaseDataId, Long fieldId, String fieldIdName, String expectedValue, String type) {
        Result ret = new Result();
        try {
            NxyFuncUsecaseExpected expected = new NxyFuncUsecaseExpected();
            expected.setUsecaseDataId(usecaseDataId);
            expected.setFieldIdName(fieldIdName);
            expected.setExpectedValue(expectedValue);
            MsgField field = msgFieldDao.findOne(fieldId);
            expected.setMsgField(field);
            expected.setType(type);
            nxyFuncUsecaseExpectedDao.save(expected);
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
        }
        return  ret;
    }

    /**
     * 域列表页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/field", method = RequestMethod.GET)
    public String field(Model model){
        return "/funcexec/usecase/field-list";
    }

    /**
     * 返回预期值列表信息
     * @param params
     * @param page
     * @return
     */
    @RequestMapping(value = "/fieldList/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> fieldList(@PathVariable("id") Long id, @RequestParam Map<String, Object> params, @PageableDefault Pageable page) {
        Map<String, Object> map = funcExecService.queryNxyFieldList(id, params, page);
        return map;
    }

    /**
     * 预期值填写页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/addExcepted", method = RequestMethod.GET)
    public String addExcepted(Model model){
        return "/funcexec/usecase/expected-set";
    }

    /**
     * 添加功能测试用例
     * @param usecase
     * @param usecaseAllDataList
     * @return
     */
    @RequestMapping(value = "/addUsecase", method = RequestMethod.POST)
    @ResponseBody
    public Result addUsecase(NxyFuncUsecase usecase, String usecaseAllDataList, Long itemId) {
        Result result = null;
        try {
            if(usecase.getId() != null){
                NxyFuncUsecase nxyFuncUsecase = nxyFuncUsecaseDao.findOne(usecase.getId());
                nxyFuncUsecase.setSeqNo(usecase.getSeqNo());
                nxyFuncUsecase.setStep(usecase.getStep());
                nxyFuncUsecase.setPurpose(usecase.getPurpose());
                nxyFuncUsecase.setExpected(usecase.getExpected());
                nxyFuncUsecase.setCaseNumber(usecase.getCaseNumber());
                nxyFuncUsecaseDao.save(nxyFuncUsecase);
                result = new Result(true, "");
            } else {
                usecaseAllDataList = URLDecoder.decode(usecaseAllDataList);
                result = funcExecService.addUsecase(usecase, usecaseAllDataList, itemId);
            }
            log.info("@A|true|添加功能测试用例成功!");
        } catch (Exception e) {
            log.error("添加功能测试用例失败！", e);
            result = new Result(false, "");
        }

        return result;
    }

    /**
     * 获取功能测试用例
     * @param id
     * @return
     */
    @RequestMapping(value = "/getUsecase")
    @ResponseBody
    public NxyFuncUsecase getUsecase(Long id) {
        NxyFuncUsecase usecase = nxyFuncUsecaseDao.findOne(id);
        return usecase;
    }

    /**
     * 根据功能测试项获取其用例列表数据
     * @param params
     * @param page
     * @return
     */
    @RequestMapping(value = "/getUsecaseByItem")
    @ResponseBody
    public Map<String, Object> getUsecaseByItem(@RequestParam Map<String, Object> params, @PageableDefault Pageable page) {
        Pageable p = page;
        if (page != null) {
            p = new PageRequest(page.getPageNumber() < 1 ? 0 : page.getPageNumber() - 1, page.getPageSize(), page.getSort());
        }
        if (params.size() > 2) {
            Map<String, Object> result = genericService.commonQuery("nxyFuncUsecase", params, p);
            List<NxyFuncUsecase> data = (List<NxyFuncUsecase>) result.get("rows");
            List<Map<String, Object>> list = new ArrayList<>();
            if (data != null) {
                for (NxyFuncUsecase nxyFuncUsecase : data) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", nxyFuncUsecase.getId());
                    map.put("no", nxyFuncUsecase.getNo());
                    map.put("caseNumber", nxyFuncUsecase.getCaseNumber());
                    map.put("purpose", nxyFuncUsecase.getPurpose());
                    map.put("step", nxyFuncUsecase.getStep());
                    map.put("expected", nxyFuncUsecase.getExpected());
                    map.put("resultDis",nxyFuncUsecase.getResultDis());
                    int number = nxyFuncUsecaseExecDao.countByUsecaseId(nxyFuncUsecase.getId());
                    map.put("number", number);
                    list.add(map);
                }
                data = null;
                result.put("rows", list);
            }
            return result;
        }
        return null;
    }

    /**
     * 删除用例
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delUsecase", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteItemData(@RequestParam("ids[]") Long[] ids){
        Result ret = new Result();
        try {
            if(ids != null && ids.length > 0){
                nxyFuncUsecaseDao.deleteUsecaseByIds(Arrays.asList(ids));
            }
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("删除失败");
        }
        return  ret;
    }

    /**
     * 修改用例数据的测试数据
     * @param msg
     * @param usecaseDataId
     * @return
     */
    @RequestMapping(value = "/updateUsecaseDataMsg", method = RequestMethod.POST)
    @ResponseBody
    public Result updateUsecaseDataMsg(String msg, Long usecaseDataId) {
        Result result = null;
        try {
            msg = URLDecoder.decode(msg);
            NxyFuncUsecaseData data = nxyFuncUsecaseDataDao.findOne(usecaseDataId);
            if("atsp".equals(getToolType())) {
                String strXml = XmlJsonHelper.JsonToXml(msg);
                strXml = XmlDocHelper.xmlZuZhuang(strXml, data.getSimMessage());
                data.setMessageMessage(strXml);
            } else {
                String xml = data.getSimMessage().getModelFileContent();
                Document doc = XmlDocHelper.getXmlFromStr(xml);
                Map<String, String> map = MapUtils.getMapByStr(msg);
                xml = XmlDocHelper.getXmlByMap(map, doc);
                data.setMessageMessage(xml);
            }
            nxyFuncUsecaseDataDao.save(data);
            log.info("@A|true|修改用例数据的测试数据成功!");
            result = new Result();
        } catch (Exception e) {
            log.error("修改用例数据的测试数据失败！", e);
            result = new Result(false, "");
        }
        return result;
    }

    /**
     * 保存用例数据
     * @return
     */
    @RequestMapping(value = "/saveUsecaseData", method = RequestMethod.POST)
    @ResponseBody
    public Result saveUsecaseData(NxyFuncUsecaseData data, String usecaseDataList, String expectedDataList, String replyDataList, String ruleDataList, Long usecaseId, Long messageId) {
        Result ret = null;
        try{
            usecaseDataList = URLDecoder.decode(usecaseDataList);
            ret = funcExecService.saveUsecaseData(data, usecaseDataList, expectedDataList, replyDataList, ruleDataList, usecaseId, messageId);
        } catch (Exception e){
            e.printStackTrace();
            ret = new Result(false, "添加数据失败");
        }
        return ret;
    }

    /**
     * 删除用例数据
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delUsecaseData", method = RequestMethod.POST)
    @ResponseBody
    public Result delUsecaseData(@RequestParam("ids[]") Long[] ids){
        Result ret = new Result();
        try {
            if(ids != null && ids.length > 0){
                funcExecService.delUsecaseDatas(Arrays.asList(ids));
            }
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("删除失败");
        }
        return  ret;
    }

    /**
     * 复制测试项
     * @param itemIds 复制数据
     * @param id  目标Id
     * @return
     */
    @RequestMapping(value = "/item/copy", method = RequestMethod.POST)
    @ResponseBody
    public Result copy(@RequestParam("itemIds[]") Long[] itemIds, @RequestParam("id") Long id, @RequestParam("type") String type, Long instanceId) {
        Result ret = new Result();
        try{
            ret = funcUsecaseService.copyItem(itemIds, id, type, instanceId);
        } catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg(e.getMessage());
        }
        return ret;
    }

    /**
     * 复制测试用例
     * @param usecaseIds 复制数据
     * @param id  目标Id
     * @return
     */
    @RequestMapping(value = "/usecase/copy", method = RequestMethod.POST)
    @ResponseBody
    public Result usecasecopy(@RequestParam("usecaseIds[]") Long[] usecaseIds, @RequestParam("id") Long id, Long instanceId) {
        Result ret = new Result();
        try{
            ret = funcUsecaseService.copyUsecase(usecaseIds, id, instanceId);
        } catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg(e.getMessage());
        }
        return ret;
    }

    /**
     * 取值规则列表页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/ruleListPage", method = RequestMethod.GET)
    public String ruleListPage(Model model){
        return "/funcexec/usecase/rule-list";
    }

    /*
   查看每个报文的组成域
    */
    @RequestMapping(value = "/getFields")
    @ResponseBody
    public List<Map<String, Object>> getFields(Long id) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (id == null) {
            return list;
        }
//        List<SimSysInsMessageField> fields = simSysInsMessageFieldService.getAllFields(id);
        SimSysInsMessage sysInsMessage = simSysInsMessageDao.findOne(id);
        List<SimSysInsMessageField> fields = sysInsMessage.getMsgFields();
        for(SimSysInsMessageField field : fields){
            Map<String, Object> map = new HashMap<>();
            map.put("fieldId", field.getMsgField().getFieldId());
            map.put("nameZh", field.getMsgField().getNameZh());
            map.put("msgCode", sysInsMessage.getMsgType());
            list.add(map);
        }
        return list;
    }

    /*
   查看每个报文的组成域
    */
    @RequestMapping(value = "/recvFieldList")
    @ResponseBody
    public List<Map<String, Object>> recvFieldList(Long id) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (id == null) {
            return list;
        }
        List<Object> replyRule = simSysInsReplyRuleDao.findReplyMsgs(id);
        if(replyRule.isEmpty()) return list;
        for(Object obj : replyRule){
            Long resp = Long.parseLong(obj.toString());
            SimSysInsMessage sysInsMessage = simSysInsMessageDao.findOne(resp);
            List<SimSysInsMessageField> fields = sysInsMessage.getMsgFields();
            for(SimSysInsMessageField field : fields){
                Map<String, Object> map = new HashMap<>();
                map.put("fieldId", field.getMsgField().getFieldId());
                map.put("nameZh", field.getMsgField().getNameZh());
                map.put("msgCode", sysInsMessage.getMsgType());
                list.add(map);
            }
        }
        return list;
    }

    @RequestMapping(value = "/assign")
    public String assign(Model model, Long itemId){
        model.addAttribute("itemId", itemId);
        return "/funcexec/usecase/func_item_assign";
    }


    @RequestMapping(value = "/getUnselectedUser")
    @ResponseBody
    public List<Map<String, Object>> getUnselectedUser(Long itemId){
        return funcExecService.getUnselectedUser(itemId);
    }

    @RequestMapping(value = "/getSelectedUser")
    @ResponseBody
    public List<Map<String, Object>> getSelectedUser(Long itemId){
        return funcExecService.getSelectedUser(itemId);
    }

    @RequestMapping(value = "/addMembers")
    @ResponseBody
    public Result addMembers(@RequestParam("ids[]") Long[] ids, @RequestParam("itemId") Long itemId){
        Result ret = new Result();
        if(ids != null && ids.length > 0){
            funcExecService.insertNxyFuncItemUser(ids, itemId);
        }
        return ret;
    }

    @RequestMapping("/updateExpected")
    public String updateExpected(){
        return "/funcexec/usecase/expected-update";
    }

    /**
     * 删除预期值或业务返回值
     * @return
     */
    @RequestMapping(value = "/updateExpected", method = RequestMethod.POST)
    @ResponseBody
    public Result updateExpected(NxyFuncUsecaseExpected expected) {
        Result ret = new Result();
        try {
            NxyFuncUsecaseExpected expected1 = nxyFuncUsecaseExpectedDao.findOne(expected.getId());
            expected1.setExpectedValue(expected.getExpectedValue());
            nxyFuncUsecaseExpectedDao.save(expected1);
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
        }
        return  ret;
    }

    @RequestMapping("/selectInstancePage")
    public String selectInstancePage(){
        return "/funcexec/usecase/instance-select";
    }

    @RequestMapping(value = "/selectInstance", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> selectInstance(Long id, String type){
        if(!type.equalsIgnoreCase("project")){
            id = nxyFuncItemDao.findOne(id).getTestProject().getId();
        }
        List<Map<String,Object>> ret = new ArrayList<>();
        TestProject pro = testProjectDao.findOne(id);
        Map<String, Object> map = new HashMap<>();
        map.put("id",pro.getInstance().getId());
        map.put("name",pro.getInstance().getName());
        ret.add(map);
        if (ret.size()>0){
            ret.get(0).put("selected",true);
        }
        return ret;
    }
}
