package com.zdtech.platform.web.controller.message;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.MsgFieldCodeDao;
import com.zdtech.platform.framework.repository.MsgFieldDao;
import com.zdtech.platform.service.message.MsgDataTypeService;
import com.zdtech.platform.service.message.MsgFieldCodeService;
import com.zdtech.platform.service.message.MsgFieldService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by leepan on 2016/5/4.
 */
@Controller
@RequestMapping("/msg/field")
public class MsgFieldController {
    private static Logger logger = LoggerFactory.getLogger(MsgFieldController.class);
    @Autowired
    private MsgDataTypeService msgDataTypeService;
    @Autowired
    private MsgFieldService msgFieldService;
    @Autowired
    private MsgFieldCodeDao msgFieldCodeDao;
    @Autowired
    private MsgFieldCodeService msgFieldCodeService;
    @Autowired
    private MsgFieldDao msgFieldDao;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String fieldList() {
        return "message/field/field-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "message/field/field-add";
    }

    @RequestMapping(value = "/editField/{id}", method = RequestMethod.GET)
    public String editField(@PathVariable("id") Long id, Model model) {
        MsgField msgField = msgFieldService.get(id);
        model.addAttribute("msgField", msgField);
        return "message/field/field-type-edit";
    }

    @RequestMapping(value = "/editField", method = RequestMethod.POST)
    @ResponseBody
    public void editField(MsgField msgField, String msgFieldId) {
        MsgField msgField1 = msgFieldService.get(Long.parseLong(msgFieldId));
        msgField1.setFieldValueType(msgField.getFieldValueType());
        msgField1.setFieldValueTypeParam(msgField.getFieldValueTypeParam());
        msgFieldDao.save(msgField1);
        msgFieldCodeDao.deleteByMsgFieldId(Long.parseLong(msgFieldId));
    }

    @RequestMapping(value = "/enum/{id}", method = RequestMethod.POST)
    @ResponseBody
    public List<MsgFieldCode> getCodes(@PathVariable("id") Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        return msgFieldService.get(id).getMsgFieldCodes();
    }

    @RequestMapping(value = "/addCode/", method = RequestMethod.GET)
    public String addCode(String codeId, String msgFieldId, String type, Model model) {
        model.addAttribute("type", type);
        model.addAttribute("msgFieldId", msgFieldId);
        model.addAttribute("codeId", codeId);
        return "message/field/field-code-add";
    }

    @RequestMapping(value = "/addCode", method = RequestMethod.POST)
    @ResponseBody
    public void addCode(MsgFieldCode msgFieldCode, String msgFieldId) {
        MsgField msgField = msgFieldService.get(Long.parseLong(msgFieldId));
        msgField.setFieldValueType("enum");
        msgField.setFieldValueTypeParam(null);
        msgFieldDao.save(msgField);
        msgFieldCode.setMsgField(msgField);
        msgFieldCodeDao.save(msgFieldCode);
    }

    @RequestMapping(value = "/getCurrentParam/", method = RequestMethod.GET)
    @ResponseBody
    public MsgFieldCode getCurrentParam(String codeId) {
        MsgFieldCode msgFieldCode = msgFieldCodeDao.findOne(Long.parseLong(codeId));
        return msgFieldCode;
    }

    @RequestMapping(value = "/delCodes", method = RequestMethod.POST)
    @ResponseBody
    public Result delCodes(@RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        try {
            msgFieldCodeService.delCodes(ids);
            logger.info("@P|true|删除项目成功");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@P|false|删除项目失败");
        }
        return ret;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result addField(MsgField field, String dataTypeId) {
        Result ret = new Result();
        try {
            MsgDataType dataType = null;
            if (!StringUtils.isEmpty(dataTypeId)) {
                dataType = msgDataTypeService.get(Long.parseLong(dataTypeId));
            }
            field.setDataType(dataType);
            ret = msgFieldService.addField(field);
            logger.info("@M|true|添加或修改业务要素集成功！");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|添加或修改业务要素集失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/get/{id}")
    @ResponseBody
    public MsgField getField(@PathVariable("id") Long id) {
        return msgFieldService.get(id);
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delField(@RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        try {
            List<Long> idList = new ArrayList<>();
            for (Long id : ids) {
                idList.add(id);
            }
            msgFieldService.deleteFields(idList);
            logger.info("@M|true|删除业务要素集成功！");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|删除业务要素集失败！");
        }
        return ret;
    }

    @RequestMapping("/update")
    @ResponseBody
    public Result update(@RequestBody List<Map<String, Object>> mapList){
        Result ret = new Result();
        try {
            for(Map<String, Object> map : mapList){
                Long id = Long.parseLong(map.get("id").toString());
                MsgField field = msgFieldDao.findOne(id);
                String nameZh = map.get("nameZh").toString();
                field.setNameZh(nameZh);
                msgFieldService.addField(field);
            }
        } catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("修改失败!");
        }
        return ret;
    }
}
