package com.zdtech.platform.web.controller.message;

import com.zdtech.platform.framework.entity.MsgField;
import com.zdtech.platform.framework.entity.MsgFieldSet;
import com.zdtech.platform.framework.entity.MsgFieldSetComp;
import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.framework.repository.MsgFieldSetCompDao;
import com.zdtech.platform.service.message.MsgFieldSetService;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by leepan on 2016/5/5.
 */
@Controller
@RequestMapping("/msg/fieldset")
public class MsgFieldSetController {
    private static Logger logger = LoggerFactory.getLogger(MsgFieldSetController.class);
    @Autowired
    private MsgFieldSetService msgFieldSetService;
    @Autowired
    private MsgFieldSetCompDao msgFieldSetCompDao;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String fieldSetList(String type,Model model) {
        model.addAttribute("setType",type);
        return "message/fieldset/field-set-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(String type,Model model) {
        model.addAttribute("setType",type);
        model.addAttribute("label",type.equalsIgnoreCase("head")?"报文头":"通用业务要素集");
        return "message/fieldset/field-set-add";
    }
    @RequestMapping(value = "/getAllBySetType")
    @ResponseBody
    public Map<String,Object> getAllBySetType(String setType){
        Map<String,Object> ret = null;
        try {
            ret = msgFieldSetService.getAllBySetType(setType);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }
    @RequestMapping(value = "/fieldList", method = RequestMethod.GET)
    public String fieldList(String id,String setType,String msgType,Model model) {
        model.addAttribute("fieldSetId",id);
        model.addAttribute("fieldType",setType);
        model.addAttribute("msgType",msgType);
        return "message/fieldset/field-list";
    }

    @RequestMapping(value = "/field/edit", method = RequestMethod.GET)
    public String fieldSetCompEdit(){
        return "message/fieldset/field-set-comp-edit";
    }
    @RequestMapping(value = "/field/get/{id}")
    @ResponseBody
    public MsgFieldSetComp getField(@PathVariable Long id) {
        return msgFieldSetCompDao.findOne(id);
    }

    @RequestMapping(value = "/field/edit", method = RequestMethod.POST)
    @ResponseBody
    public Result fieldSetCompEdit(Long id,MsgFieldSetComp field){
        Result ret = new Result();
        try {
            msgFieldSetService.saveFiled(id,field);
            ret.setSuccess(true);
            logger.info("@M|true|修改域要素成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|修改域要素失败！");
        }

        return ret;
    }

    @RequestMapping(value = "/get/{id}")
    @ResponseBody
    public MsgFieldSet get(@PathVariable Long id) {
        return msgFieldSetService.get(id);
    }

    @RequestMapping(value = "/getSelected", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getSelected(@RequestParam("ids[]")Long[] ids){
        Map<String,Object> ret = new HashMap<>();
        if (ids == null || ids.length < 1){
            ret.put("total",0);
            ret.put("rows",new ArrayList<>());
            return ret;
        }
        List<Long> idList = new ArrayList<>();
        for (Long id:ids){
            idList.add(id);
        }
        try {
            List<MsgField> list = msgFieldSetService.getFieldsByIds(idList);
            List<Map<String, Object>> data = new ArrayList<>();
            for (MsgField filed:list){
                Map<String, Object> map = BeanUtils.describe(filed);
                map.put("moFlagDis",filed.isMoFlag()?"是":"否");
                map.put("fixFlagDis",filed.isFixFlag()?"是":"否");
                data.add(map);
            }

            if (list == null || list.size() < 1){
                ret.put("total",0);
                ret.put("rows",list);
                return ret;
            }
            ret.put("total",list.size());
            ret.put("rows",data);
        }catch (Exception e){

        }

        return ret;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result addFieldSet(MsgFieldSet fieldSet){
        Result ret = new Result();
        try {
            msgFieldSetService.addFieldSet(fieldSet);
            ret.setSuccess(true);
            logger.info("@M|true|添加或修改通用业务要素集或报文头成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|添加或修改通用业务要素集或报文头失败！");
        }

        return ret;
    }

    @RequestMapping(value = "/addFieldSetComps", method = RequestMethod.POST)
    @ResponseBody
    public Result addFieldSetComps(String fieldSetId,String ids, @RequestBody List<MsgFieldSetComp> list){
        Result ret = new Result();
        return ret;
    }

    @RequestMapping(value = "/addFields", method = RequestMethod.POST)
    @ResponseBody
    public Result addFields(Long fieldSetId,@RequestParam("ids[]") Long[] ids){
        Result ret = null;
        try {
            List<Long> list = Arrays.asList(ids);
            List<String> stringList=msgFieldSetService.addFields(fieldSetId,list);
            if(stringList.size()==0){
                ret=new Result(true,"域要素添加成功");
                logger.info("@M|true|添加域要素成功！");
            }else{
                StringBuilder result = new StringBuilder();
                boolean flag = false;
                for (String string : stringList) {
                    if (flag) {
                        result.append(",");
                    } else {
                        flag = true;
                    }
                    result.append(string);
                }
                ret=new Result(false, result.toString() + "域值在通用业务要素集表中已存在");
                logger.warn("@M|false|域值已存在");
             //  ret.setMsg("\"要素组成中\" + result.toString() + \"域值在业务要素集表中已存在");
            }
        }catch (Exception e){
            e.printStackTrace();
            ret=new Result(false,"操作失败");
            logger.error("@M|false|添加域要素失败！");
        }
        return ret;
    }
    @RequestMapping(value = "/delFields", method = RequestMethod.POST)
    @ResponseBody
    public Result delFields(Long fieldSetId,@RequestParam("ids[]") Long[] ids){
        Result ret = new Result();
        try {
            List<Long> list = Arrays.asList(ids);
            msgFieldSetService.delFields(fieldSetId,list);
            logger.info("@M|true|删除域要素成功！");
        }catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|删除域要素失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/getFields", method = RequestMethod.POST)
    @ResponseBody
    public List<MsgFieldSetComp> getFields( @RequestParam(required=false)Long id){
        if (id == null){
            return new ArrayList<>();
        }
        return msgFieldSetService.get(id).getFields();
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delFieldSet(@RequestParam("ids[]")Long[] ids) {
        Result ret = new Result();
        try {
            List<Long> list = Arrays.asList(ids);
            msgFieldSetService.deleteFieldSets(list);
            ret.setSuccess(true);
            logger.info("@M|true|删除通用业务要素集成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|删除通用业务要素集失败！");
        }
        return ret;
    }


}
