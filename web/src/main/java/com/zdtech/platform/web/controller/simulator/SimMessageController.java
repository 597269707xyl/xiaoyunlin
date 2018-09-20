package com.zdtech.platform.web.controller.simulator;


import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.utils.XMLConverter;
import com.zdtech.platform.framework.utils.XmlDocHelper;
import com.zdtech.platform.service.message.MsgFieldSetService;
import com.zdtech.platform.service.simulator.*;
import org.apache.commons.io.IOUtils;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * SimMessageController
 *
 * @author xiaolanli
 * @date 2016/5/11
 */
@Controller
@RequestMapping("/sim/message")
public class SimMessageController {
    private static Logger logger = LoggerFactory.getLogger(SimMessageController.class);
    @Autowired
    private SimMessageService simMessageService;
    @Autowired
    private SimSystemService simSystemService;
    @Autowired
    private MsgFieldSetService msgFieldSetService;
    @Autowired
    private SimMessageFieldService simMessageFieldService;
    @Autowired
    private SimReplyRuleService simReplyRuleService;
    @Autowired
    private SimMsgFieldValueDao simMsgFieldValueDao;
    @Autowired
    private SimMessageDao simMessageDao;
    @Autowired
    private MsgFieldDao msgFieldDao;
    @Autowired
    private SimMessageFieldDao simMessageFieldDao;
    @Autowired
    private SimMsgFieldCodeDao simMessageFieldCodeDao;
    @Autowired
    private SimMessageFieldCodeService simMessageFieldCodeService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String selflist(String type, Model model) {
        model.addAttribute("type", type);
        if (type.equals("SELF")) {
            return "simulator/message/self/self-list";
        } else {
            return "simulator/message/xml/xml-list";
        }
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> query(@RequestParam Map<String, Object> params, @PageableDefault Pageable page) {
        Map<String, Object> ret = new HashMap<>();
        ret = simMessageService.findMessages(params, page);
        return ret;
    }

    @RequestMapping(value = "/addself", method = RequestMethod.GET)
    public String add(String type, Model model) {
        model.addAttribute("type", type);
        if (type.equals("SELF")) {
            return "simulator/message/self/self-add";
        } else {
            return "simulator/message/xml/xml-add";
        }
    }

    @RequestMapping(value = "/editField/{id}", method = RequestMethod.GET)
    public String editField(@PathVariable("id") Long id, Model model) {
        SimMessageField simMessageField = simMessageFieldDao.getOne(id);
        model.addAttribute("simMessageField", simMessageField);
        return "simulator/message/self/self-field-type-edit";
    }

    @RequestMapping(value = "/editField", method = RequestMethod.POST)
    @ResponseBody
    public void editField(SimMessageField simMessageField, String simMessageFieldId) {
        SimMessageField simMessageFieldInitial = simMessageFieldService.findOne(Long.parseLong(simMessageFieldId));
        simMessageFieldInitial.setFieldValueType(simMessageField.getFieldValueType());
        simMessageFieldInitial.setFieldValueTypeParam(simMessageField.getFieldValueTypeParam());
        simMessageFieldDao.save(simMessageFieldInitial);
        simMessageFieldCodeDao.deleteBySimMessageFieldId(Long.parseLong(simMessageFieldId));
    }

    @RequestMapping(value = "/enum/{id}", method = RequestMethod.POST)
    @ResponseBody
    public List<SimMsgFieldCode> getCodes(@PathVariable("id") Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        return simMessageFieldDao.findOne(id).getMsgFieldCodes();
    }

    @RequestMapping(value = "/addCode/", method = RequestMethod.GET)
    public String addCode(String codeId, String simMessageFieldId, String type, Model model) {
        model.addAttribute("type", type);
        model.addAttribute("simMessageFieldId", simMessageFieldId);
        model.addAttribute("codeId", codeId);
        return "simulator/message/self/self-field-code-add";
    }

    @RequestMapping(value = "/addCode", method = RequestMethod.POST)
    @ResponseBody
    public void addCode(SimMsgFieldCode simMsgFieldCode, String simMessageFieldId) {
        SimMessageField simMessageField = simMessageFieldDao.findOne(Long.parseLong(simMessageFieldId));
        simMessageField.setFieldValueType("enum");
        simMessageField.setFieldValueTypeParam(null);
        simMessageFieldDao.save(simMessageField);
        simMsgFieldCode.setMsgField(simMessageField);
        simMessageFieldCodeDao.save(simMsgFieldCode);
    }

    @RequestMapping(value = "/getCurrentParam/", method = RequestMethod.GET)
    @ResponseBody
    public SimMsgFieldCode getCurrentParam(String codeId) {
        SimMsgFieldCode simMsgFieldCode = simMessageFieldCodeDao.findOne(Long.parseLong(codeId));
        return simMsgFieldCode;
    }

    @RequestMapping(value = "/delCodes", method = RequestMethod.POST)
    @ResponseBody
    public Result delCodes(@RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        try {
            simMessageFieldCodeService.delCodes(ids);
            logger.info("@P|true|删除项目成功");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@P|false|删除项目失败");
        }
        return ret;
    }

    /*
    增加新的报文（包含报文的基本信息以及所有的报文头和通用业务要素集域）
     */
    @RequestMapping(value = "/addself/{simid}/{generalid}", method = RequestMethod.POST)
    @ResponseBody
    public Result addself(@PathVariable Long simid, @PathVariable Long generalid, SimMessage simMessage) {
        Result ret = new Result();
        try {

            ret = simMessageService.addSimMessage(simid, generalid, simMessage);
            logger.info("@C|true|添加报文成功！");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|添加报文失败！");
        }
        return ret;
    }

    /*
     查询所有的仿真系统名称
     */
    @RequestMapping(value = "/getAllsim")
    @ResponseBody
    public List<Map<String, String>> getAllsim(String type) {
        List<Map<String, String>> list = new ArrayList<>();
        List<SimSystem> systemList = simSystemService.getallsim(type);
        List<Map<String, String>> rnt = new ArrayList<Map<String, String>>();
        if (systemList == null || systemList.isEmpty())
            return rnt;
        Map<String, String> map = null;
        for (SimSystem simSystem : systemList) {
            map = new HashMap<String, String>();
            map.put("id", String.valueOf(simSystem.getId()));
            map.put("text", simSystem.getName());
            rnt.add(map);
        }
        return rnt;
    }

    /*
    查询所有的通用业务要素集
     */
    @RequestMapping(value = "/getAllgeneralset")
    @ResponseBody
    public List<Map<String, String>> getAllgeneralset(String type) {
        List<Map<String, String>> list = new ArrayList<>();
        List<MsgFieldSet> msgFieldSets = msgFieldSetService.getallgeneralset(type);
        List<Map<String, String>> rnt = new ArrayList<Map<String, String>>();
        if (msgFieldSets == null || msgFieldSets.isEmpty())
            return rnt;
        Map<String, String> map = null;
        for (MsgFieldSet simSystem : msgFieldSets) {
            map = new HashMap<String, String>();
            map.put("id", String.valueOf(simSystem.getId()));
            map.put("text", simSystem.getName());
            rnt.add(map);
        }
        return rnt;
    }

    /*
    查询要修改的报文信息
     */
    @RequestMapping(value = "/get/{id}")
    @ResponseBody
    public SimMessage get(@PathVariable Long id) {
        return simMessageService.get(id);
    }

    /*
    业务要素页面
     */
    @RequestMapping(value = "/fieldList", method = RequestMethod.GET)
    public String fieldList(String id, String type, Model model) {
        model.addAttribute("simMessageid", id);
        model.addAttribute("type", type);
        return "simulator/message/self/self-field-list";
    }

    /*
    查看每个报文的组成域
     */
    @RequestMapping(value = "/getFields", method = RequestMethod.POST)
    @ResponseBody
    public List<SimMessageField> getFields(@RequestParam(required = false) Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        return simMessageFieldService.getall(id);
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
            simMessageFieldService.addFields(simMessageid, list, null);
            ret.setSuccess(true);
            ret.setMsg("域值增加成功");
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
            simMessageFieldService.delFields(simMessageId, list);
            ret.setSuccess(true);
            ret.setMsg("删除成功");
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
            simMessageFieldService.signFields(simMessageId, list);
            ret.setSuccess(true);
            ret.setMsg("加签成功");
            logger.info("@C|true|加签业务要素成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|加签业务要素失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/field/get/{id}")
    @ResponseBody
    public SimMessageField getField(@PathVariable Long id) {
        return simMessageFieldService.findOne(id);
    }

    /*
        修改组成域的值
         */
    @RequestMapping(value = "/field/edit", method = RequestMethod.GET)
    public String editfield(String id, String type, Model model) {
        model.addAttribute("simMessageid", id);
        model.addAttribute("type", type);
        if (type.equals("SELF")) {
            return "simulator/message/self/self-field-comp-edit";
        } else {
            return "simulator/message/xml/xml-field-comp-edit";
        }
    }

    @RequestMapping(value = "/field/edit", method = RequestMethod.POST)
    @ResponseBody
    public Result editField(Long id, SimMessageField field) {
        Result ret = new Result();
        try {
            simMessageFieldService.saveFiled(id, field);
            ret.setSuccess(true);
            ret.setMsg("保存成功");
            logger.info("@C|true|修改业务要素成功！");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|修改业务要素失败！");
        }

        return ret;
    }

    @RequestMapping(value = "/getResponseMsgs", method = RequestMethod.POST)
    @ResponseBody
    public List<SimMessage> getResponseMsgs(Long requestId) {
        return simReplyRuleService.getResponseMsgsByRequestId(requestId);
    }

    @RequestMapping(value = "/getReponseRule", method = RequestMethod.GET)
    public String getReponseRule(String type, Model model) {
        model.addAttribute("type", type);
        if (type.equals("SELF")) {
            return "simulator/message/self/self-reply-list";
        } else {
            return "simulator/message/xml/xml-reply-list";
        }
    }

    @RequestMapping(value = "/listReponseMsgs", method = RequestMethod.GET)
    public String listReponseMsgs(String type, Model model) {
        model.addAttribute("type", type);
        if (type.equals("SELF")) {
            return "simulator/message/self/self-reply-select-list";
        } else {
            return "simulator/message/xml/xml-reply-select-list";
        }
    }

    @RequestMapping(value = "/getAllResponseMsgs", method = RequestMethod.POST)
    @ResponseBody
    public List<SimMessage> listResponseMsgs(Long requestId) {
        List<SimMessage> list = simMessageService.findSystemMsgs(requestId);
        return list;
    }

    @RequestMapping(value = "/addResponses", method = RequestMethod.POST)
    @ResponseBody
    public Result addResponses(Long requestId, @RequestParam("responseIds[]") Long[] ids, String param) {
        Result ret = new Result();
        try {
            simReplyRuleService.addReplyRules(requestId, ids, param);
            ret.setSuccess(true);
            ret.setMsg("操作成功");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
        }
        return ret;
    }

    @RequestMapping(value = "/deleteResponses", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteResponses(Long requestId, @RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        try {
            simReplyRuleService.deleteReplyRules(requestId, ids);
            ret.setSuccess(true);
            ret.setMsg("操作成功");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
        }
        return ret;
    }

    @RequestMapping(value = "/listReponseMsgParam", method = RequestMethod.GET)
    public String listReponseMsgParam(String type) {
        return "simulator/message/reply-rule-param";
    }

    /*
    设置请求应答规则
     */
    @RequestMapping(value = "/setReponseRule", method = RequestMethod.GET)
    public String setReponseRule(String type, Model model) {
        model.addAttribute("type", type);
        if (type.equals("SELF")) {
            return "simulator/message/self/self-replyset-list";
        } else {
            return "simulator/message/xml/xml-replyset-list";
        }
    }

    @RequestMapping(value = "/addReplyRule", method = RequestMethod.POST)
    @ResponseBody
    public Result addReplyRule(Long requestid, @RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        try {
            List<Long> list = Arrays.asList(ids);
            simReplyRuleService.saveReplyRule(requestid, list);
            ret.setSuccess(true);
            ret.setMsg("设置成功");
            logger.info("@C|true|设置应答规则成功！");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|设置应答规则失败！");
        }

        return ret;
    }

    /*
   查看所有报文
    */
    @RequestMapping(value = "/getAllmessage", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> getAllmessage(String type) {
        List<Map<String, String>> list = new ArrayList<>();
        List<SimMessage> simMessages = simMessageService.getAllmessage(type);
        List<Map<String, String>> rnt = new ArrayList<Map<String, String>>();
        if (simMessages == null || simMessages.isEmpty())
            return rnt;
        Map<String, String> map = null;
        for (SimMessage simMessage : simMessages) {
            map = new HashMap<String, String>();
            map.put("id", String.valueOf(simMessage.getId()));
            map.put("text", simMessage.getName());
            rnt.add(map);
        }
        return rnt;
    }

    @RequestMapping(value = "/getAllSchemaFile")
    @ResponseBody
    public List<Map<String, String>> getAllSchemaFile() {
        List<Map<String, String>> list = new ArrayList<>();
        List<MsgSchemaFile> msgSchemaFiles = simMessageService.getAllSchemaFile();
        List<Map<String, String>> rnt = new ArrayList<Map<String, String>>();
        if (msgSchemaFiles == null || msgSchemaFiles.isEmpty())
            return rnt;
        Map<String, String> map = null;
        for (MsgSchemaFile file : msgSchemaFiles) {
            map = new HashMap<String, String>();
            map.put("id", String.valueOf(file.getId()));
            map.put("text", file.getName());
            rnt.add(map);
        }
        return rnt;
    }

    @RequestMapping(value = "/addXmlReplyRule/{requestid}/{replymesgType}", method = RequestMethod.POST)
    @ResponseBody
    public Result addXmlReplyRule(@PathVariable Long requestid, @PathVariable String replymesgType) {
        Result ret = new Result();
        try {
            simReplyRuleService.saveXmlReplyRule(requestid, replymesgType);
            ret.setSuccess(true);
            ret.setMsg("设置成功");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
        }

        return ret;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delSimMessage(@RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        try {
            List<Long> list = Arrays.asList(ids);
            simMessageService.deleteSimMessages(list);
            ret.setSuccess(true);
            ret.setMsg("删除成功");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");

        }
        return ret;
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
        return "simulator/message/self/datatype-enum-add";
    }

    /**
     * 域值详细信息列表
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getFieldValues")
    @ResponseBody
    public List<SimMsgFieldValue> getFieldValues(@RequestParam("id") Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        return simMsgFieldValueDao.findByFid(id);
    }

    /**
     * wzx
     *
     * @param simMsgFieldValue
     * @param dataTypeId
     * @return
     */
    @RequestMapping(value = {"/addDataTypeEnum/{dataTypeId}"}, method = RequestMethod.POST)
    @ResponseBody
    public Result addDataTypeEnum(SimMsgFieldValue simMsgFieldValue, @PathVariable Long dataTypeId) {
        Result ret = new Result();
        try {
            SimMessageField simMessageField = simMessageFieldService.findOne(dataTypeId);
            simMsgFieldValue.setSimMessageField(simMessageField);
            simMsgFieldValue.setFlag(1);
            simMsgFieldValueDao.save(simMsgFieldValue);
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
            simMsgFieldValueDao.delete(id);
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

    /*
       增加新的xml报文（包含报文的基本信息以及所有的报文头和通用业务要素集域）
        */
    @RequestMapping(value = "/addXmlMessage/{simid}", method = RequestMethod.POST)
    @ResponseBody
    public String addXmlMessage(@PathVariable Long simid, SimMessage simMessage) {
        Long id = simMessageService.addXmlMessage(simid, simMessage);
        return String.valueOf(id);
    }


    @RequestMapping(value = "/updateXml", method = RequestMethod.GET)
    public String updateXml(Model model) {
        return "simulator/message/xml/xml-edit";

    }

    @RequestMapping(value = "/updateXmlMessage/{simid}", method = RequestMethod.POST)
    @ResponseBody
    public Result updateXmlMessage(@PathVariable Long simid, SimMessage simMessage) {
        Result result = new Result();
        result = simMessageService.updateXmlMessage(simid, simMessage);

        return result;
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.POST)
    @ResponseBody
    public Result uploads(Long id, @RequestParam(required = false) CommonsMultipartFile file1, @RequestParam(required = false) CommonsMultipartFile file2) {
        Result result = null;
        InputStream schemain = null;
        StringBuffer modelbuffer = new StringBuffer();
        StringBuffer schemabuffer = new StringBuffer();
        ArrayList<Leaf> elemList = new ArrayList<Leaf>();
        String modelString = null;
        if (!file1.isEmpty()) {
            try {
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

                Document doc = null;
                SAXReader reader1 = new SAXReader();
                doc = reader1.read(file1.getInputStream());
                doc = XMLConverter.asDocumentByAssemble(doc);
                Element root = doc.getRootElement();
                analyzeXml(root, new HashMap<String, Boolean>(), id);
                elemList = getElementList(root, elemList);
                modelString = doc.asXML();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SimMessage simMessage = simMessageDao.findOne(id);
        simMessage.setSchemaFileContent(schemabuffer.toString());
        simMessageDao.save(simMessage);
        result = simMessageService.saveFile(id, elemList, modelString, schemabuffer.toString());
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
                analyzeXml(root, new HashMap<String, Boolean>(), id);
                elemList = getElementList(root, elemList);
                modelString = doc.asXML();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SimMessage simMessage = simMessageDao.findOne(id);
        simMessageDao.save(simMessage);
        result = simMessageService.saveFile(id, elemList, modelString);

        return result;
    }

    /**
     * 获取节点所有属性值
     *
     * @param element
     * @return
     */
    public String getNodeAttribute(Element element) {
        String xattribute = "";
        DefaultAttribute e = null;
        List list = element.attributes();
        for (int i = 0; i < list.size(); i++) {
            e = (DefaultAttribute) list.get(i);
            xattribute += " " + e.getName() + "=" + "\"" + e.getText() + "\"";
        }
        return xattribute;
    }

    /**
     * 递归遍历方法
     *
     * @param element
     * @return
     */
    public ArrayList getElementList(Element element, ArrayList elemList) {
        List elements = element.elements();
        if (elements.size() == 0) {
            //没有子元素
            String xpath = getFormatPath(element);
            String value = element.getTextTrim();
            elemList.add(new Leaf(getNodeAttribute(element), xpath, value));
        } else {
            //有子元素
            Iterator it = elements.iterator();
            while (it.hasNext()) {
                Element elem = (Element) it.next();
                //递归遍历
                getElementList(elem, elemList);

            }
        }
        return elemList;
    }

    @RequestMapping(value = "/updateself", method = RequestMethod.GET)
    public String update(String type, Model model) {
        model.addAttribute("type", type);
        if (type.equals("SELF")) {
            return "simulator/message/self/self-update";
        } else {
            return "simulator/message/xml/xml-add";
        }
    }

    /*
    修改报文（包含报文的基本信息以及所有的报文头和通用业务要素集域）
     */
    @RequestMapping(value = "/updateSelf/{simid}", method = RequestMethod.POST)
    @ResponseBody
    public Result updateSelf(@PathVariable Long simid, SimMessage simMessage) {
        Result ret = new Result();
        try {

            ret = simMessageService.updateSimMessage(simid, simMessage);
            logger.info("@C|true|修改报文成功！");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|修改报文失败！");
        }
        return ret;
    }

    private String getFormatPath(Element element){
        String ret = "";
        if (element == null){
            return ret;
        }
        String name = element.getName();
        ret = "/"+name;
        while (element.getParent() != null){
            element = element.getParent();
            name = element.getName();
            ret = "/" + name + ret;
        }
        return ret;
    }
    public void analyzeXml(Element element, Map<String, Boolean> map, Long simMessageId) {
        List elements = element.elements();
        if (elements.size() == 0) {
            String path = getFormatPath(element);
            if (!map.containsKey(path)) {
                String name = element.getName();
                String value = element.getTextTrim();
                if (value.length() > 255) {
                    value = null;
                }
                MsgField msgField1 = msgFieldDao.findByFieldId(path);
                if (msgField1 == null) {
                    MsgField msgField = new MsgField();
                    msgField.setFieldId(path);
                    msgField.setNameEn(name);
                    msgField.setDefaultValue(value);
                    msgField.setMsgType("XML");
                    msgField.setFieldType("BODY");
                    msgField.setMoFlag(true);
                    msgField.setFixFlag(false);
                    msgFieldDao.save(msgField);
                    map.put(path, true);
                }
            }

        } else {
            Iterator iterator = elements.iterator();
            while (iterator.hasNext()) {
                Element elem = (Element) iterator.next();
                analyzeXml(elem, map, simMessageId);
            }
        }
    }

    /*
     * 查看模板文件
     */
    @RequestMapping(value = "/file/{id}/{type}")
    public String get(Model model, @PathVariable Long id, @PathVariable String type) {
        SimMessage simMessage = simMessageService.get(id);
        if("xml".equals(type) && !StringUtils.isEmpty(simMessage.getModelFileContent())){
            Document document = XmlDocHelper.getXmlFromStr(simMessage.getModelFileContent());
            simMessage.setModelFileContent(format(document));
        } else if("schema".equals(type) && !StringUtils.isEmpty(simMessage.getSchemaFileContent())){
            Document document = XmlDocHelper.getXmlFromStr(simMessage.getSchemaFileContent());
            simMessage.setSchemaFileContent(format(document));
        }
        model.addAttribute("sm", simMessage);
        model.addAttribute("type", type);
        return "simulator/message/xml/file-content";
    }

    /*
     * 下载模板文件
     */
    @RequestMapping(value = "/uploadXml/{id}/{type}")
    public void upload(@PathVariable Long id, @PathVariable String type, HttpServletResponse response) {
        SimMessage simMessage = simMessageService.get(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date());
        String filename = simMessage.getName() + "-" + date;
        String msg = "";
        if("xml".equals(type) && !StringUtils.isEmpty(simMessage.getModelFileContent())){
            Document document = XmlDocHelper.getXmlFromStr(simMessage.getModelFileContent());
            msg = format(document);
            filename = simMessage.getName() + "-" + date +".xml";
        } else if("schema".equals(type) && !StringUtils.isEmpty(simMessage.getSchemaFileContent())){
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

    @RequestMapping(value = "/cloneToInstance", method = RequestMethod.GET)
    public String cloneToInstance(){
        return "simulator/message/xml/xml-clone-to-instance";
    }

    @RequestMapping(value = "/cloneDataToInstance", method = RequestMethod.POST)
    @ResponseBody
    public Result cloneDataToInstance(@RequestParam("simId")Long simId, @RequestParam("type")String type, @RequestParam("instanceIds[]") Long[] instanceIds){
        Result ret = new Result();
        try {
            if("cloneBaseData".equals(type)){ //同步基础数据
                ret = simMessageService.clonetBaseData(simId, instanceIds);
            }
            if("cloneBaseMsg".equals(type)){  //同步报文模板
                ret = simMessageService.clonetBaseMsg(simId, instanceIds);
            }
            logger.info("@C|true|同步数据成功！");
        } catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("同步数据失败!");
            logger.info("@C|true|同步数据失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/updateBaseMsg", method = RequestMethod.POST)
    @ResponseBody
    public Result updateBaseMsg(Long id, String msg){
        Result ret = new Result();
        try {
            int index = msg.indexOf("<");
            if(index == -1){
                ret.setSuccess(false);
                ret.setMsg("报文模板格式不正确!");
                logger.info("@C|true|报文模板格式不正确！");
                return ret;
            }
            msg = msg.substring(index);
            ArrayList<Leaf> elemList = new ArrayList<Leaf>();
            Document doc = XmlDocHelper.getXmlFromStr(msg);
            doc = XMLConverter.asDocumentByAssemble(doc);
            Element root = doc.getRootElement();
            analyzeXml(root, new HashMap<String, Boolean>(), id);
            elemList = getElementList(root, elemList);
            String modelString = doc.asXML();
            List<SimMessageField> list = simMessageFieldDao.findByid(id);
            List<String> xpathList = getXpaths(elemList);
            for(SimMessageField field : list){
                if(xpathList.size()>0 && !xpathList.contains(field.getMsgField().getFieldId())){
                    simMessageFieldDao.delete(field);
                }
            }
            ret = simMessageService.saveFile(id, elemList, modelString);
            logger.info("@C|true|修改报文模板成功！");
        } catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("修改报文模板失败!");
            logger.info("@C|true|修改报文模板失败！");
        }
        return ret;
    }

    private List<String> getXpaths(ArrayList<Leaf> elemList){
        List<String> list = new ArrayList<>();
        for(Leaf leaf : elemList){
            list.add(leaf.getXpath());
        }
        return list;
    }
}
