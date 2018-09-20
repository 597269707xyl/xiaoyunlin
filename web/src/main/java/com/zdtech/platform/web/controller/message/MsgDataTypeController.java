package com.zdtech.platform.web.controller.message;

import com.zdtech.platform.framework.entity.MsgDataType;
import com.zdtech.platform.framework.entity.MsgDataTypeEnum;
import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.service.message.MsgDataTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by leepan on 2016/5/4.
 */
@Controller
@RequestMapping("/msg/datatype")
public class MsgDataTypeController {
    private static Logger logger = LoggerFactory.getLogger(MsgDataTypeController.class);
    @Autowired
    private MsgDataTypeService msgDataTypeService;

    @RequestMapping(value = "/delCharset", method = RequestMethod.POST)
    @ResponseBody
    public Result delMsgCharset(@RequestParam("id") Long id){
        Result ret = new Result();
        try {
            msgDataTypeService.delCharset(id);
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
        }
        return ret;
    }


    @RequestMapping(value = "/getAll")
    @ResponseBody
    public Map<String,Object> getAll(){
        Map<String,Object> ret = null;
        try {
            ret = msgDataTypeService.getAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }

    @RequestMapping(value = "/datatypelist")
    public String datatypeList(){
        return "message/datatype/datatype/datatype-list";
    }

    @RequestMapping(value = "/add")
    public String addMsgDataType(){return "message/datatype/datatype/datatype-add";}

    @RequestMapping(value = {"/add"}, method = RequestMethod.POST)
    @ResponseBody
    public Result addMsgDataType(MsgDataType msgDataType,Long charsetId){
        Result ret = new Result();
        try{
            msgDataTypeService.addDataType(msgDataType,charsetId);
            ret.setSuccess(true);
            logger.info("@M|true|添加或修改数据类型成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|添加或修改数据类型失败！");
        }

        return ret;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delDataType(@RequestParam("ids[]")Long[] ids) {
        Result ret = new Result();
        try {
            List<Long> list = Arrays.asList(ids);
            msgDataTypeService.deleteDataTypes(list);
            ret.setSuccess(true);
            logger.info("@M|true|删除数据类型成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|删除数据类型失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/get/{id}")
    @ResponseBody
    public MsgDataType get(@PathVariable Long id){
        return msgDataTypeService.get(id);
    }

    @RequestMapping(value = "/addDataTypeEnum", method = RequestMethod.GET)
    public String addDataTypeEnum(String id,Model model) {
        model.addAttribute("dataTypeId",id);
        return "message/datatype/datatype/datatype-enum-add";
    }

    @RequestMapping(value = {"/addDataTypeEnum/{dataTypeId}"}, method = RequestMethod.POST)
    @ResponseBody
    public Result addDataTypeEnum(MsgDataTypeEnum msgDataTypeEnum,@PathVariable Long dataTypeId){
        Result ret = new Result();
        try{
            msgDataTypeService.addDataTypeEnum(msgDataTypeEnum,dataTypeId);
            ret.setSuccess(true);
            logger.info("@M|true|添加相应数据类型取值成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|添加相应数据类型取值失败！");
        }

        return ret;
    }

    @RequestMapping(value = "/delDataTypeEnum", method = RequestMethod.POST)
    @ResponseBody
    public Result delDataTypeEnum(Long dataTypeId,@RequestParam("ids[]") Long[] ids){
        Result ret = new Result();
        try {
            List<Long> list = Arrays.asList(ids);
            msgDataTypeService.delDataTypeEnum(dataTypeId,list);
            logger.info("@M|true|删除相应数据类型取值成功！");
        }catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|删除相应数据类型取值失败！");
        }
        return ret;
    }
    @RequestMapping(value = "/getDataEnum", method = RequestMethod.POST)
    @ResponseBody
    public List<MsgDataTypeEnum> getDataEnum(@RequestParam(required=false)Long id){
        if (id == null){
            return new ArrayList<>();
        }
        return msgDataTypeService.get(id).getDatas();
    }
    @RequestMapping(value = "/getEnum/{id}", method = RequestMethod.GET)
    @ResponseBody
    public MsgDataTypeEnum getEnum(@PathVariable Long id){
        if (id == null){
            return null;
        }
        return msgDataTypeService.getDataEnum(id);

    }

    @RequestMapping(value = "/updateDataTypeEnum")
    public String updateDataTypeEnum() {
        return "message/datatype/datatype/datatype-enum-edit";
    }


    @RequestMapping(value = {"/updateDataTypeEnum/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    public Result updateDataTypeEnum(MsgDataTypeEnum msgDataTypeEnum,@PathVariable Long id){
        Result ret = new Result();
        try{
            msgDataTypeService.updateDataTypeEnum(msgDataTypeEnum,id);
            ret.setSuccess(true);
            logger.info("@M|true|修改相应数据类型取值成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|修改相应数据类型取值失败！");
        }

        return ret;
    }

}
