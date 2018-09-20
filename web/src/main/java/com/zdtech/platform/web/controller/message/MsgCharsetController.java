package com.zdtech.platform.web.controller.message;

import com.zdtech.platform.framework.entity.MsgCharset;
import com.zdtech.platform.framework.entity.MsgCharsetEnum;
import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.framework.repository.MsgCharsetDao;
import com.zdtech.platform.service.message.MsgCharsetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangbo on 2016/5/18.
 */
@Controller
@RequestMapping("/msg/charset")
public class MsgCharsetController {
    private static Logger logger = LoggerFactory.getLogger(MsgCharsetController.class);
    @Autowired
    private MsgCharsetService msgCharsetService;
    @Autowired
    private MsgCharsetDao msgCharsetDao;

    @RequestMapping(value = "/list")
    public String funcList(){
        return "message/datatype/charset/charset-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(){
        return "message/datatype/charset/charset-add";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editProj(@PathVariable("id") Long id, Model model) {
        MsgCharset msgCharset = msgCharsetDao.findOne(id);
        model.addAttribute("msgCharset",msgCharset);
        List<MsgCharsetEnum> msgCharsetEnumSet = msgCharset.getChars();
        StringBuilder tempOfEnum = new StringBuilder();
        for (MsgCharsetEnum tempForLoop : msgCharsetEnumSet){
            tempOfEnum.append(tempForLoop.getValue());
            tempOfEnum.append(",");
        }
        if (tempOfEnum.length()>0 && tempOfEnum.charAt(tempOfEnum.length()-1)==','){
            tempOfEnum.deleteCharAt(tempOfEnum.length()-1);
        }
        model.addAttribute("enumeration",tempOfEnum.toString());
        return "/message/datatype/charset/charset-edit";
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.POST)
    @ResponseBody
    public Result addCharset(MsgCharset msgCharset, String enumeration) {
        Result ret = new Result();
        try {
            ret = msgCharsetService.addCharset(msgCharset,enumeration);
            logger.info("@M|true|添加字符集成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|添加字符集失败！");
        }
        return  ret;
    }

    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    @ResponseBody
    public Result editCharset(MsgCharset msgCharset, String enumeration, String id) {
        Result ret = new Result();
        try {
            ret = msgCharsetService.editCharset(msgCharset,enumeration,id);
            logger.info("@M|true|修改字符集成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|修改字符集失败！");
        }
        return  ret;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delMsgCharset(@RequestParam("id") Long id){
        Result ret = new Result();
        try {
            msgCharsetService.delCharset(id);
            logger.info("@M|true|删除字符集成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@M|false|删除字符集失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> query(@RequestParam Map<String, Object> params, @PageableDefault Pageable page){
        Map<String,Object> ret = new HashMap<>();
        ret = msgCharsetService.findMsgCharsetEnum(params,page);
        return ret;
    }

    @RequestMapping(value = "/getAll")
    @ResponseBody
    public Map<String,Object> getAllCharset(){
        Map<String,Object> ret = null;
        try {
            ret = msgCharsetService.getAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }
}
