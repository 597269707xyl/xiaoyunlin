package com.zdtech.platform.web.controller.message;

import com.zdtech.platform.framework.entity.MsgFormat;
import com.zdtech.platform.framework.entity.MsgFormatComp;
import com.zdtech.platform.framework.entity.MsgFormatCompField;
import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.framework.repository.MsgFormatCompDao;
import com.zdtech.platform.framework.repository.MsgFormatDao;
import com.zdtech.platform.service.message.MsgFormatService;
import org.apache.commons.beanutils.BeanUtils;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by htma on 2016/5/9.
 */
@Controller
@RequestMapping("/msg/format")
public class MsgFormatController {
    private static Logger logger = LoggerFactory.getLogger(MsgFormatController.class);
    @Autowired
    private MsgFormatService msgFormatService;
    @Autowired
    private MsgFormatCompDao msgFormatCompDao;

    @RequestMapping(value = "/getAll")
    @ResponseBody
    public Map<String,Object> getAll(){
        Map<String,Object> ret = null;
        try {
            ret = msgFormatService.getAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(){
        return "message/format/format-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(){
        return "message/format/format-add";
    }

    @RequestMapping(value = "/get/{id}")
    @ResponseBody
    public MsgFormat get(@PathVariable Long id){
        return msgFormatService.get(id);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result addMsgFormat(MsgFormat format){
        Result ret = new Result();
        try {
            msgFormatService.addMsgFormat(format);
            ret.setSuccess(true);
            ret.setMsg("");
            logger.info("@M|true|添加或修改报文格式成功！");
        }catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|添加或修改报文格式失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/getComps", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> getComps(@RequestParam(required=false)Long id){
        if (id == null){
            return new ArrayList<>();
        }
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            List<MsgFormatComp> list = msgFormatService.getFormatComps(id);
            for (MsgFormatComp comp:list){
                Map<String, Object> map = BeanUtils.describe(comp);
                List<MsgFormatCompField> fields = comp.getFields();
                StringBuilder sb = new StringBuilder();
                for (MsgFormatCompField field:fields){
                    sb.append(field.getProperty());
                    sb.append(",");
                }
                if (sb.length()>0 && sb.charAt(sb.length()-1)==','){
                    sb.deleteCharAt(sb.length()-1);
                }
                map.put("fields",sb.toString());
                result.add(map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delFormats(@RequestParam("ids[]")Long[] ids){
        Result ret = new Result();
        try {
            List<Long> list = Arrays.asList(ids);
            msgFormatService.deleteFormats(list);
            ret.setSuccess(true);
            ret.setMsg("");
            logger.info("@M|true|删除报文格式成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|删除报文格式失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/addComp", method = RequestMethod.GET)
    public String addComp(String id, Model model){
        model.addAttribute("formatId",id);
        return "message/format/format-comp-add";
    }

    @RequestMapping(value = "/addComp", method = RequestMethod.POST)
    @ResponseBody
    public Result addComp(Long formatId,MsgFormatComp comp,String protocol){
        Result ret = new Result();
        try {
            msgFormatService.addComp(formatId,comp,protocol);
            logger.info("@M|true|添加或修改报文组成成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|添加或修改报文组成失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/getComp/{id}")
    @ResponseBody
    public Map<String,Object> getComp(@PathVariable Long id){
        Map<String,Object> map = null;
        MsgFormatComp comp = msgFormatCompDao.findOne(id);
        try {
            map = BeanUtils.describe(comp);
            List<MsgFormatCompField> fields = comp.getFields();
            StringBuilder sb = new StringBuilder();
            for (MsgFormatCompField field:fields){
                sb.append(field.getProperty().toString());
                sb.append(",");
            }
            if (sb.length()>0 && sb.charAt(sb.length()-1)==','){
                sb.deleteCharAt(sb.length()-1);
            }
            map.put("protocol",sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @RequestMapping(value = "/delComps", method = RequestMethod.POST)
    @ResponseBody
    public Result delFields(Long formatId,@RequestParam("ids[]") Long[] ids){
        Result ret = new Result();
        try {
            List<Long> list = Arrays.asList(ids);
            msgFormatService.delComps(formatId,list);
            logger.info("@M|true|删除报文组成成功！");
        }catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|删除报文组成失败！");
        }
        return ret;
    }

}
