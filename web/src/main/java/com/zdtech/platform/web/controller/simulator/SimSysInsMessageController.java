package com.zdtech.platform.web.controller.simulator;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.service.GenericService;
import com.zdtech.platform.framework.service.UcodeService;
import com.zdtech.platform.framework.utils.XMLConverter;
import com.zdtech.platform.framework.utils.XmlDocHelper;
import com.zdtech.platform.framework.utils.XmlToJson;
import com.zdtech.platform.service.simulator.*;
import com.zdtech.platform.utils.Constants;
import com.zdtech.platform.utils.reflect.InvokedClass;
import com.zdtech.platform.utils.reflect.ReflectWraper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SimSysInsMessageController
 *
 * @author xiaolanli
 * @date 2016/5/18
 */
@Controller
@RequestMapping("/sim/instance/message")
public class SimSysInsMessageController {
    private static Logger logger = LoggerFactory.getLogger(SimSysInsMessageController.class);

    private static Map<Long, Boolean> map = new ConcurrentHashMap<>();
    @Autowired
    private SimSysInsMessageService simSysInsMessageService;
    @Autowired
    private SimSysInsMessageFieldService simSysInsMessageFieldService;
    @Autowired
    private SimSystemIntanceService simSystemIntanceService;
    @Autowired
    private SimSysInsReplyRuleService simSysInsReplyRuleService;
    @Autowired
    private SimMessageService simMessageService;
    @Autowired
    private UcodeService ucodeService;
    @Autowired
    private SimSysinsMsgFieldValueDao simSysinsMsgFieldValueDao;
    @Autowired
    private SimMessageController simMessageController;
    @Autowired
    private SimSysInsMessageDao simSysInsMessageDao;
    @Autowired
    private MsgFieldDao msgFieldDao;
    @Autowired
    private SimSysInsMessageFieldDao simSysInsMessageFieldDao;
    @Autowired
    private SimSysInsMsgFieldCodeDao simSysInsMessageFieldCodeDao;
    @Autowired
    private GenericService genericService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(String type, Model model) {
        model.addAttribute("type", type);
        if (type.equals("SELF")) {
            return "simulator/instancemessage/instanceself/instance-self-list";
        } else {
            return "simulator/instancemessage/instancexml/instance-xml-list";
        }
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> query(@RequestParam Map<String, Object> params, @PageableDefault Pageable page) {
        Map<String, Object> ret = new HashMap<>();
        ret = simSysInsMessageService.findMessages(params, page);
        return ret;
    }

    /*
   查看每个报文的组成域
    */
    @RequestMapping(value = "/getFields", method = RequestMethod.POST)
    @ResponseBody
    public List<SimSysInsMessageField> getFields(@RequestParam(required = false) Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        return simSysInsMessageFieldService.getAllFields(id);
    }

    @RequestMapping(value = "/addMessage", method = RequestMethod.GET)
    public String addMessage(String type, Model model) {
        model.addAttribute("type", type);
        if (type.equals("SELF")) {
            return "simulator/instancemessage/instanceself/instance-self-add";
        } else {
            return "simulator/instancemessage/instancexml/instance-xml-add";
        }
    }

    @RequestMapping(value = "/editField/{id}", method = RequestMethod.GET)
    public String editField(@PathVariable("id") Long id, Model model) {
        SimSysInsMessageField simSysInsMessageField = simSysInsMessageFieldDao.getOne(id);
        model.addAttribute("simSysInsMessageField", simSysInsMessageField);
        return "simulator/instancemessage/instanceself/instance-self-field-type-edit";
    }

    @RequestMapping(value = "/editField", method = RequestMethod.POST)
    @ResponseBody
    public void editField(SimSysInsMessageField simSysInsMessageField,String simSysInsMessageFieldId){
        SimSysInsMessageField simSysInsMessageFieldInitial = simSysInsMessageFieldService.findOne(Long.parseLong(simSysInsMessageFieldId));
        simSysInsMessageFieldInitial.setFieldValueType(simSysInsMessageField.getFieldValueType());
        simSysInsMessageFieldInitial.setFieldValueTypeParam(simSysInsMessageField.getFieldValueTypeParam());
        simSysInsMessageFieldDao.save(simSysInsMessageFieldInitial);
        simSysInsMessageFieldCodeDao.deleteBySimMessageFieldId(Long.parseLong(simSysInsMessageFieldId));
    }

    @RequestMapping(value = "/enum/{id}", method = RequestMethod.POST)
    @ResponseBody
    public List<SimSysInsMsgFieldCode> getCodes(@PathVariable("id") Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        return simSysInsMessageFieldDao.findOne(id).getMsgFieldCodes();
    }

    @RequestMapping(value = "/addCode/", method = RequestMethod.GET)
    public String addCode(String codeId, String simSysInsMessageFieldId, String type,Model model) {
        model.addAttribute("type", type);
        model.addAttribute("simSysInsMessageFieldId", simSysInsMessageFieldId);
        model.addAttribute("codeId", codeId);
        return "simulator/instancemessage/instanceself/instance-self-field-code-add";
    }

    @RequestMapping(value = "/addCode", method = RequestMethod.POST)
    @ResponseBody
    public void addCode(SimSysInsMsgFieldCode simSysInsMsgFieldCode,String simSysInsMessageFieldId){
        SimSysInsMessageField simSysInsMessageField = simSysInsMessageFieldDao.findOne(Long.parseLong(simSysInsMessageFieldId));
        simSysInsMessageField.setFieldValueType("enum");
        simSysInsMessageField.setFieldValueTypeParam(null);
        simSysInsMessageFieldDao.save(simSysInsMessageField);
        simSysInsMsgFieldCode.setMsgField(simSysInsMessageField);
        simSysInsMessageFieldCodeDao.save(simSysInsMsgFieldCode);
    }

    @RequestMapping(value = "/getCurrentParam/", method = RequestMethod.GET)
    @ResponseBody
    public SimSysInsMsgFieldCode getCurrentParam(String codeId){
        SimSysInsMsgFieldCode simSysInsMsgFieldCode = simSysInsMessageFieldCodeDao.findOne(Long.parseLong(codeId));
        return simSysInsMsgFieldCode;
    }

    @RequestMapping(value = "/delCodes", method = RequestMethod.POST)
    @ResponseBody
    public Result delCodes(@RequestParam("ids[]")Long[] ids){
        Result ret = new Result();
        try {
            for (Long id:ids){
                simSysInsMessageFieldCodeDao.delete(id);
            }
            logger.info("@P|true|删除项目成功");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@P|false|删除项目失败");
        }
        return ret;
    }

    /*
     查询所有的仿真实例名称
     */
    @RequestMapping(value = "/getAllsim")
    @ResponseBody
    public List<Map<String, String>> getAllsim(String type) {
        List<Map<String, String>> list = new ArrayList<>();
        List<SimSystemInstance> systemList = simSystemIntanceService.getAllInstance(type);
        List<Map<String, String>> rnt = new ArrayList<Map<String, String>>();
        if (systemList == null || systemList.isEmpty())
            return rnt;
        Map<String, String> map = null;
        for (SimSystemInstance simSystemInstance : systemList) {
            map = new HashMap<String, String>();
            map.put("id", String.valueOf(simSystemInstance.getId()));
            map.put("text", simSystemInstance.getName());
            rnt.add(map);
        }
        return rnt;
    }

    /*
    查询要修改的报文信息
     */
    @RequestMapping(value = "/get/{id}")
    @ResponseBody
    public SimSysInsMessage get(@PathVariable Long id) {
        return simSysInsMessageService.get(id);
    }

    /*
   增加新的self报文（包含报文的基本信息以及所有的报文头和通用业务要素集域）
    */
    @RequestMapping(value = "/addself/{simid}/{generalid}", method = RequestMethod.POST)
    @ResponseBody
    public Result addSelfMessage(@PathVariable Long simid, @PathVariable Long generalid, SimSysInsMessage simMessage) {
        Result ret = new Result();
        try {
            ret = simSysInsMessageService.addSimMessage(simid, generalid, simMessage);
            logger.info("@C|true|添加实例成功！");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|添加实例失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/updateMessage", method = RequestMethod.GET)
    public String updateMessage(String type, Model model) {
        model.addAttribute("type", type);
        if (type.equals("SELF")) {
            return "simulator/instancemessage/instanceself/instance-self-update";
        } else {
            return "simulator/instancemessage/instancexml/instance-xml-add";
        }
    }

    /*
  修改的self报文（包含报文的基本信息）
   */
    @RequestMapping(value = "/updateself/{simid}", method = RequestMethod.POST)
    @ResponseBody
    public Result updateself(@PathVariable Long simid, SimSysInsMessage simMessage) {
        Result ret = new Result();
        try {
            ret = simSysInsMessageService.updateSimMessage(simid, simMessage);
            logger.info("@C|true|修改实例成功！");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|修改实例失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/getResponseMsgs", method = RequestMethod.POST)
    @ResponseBody
    public List<SimSysInsMessage> getResponseMsgs(Long requestId){
        return simSysInsReplyRuleService.getResponseMsgsByRequestId(requestId);
    }
    @RequestMapping(value = "/getReponseRule", method = RequestMethod.GET)
    public String getReponseRule(String type, Model model) {
        model.addAttribute("type", type);
        if (type.equals("SELF")) {
            return "simulator/instancemessage/instanceself/self-reply-list";
        } else {
            return "simulator/instancemessage/instancexml/xml-reply-list";
        }
    }
    @RequestMapping(value = "/listReponseMsgs", method = RequestMethod.GET)
    public String listReponseMsgs(String type,Model model){
        model.addAttribute("type", type);
        if (type.equals("SELF")) {
            return "simulator/instancemessage/instanceself/self-reply-select-list";
        } else {
            return "simulator/instancemessage/instancexml/xml-reply-select-list";
        }
    }
    @RequestMapping(value = "/getAllResponseMsgs", method = RequestMethod.POST)
    @ResponseBody
    public List<SimSysInsMessage> listResponseMsgs(Long requestId){
        List<SimSysInsMessage> list = simSysInsMessageService.findSystemMsgs(requestId);
        return  list;
    }
    @RequestMapping(value = "/addResponses", method = RequestMethod.POST)
    @ResponseBody
    public Result addResponses(Long requestId,@RequestParam("responseIds[]") Long[] ids,String param){
        Result ret = new Result();
        try {
            simSysInsReplyRuleService.addReplyRules(requestId,ids,param);
            ret.setSuccess(true);
            ret.setMsg("操作成功");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
        }
        return ret;
    }
    @RequestMapping(value = "/deleteResponses", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteResponses(Long requestId,@RequestParam("ids[]") Long[] ids){
        Result ret = new Result();
        try {
            simSysInsReplyRuleService.deleteReplyRules(requestId,ids);
            ret.setSuccess(true);
            ret.setMsg("操作成功");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
        }
        return ret;
    }
    @RequestMapping(value = "/listReponseMsgParam", method = RequestMethod.GET)
    public String listReponseMsgParam(String type){
        return "simulator/instancemessage/reply-rule-param";
    }



    @RequestMapping(value = "/setReponseRule", method = RequestMethod.GET)
    public String setReponseRule(String type, Model model) {
        model.addAttribute("type", type);
        if (type.equals("SELF")) {
            return "simulator/instancemessage/instanceself/instance-self-replyset-list";
        } else {
            return "simulator/instancemessage/instancexml/instance-xml-replyset-list";
        }
    }

    @RequestMapping(value = "/addReplyRule", method = RequestMethod.POST)
    @ResponseBody
    public Result addReplyRule(Long requestid, @RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        try {
            List<Long> list = Arrays.asList(ids);
            simSysInsReplyRuleService.saveReplyRule(requestid, list);
            ret.setSuccess(true);
            logger.info("@C|true|设置应答规则成功！");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|设置应答规则失败！");
        }

        return ret;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delSimMessage(@RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        try {
            List<Long> list = Arrays.asList(ids);
            simSysInsMessageService.deleteSimMessages(list);
            ret.setSuccess(true);
            logger.info("@C|true|删除实例成功！");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|删除实例失败！");
        }
        return ret;
    }

    /*
    业务要素页面
     */
    @RequestMapping(value = "/fieldList", method = RequestMethod.GET)
    public String fieldList(String id, String type, Model model) {
        model.addAttribute("simMessageid", id);
        model.addAttribute("type", type);
        return "simulator/instancemessage/instanceself/instance-self-field-list";
    }

    /*
  增加报文的扩展域
   */
    @RequestMapping(value = "/addFields", method = RequestMethod.POST)
    @ResponseBody
    public Result addFields(Long simMessageid, @RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        try {
            List<Long> list = Arrays.asList(ids);
            simSysInsMessageFieldService.addFields(simMessageid, list);
            logger.info("@C|true|添加业务要素成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|添加业务要素失败！");
        }
        return ret;
    }

    /*
    删除报文的扩展域
     */
    @RequestMapping(value = "/delFields", method = RequestMethod.POST)
    @ResponseBody
    public Result delFields(Long simMessageId, @RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        try {
            List<Long> list = Arrays.asList(ids);
            simSysInsMessageFieldService.delFields(simMessageId, list);
            logger.info("@C|true|删除业务要素成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|删除业务要素失败！");
        }
        return ret;
    }

    /*
     * 批量加签
     */
    @RequestMapping(value = "/signFields", method = RequestMethod.POST)
    @ResponseBody
    public Result signFields(Long simMessageId, @RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        try {
            List<Long> list = Arrays.asList(ids);
            simSysInsMessageFieldService.signFields(simMessageId, list);
            logger.info("@C|true|加签业务要素成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|加签业务要素失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/addMoFlagFields", method = RequestMethod.POST)
    @ResponseBody
    public Result addMoFlagFields(Long simMessageId, @RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        try {
            List<Long> list = Arrays.asList(ids);
            simSysInsMessageFieldService.addMoFlagFields(simMessageId, list);
            logger.info("@C|true|业务要素设置必填成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|业务要素设置必填失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/field/get/{id}")
    @ResponseBody
    public SimSysInsMessageField getField(@PathVariable Long id) {
        return simSysInsMessageFieldService.findOne(id);
    }

    /*
        修改组成域的值
         */
    @RequestMapping(value = "/field/edit", method = RequestMethod.GET)
    public String editfield(String id, String type, Model model) {
        model.addAttribute("simMessageid", id);
        model.addAttribute("type", type);
        if (type.equals("SELF")) {
            return "simulator/instancemessage/instanceself/instance-self-field-comp-edit";
        } else {
            return "simulator/instancemessage/instancexml/instance-xml-field-comp-edit";
        }
    }

    @RequestMapping(value = "/field/edit", method = RequestMethod.POST)
    @ResponseBody
    public Result editField(Long id, SimSysInsMessageField field) {
        Result ret = new Result();
        try {
            simSysInsMessageFieldService.saveFiled(id, field);
            ret.setSuccess(true);
            logger.info("@C|true|修改业务要素成功！");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|修改业务要素失败！");
        }

        return ret;
    }

    @RequestMapping(value = "/addXml/{simid}", method = RequestMethod.POST)
    @ResponseBody
    public String addXml(@PathVariable Long simid, SimSysInsMessage simMessage) {

        Long id = simSysInsMessageService.addXmlMessage(simid, simMessage);

        return id + "";
    }

    @RequestMapping(value = "/addInstanceMessage", method = RequestMethod.GET)
    public String addInstanceMessage(String type, Model model) {
        model.addAttribute("type", type);
        if (type.equals("SELF")) {
            return "simulator/instancemessage/instanceself/instance-self-messageSelected";
        } else {
            return "simulator/instancemessage/instancexml/instance-xml-messageSelected";
        }
    }

    /*
  查看实例所属系统下的所有报文
   */
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    @ResponseBody
    public List<SimMessage> findByInstanceId(@RequestParam(required = false) Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        return simMessageService.getMessages(id);
    }

    /*
  增加实例报文
   */
    @RequestMapping(value = "/addInstanceMessage", method = RequestMethod.POST)
    @ResponseBody
    public Result addInstanceMessage(Long simid, @RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        if (map.get(simid) != null) {
            ret.setMsg("其它用户正在添加报文，请稍后再进行操作");
            ret.setSuccess(false);
            return ret;
        }
        try {
            map.put(simid, Boolean.TRUE);
            List<Long> list = Arrays.asList(ids);
            List<String> messagelist = simSysInsMessageService.addInstanceMessage(simid, list);
            if (messagelist.size() == 0) {
                ret = new Result(true, "报文添加成功");
                logger.info("@C|true|从仿真系统中添加报文成功！");
            } else {
                StringBuilder result = new StringBuilder();
                boolean flag = false;
                for (String string : messagelist) {
                    if (flag) {
                        result.append(",");
                    } else {
                        flag = true;
                    }
                    result.append(string);
                }
                ret = new Result(false, result.toString() + "报文已经存在");
                logger.warn("@C|false|报文已经存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = new Result(false, "操作失败");
            logger.error("@C|false|从仿真系统中添加报文失败！");
        } finally {
            map.remove(simid);
        }
        return ret;
    }
    @RequestMapping(value = "/addAllInstanceMessage", method = RequestMethod.POST)
    @ResponseBody
    public Result addAllInstanceMessage(Long simInsId, @RequestParam Map<String,Object> searchParam) {
        Result ret = new Result();
        if (map.get(simInsId) != null) {
            ret.setMsg("其它用户正在添加报文，请稍后再进行操作");
            ret.setSuccess(false);
            return ret;
        }
        try {
            searchParam.remove("simInsId");
            Object osimsys = searchParam.get("simSysId");
            Object oname = searchParam.get("name");
            Object ocode = searchParam.get("trsCode");
            Object otype = searchParam.get("mesgType");
            if (osimsys != null && StringUtils.isNotEmpty(osimsys.toString())){
                searchParam.put("simSystem.id",osimsys.toString());
            }
            if (oname != null && StringUtils.isNotEmpty(oname.toString())){
                searchParam.put("name|like",oname.toString());
            }
            if (ocode != null && StringUtils.isNotEmpty(ocode.toString())){
                searchParam.put("trsCode|like",ocode.toString());
            }
            if (otype != null && StringUtils.isNotEmpty(otype.toString())){
                searchParam.put("mesgType|like",otype.toString());
            }
            searchParam.remove("simSysId");
            searchParam.remove("name");
            searchParam.remove("trsCode");
            searchParam.remove("mesgType");
            Map<String,Object> serchresult = genericService.commonQuery("simMessage",searchParam,null);
            Object orows = serchresult.get("rows");
            if (orows == null){
                ret.setMsg("请选择要导入的报文");
                ret.setSuccess(false);
                return ret;
            }
            List l = (List)orows;
            List<Long> list = new ArrayList<>();
            for (Object o:l){
                list.add(((SimMessage)o).getId());
            }
            map.put(simInsId, Boolean.TRUE);
            List<String> messagelist = simSysInsMessageService.addInstanceMessage(simInsId, list);
            if (messagelist.size() == 0) {
                ret = new Result(true, "报文添加成功");
                logger.info("@C|true|从仿真系统中添加报文成功！");
            } else {
                StringBuilder result = new StringBuilder();
                boolean flag = false;
                for (String string : messagelist) {
                    if (flag) {
                        result.append(",");
                    } else {
                        flag = true;
                    }
                    result.append(string);
                }
                ret = new Result(false, result.toString() + "报文已经存在");
                logger.warn("@C|false|报文已经存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = new Result(false, "操作失败");
            logger.error("@C|false|从仿真系统中添加报文失败！");
        } finally {
            map.remove(simInsId);
        }
        return ret;
    }


    @RequestMapping(value = "/getSimSystem", method = RequestMethod.POST)
    @ResponseBody
    public Long getSimSystem(Long siminsId) {
        Long id = null;
        try {
            id = simSystemIntanceService.get(siminsId).getSimSystem().getId();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return id;
    }

    @RequestMapping(value = "/getXml")
    @ResponseBody
    public String getXml(Long id) {
        List<Map<String, Object>> ret = new ArrayList<>();
        SimSysInsMessage simSysInsMessage = simSysInsMessageService.get(id);
        Map filedMap = new HashMap();
        List<SimSysInsMessageField> list = simSysInsMessageFieldService.getAllFields(id);
        if (list != null && list.size() > 0) {
            for (SimSysInsMessageField field : list) {
                if (simSysInsMessage.getType().equals("XML") && field.getFieldType().equals("XMLBODY")) {
                    filedMap.put(field.getMsgField().getFieldId(), field.getDefaultValue());
                }
            }
        }
        XmlToJson xmlToJson = new XmlToJson();
        xmlToJson.setSrcMap(filedMap);
        String str = "false";
        if (!"".equals(simSysInsMessage.getModelFileContent()) && null != simSysInsMessage.getModelFileContent()) {
            str = xmlToJson.getTreeCamera(simSysInsMessage.getModelFileContent());
        }
        return str;
    }


    @RequestMapping(value = "/getJhSendFields", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> getJhSendFields(Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> ret = new ArrayList<>();
        List<SimSysInsMessageField> list = simSysInsMessageFieldService.getJhSortAllFields(id);
        Map<String,String> mapEqual = new HashMap<>();
        if (list != null && list.size() > 0) {
            for (SimSysInsMessageField field : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", field.getId());
                map.put("msgFieldId", field.getMsgField().getId());
                map.put("fieldId", field.getMsgField().getFieldId());
                map.put("nameZh", field.getMsgField().getNameZh());
                map.put("moFlag", field.getMoFlag());
                String valueType = field.getRespValueType();
                String valueValue = field.getRespValue();
                if (StringUtils.isEmpty(valueType) || StringUtils.isEmpty(valueValue)) {
                    map.put("defaultValue", field.getDefaultValue());
                } else {
                    if (valueType.equals(Constants.MESSAGE_RESPONSE_FIELD_TYPE_DEFAULT)) {
                        map.put("defaultValue", valueValue);
                    } else if (valueType.equals(Constants.MESSAGE_RESPONSE_FIELD_TYPE_INVOKE)) {
                        String value = ReflectWraper.invoke(valueValue, "");
                        if (StringUtils.isEmpty(value)) {
                            map.put("defaultValue", field.getDefaultValue());
                        } else {
                            map.put("defaultValue", value);
                        }
                    }else if (valueType.equals(Constants.MESSAGE_RESPONSE_FIELD_TYPE_EQUALVALUE)){
                        String fid = map.get("fieldId").toString();
                        mapEqual.put(fid,valueValue);
                        map.put("defaultValue", field.getDefaultValue());
                    } else {
                        map.put("defaultValue", field.getDefaultValue());
                    }
                }
                ret.add(map);
            }
        }
        if (mapEqual.size() > 0){
            for (String key:mapEqual.keySet()){
                int index = -1;
                String srcValue = "";
                String srcKey = mapEqual.get(key);
                for (int i = 0; i < ret.size(); i++ ){
                    if (ret.get(i).get("fieldId").equals(srcKey)){
                        srcValue = ret.get(i).get("defaultValue").toString();
                    }
                    if (ret.get(i).get("fieldId").equals(key)){
                        index = i;
                    }
                }
                if (index != -1 && StringUtils.isNotEmpty(srcValue)){
                    ret.get(index).put("defaultValue",srcValue);
                }
            }
        }
        return ret;
    }
    @RequestMapping(value = "/getSendFields", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> getSendFields(Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> ret = new ArrayList<>();
        SimSysInsMessage simSysInsMessage = simSysInsMessageService.get(id);
        List<SimSysInsMessageField> list = simSysInsMessageFieldService.getSortAllFields(id);
        Map<String,String> keyElements = new HashMap<>();
        Map<String,Object> mapKeyFields = new HashMap<>();
        Map<String,String> mapEqual = new HashMap<>();
        if (list != null && list.size() > 0) {
            for (SimSysInsMessageField field : list) {
                if (simSysInsMessage.getType().equals("SELF") || (simSysInsMessage.getType().equals("XML") && field.getFieldType().equals("HEAD"))) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", field.getId());
                    map.put("msgFieldId", field.getMsgField().getId());
                    map.put("fieldId", field.getMsgField().getFieldId());
                    map.put("nameZh", field.getMsgField().getNameZh());
                    map.put("moFlag", field.getMoFlag());
                    String valueType = field.getRespValueType();
                    String valueValue = field.getRespValue();
                    if (StringUtils.isEmpty(valueType) || StringUtils.isEmpty(valueValue)){
                        map.put("defaultValue", field.getDefaultValue());
                    }else {
                        if (valueType.equals(Constants.MESSAGE_RESPONSE_FIELD_TYPE_DEFAULT)) {
                            map.put("defaultValue", valueValue);
                        }else if (valueType.equals(Constants.MESSAGE_RESPONSE_FIELD_TYPE_INVOKE)){
                            String fid = map.get("fieldId").toString();
                            if (fid.equals(Constants.MSG_FIELD_1013)){
                                map.put("defaultValue", field.getDefaultValue());
                                ret.add(map);
                                continue;
                            }
                            if (valueValue.equalsIgnoreCase("InvokedClass:keyElements")){
                                mapKeyFields.put(fid,"");
                            }else {
                                String value = ReflectWraper.invoke(valueValue, "");
                                if (StringUtils.isEmpty(value)){
                                    map.put("defaultValue", field.getDefaultValue());
                                }else {
                                    map.put("defaultValue", value);
                                }
                            }
                        }else if (valueType.equals(Constants.MESSAGE_RESPONSE_FIELD_TYPE_EQUALVALUE)){
                            String fid = map.get("fieldId").toString();
                            mapEqual.put(fid,valueValue);
                            map.put("defaultValue", field.getDefaultValue());
                        }else {
                            map.put("defaultValue", field.getDefaultValue());
                        }
                    }
                    ret.add(map);
                    if (map.get("fieldId").equals(Constants.MESSAGE_KEY_ELEMENTS_1001)){
                        keyElements.put(map.get("fieldId").toString(),(map.get("defaultValue")==null||StringUtils.isEmpty(map.get("defaultValue").toString()))?"  ":map.get("defaultValue").toString());
                    }else if (map.get("fieldId").equals(Constants.MESSAGE_KEY_ELEMENTS_2002)){
                        keyElements.put(map.get("fieldId").toString(),(map.get("defaultValue")==null||StringUtils.isEmpty(map.get("defaultValue").toString()))?"            ":map.get("defaultValue").toString());
                    }else if (map.get("fieldId").equals(Constants.MESSAGE_KEY_ELEMENTS_2006)){
                        InvokedClass ic = new InvokedClass();
                        keyElements.put(map.get("fieldId").toString(),(map.get("defaultValue")==null||StringUtils.isEmpty(map.get("defaultValue").toString()))?ic.date(""):map.get("defaultValue").toString());
                    }else if (map.get("fieldId").equals(Constants.MESSAGE_KEY_ELEMENTS_2039)){
                        keyElements.put(map.get("fieldId").toString(),(map.get("defaultValue")==null||StringUtils.isEmpty(map.get("defaultValue").toString()))?"            ":map.get("defaultValue").toString());
                    }
                } else {
                    return null;
                }
            }
        }
        //1013
        if (keyElements.size() == 4){
            String value1013 = keyElements.get(Constants.MESSAGE_KEY_ELEMENTS_2006)+
                    keyElements.get(Constants.MESSAGE_KEY_ELEMENTS_2002)+
                    keyElements.get(Constants.MESSAGE_KEY_ELEMENTS_1001)+
                    keyElements.get(Constants.MESSAGE_KEY_ELEMENTS_2039);
            for (int i = 0; i < ret.size(); i++){
                if (ret.get(i).get("fieldId").equals(Constants.MSG_FIELD_1013)){
                    ret.get(i).put("defaultValue",value1013);
                }
                if (mapKeyFields.containsKey(ret.get(i).get("fieldId").toString())){
                    ret.get(i).put("defaultValue",value1013);
                }
            }
        }
        if (mapEqual.size() > 0){
            for (String key:mapEqual.keySet()){
                int index = -1;
                String srcValue = "";
                String srcKey = mapEqual.get(key);
                for (int i = 0; i < ret.size(); i++ ){
                    if (ret.get(i).get("fieldId").equals(srcKey)){
                        srcValue = ret.get(i).get("defaultValue").toString();
                    }
                    if (ret.get(i).get("fieldId").equals(key)){
                        index = i;
                    }
                }
                if (index != -1 && StringUtils.isNotEmpty(srcValue)){
                    ret.get(index).put("defaultValue",srcValue);
                }
            }
        }
        return ret;
    }

    @RequestMapping(value = "/field/editTaskField", method = RequestMethod.GET)
    public String editTaskField(String id, Model model) {
        model.addAttribute("simMessageid", id);
        return "test/simulatorexec/task-exec-field-edit";

    }

    /**
     * 增加业务取值
     *
     * @param id
     * @param model
     */
    @RequestMapping(value = "/addDataTypeEnum", method = RequestMethod.GET)
    public String addDataTypeEnum(String id, Model model) {
        model.addAttribute("dataTypeId", id);
        return "simulator/instancemessage/instanceself/datatype-enum-add";
    }

    /**
     * 域值详细信息列表
     *
     * @param id
     * @return
     * @author wzx
     */
    @RequestMapping(value = "/getFieldValues")
    @ResponseBody
    public List<SimSysinsMsgFieldValue> getFieldValues(@RequestParam("id") Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        return simSysinsMsgFieldValueDao.findByFid(id);
    }

    /**
     * wzx 添加业务取值
     *
     * @param simSysinsMsgFieldValue
     * @param dataTypeId
     * @return
     */
    @RequestMapping(value = {"/addDataTypeEnum/{dataTypeId}"}, method = RequestMethod.POST)
    @ResponseBody
    public Result addDataTypeEnum(SimSysinsMsgFieldValue simSysinsMsgFieldValue, @PathVariable Long dataTypeId) {
        Result ret = new Result();
        try {
            SimSysInsMessageField simSysInsMessageField = simSysInsMessageFieldService.findOne(dataTypeId);
            simSysinsMsgFieldValue.setSimMessageField(simSysInsMessageField);
            simSysinsMsgFieldValueDao.save(simSysinsMsgFieldValue);
            ret.setSuccess(true);
            logger.info("@C|true|增加业务取值成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|增加业务取值失败！");
        }

        return ret;
    }

    @RequestMapping(value = {"/deleteValue"}, method = RequestMethod.POST)
    @ResponseBody
    public Result deleteValue(@RequestParam("id") Long id) {
        Result ret = new Result();
        try {
            simSysinsMsgFieldValueDao.delete(id);
            ret.setSuccess(true);
            logger.info("@C|true|删除业务取值成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.info("@C|true|删除业务取值成功！");
        }

        return ret;
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.POST)
    @ResponseBody
    public Result uploads(Long id, @RequestParam(required = false) CommonsMultipartFile file1, @RequestParam(required = false) CommonsMultipartFile file2) {
        Result result = null;
        InputStream schemain = null;
        StringBuffer schemabuffer = new StringBuffer();
        ArrayList<Leaf> elemList = new ArrayList<Leaf>();
        String modelString = null;
        if (!file1.isEmpty()) {
            try {
                Document doc = null;
                SAXReader reader1 = new SAXReader();
                doc = reader1.read(file1.getInputStream());
                doc = XMLConverter.asDocumentByAssemble(doc);
                Element root = doc.getRootElement();
                simMessageController.analyzeXml(root,new HashMap<String, Boolean>(), id);
                elemList = simMessageController.getElementList(root, elemList);
                modelString = doc.asXML();
                schemain = file2.getInputStream();
                String line2;
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(schemain));
                line2 = reader2.readLine();
                while (line2 != null) {
                    schemabuffer.append(line2);
                    schemabuffer.append("\n");
                    line2 = reader2.readLine();
                }
                reader2.close();
                schemain.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result = simSysInsMessageService.saveFile(id, elemList, modelString, schemabuffer.toString());

        return result;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Result upload(Long id, @RequestParam(required = false) CommonsMultipartFile file1) {
        Result result = null;
        ArrayList<Leaf> elemList = new ArrayList<Leaf>();
        String modelString = null;
        if (!file1.isEmpty()) {
            try {
                Document doc = null;
                SAXReader reader1 = new SAXReader();
                doc = reader1.read(file1.getInputStream());
                doc = XMLConverter.asDocumentByAssemble(doc);
                Element root = doc.getRootElement();
                simMessageController.analyzeXml(root,new HashMap<String, Boolean>(), id);
                elemList = simMessageController.getElementList(root, elemList);
                modelString = doc.asXML();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SimSysInsMessage simMessage = simSysInsMessageDao.findOne(id);
        simSysInsMessageDao.save(simMessage);
        result = simSysInsMessageService.saveFile(id, elemList, modelString);

        return result;
    }

    @RequestMapping(value = "/updateXml", method = RequestMethod.GET)
    public String updateXml(Model model) {
        return "simulator/instancemessage/instancexml/instance-xml-edit";

    }

    @RequestMapping(value = "/updateXmlMessage/{simid}", method = RequestMethod.POST)
    @ResponseBody
    public Result updateXmlMessage(@PathVariable Long simid, SimSysInsMessage simMessage) {
        Result result = new Result();
        result = simSysInsMessageService.updateXmlMessage(simid, simMessage);

        return result;
    }

    /*
     * 查看模板文件
     */
    @RequestMapping(value = "/file/{id}/{type}")
    public String get(Model model, @PathVariable Long id, @PathVariable String type) {
        SimSysInsMessage simMessage = simSysInsMessageService.get(id);
        if("xml".equals(type) && !org.springframework.util.StringUtils.isEmpty(simMessage.getModelFileContent())){
            Document document = XmlDocHelper.getXmlFromStr(simMessage.getModelFileContent());
            simMessage.setModelFileContent(format(document));
        } else if("schema".equals(type) && !org.springframework.util.StringUtils.isEmpty(simMessage.getSchemaFileContent())){
            Document document = XmlDocHelper.getXmlFromStr(simMessage.getSchemaFileContent());
            simMessage.setSchemaFileContent(format(document));
        }
        model.addAttribute("sm", simMessage);
        model.addAttribute("type", type);
        return "simulator/instancemessage/instancexml/file-content";
    }

    /*
     * 下载模板文件
     */
    @RequestMapping(value = "/uploadXml/{id}/{type}")
    public void upload(@PathVariable Long id, @PathVariable String type, HttpServletResponse response) {
        SimSysInsMessage simMessage = simSysInsMessageService.get(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date());
        String filename = simMessage.getName() + "-" + date;
        String msg = "";
        if("xml".equals(type) && !org.springframework.util.StringUtils.isEmpty(simMessage.getModelFileContent())){
            Document document = XmlDocHelper.getXmlFromStr(simMessage.getModelFileContent());
            msg = format(document);
            filename = simMessage.getName() + "-" + date +".xml";
        } else if("schema".equals(type) && !org.springframework.util.StringUtils.isEmpty(simMessage.getSchemaFileContent())){
            Document document = XmlDocHelper.getXmlFromStr(simMessage.getSchemaFileContent());
            msg = format(document);
            filename = simMessage.getName() + "-schema-" + date +".xml";
        }
        try{
            InputStream in = new BufferedInputStream(new ByteArrayInputStream(msg.getBytes("utf-8")));
            if (in == null)
                return;
            response.reset();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GB2312"), "iso8859-1"));
            // 输出资源内容到相应对象
            byte[] b = new byte[1024];
            int len;
            OutputStream outs = response.getOutputStream();
            while ((len = in.read(b, 0, 1024)) != -1) {
                outs.write(b, 0, len);
            }
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(outs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * XML格式化输出字符串
     * @param document
     * @return
     */
    private String format(Document document) {
        // 格式化输出格式
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");  //字符编码
        format.setIndentSize(4);  //缩进值
        format.setExpandEmptyElements(true);//当节点值为空时正常输出
        StringWriter writer = new StringWriter();
        // 格式化输出流
        XMLWriter xmlWriter = new XMLWriter(writer, format);
        try {
            // 将document写入到输出流
            xmlWriter.write(document);
            xmlWriter.flush();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
                xmlWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequestMapping(value = "/cloneDataToSystem", method = RequestMethod.POST)
    @ResponseBody
    public Result cloneDataToSystem(Long id){
        Result ret = new Result();
        try {
            ret = simSysInsMessageService.clonetBaseData(id);
            logger.info("@C|true|同步数据成功！");
        } catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("同步数据失败!");
            logger.info("@C|true|同步数据失败！");
        }
        return ret;
    }
}
