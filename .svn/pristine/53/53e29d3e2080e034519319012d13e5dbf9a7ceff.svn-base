package com.zdtech.platform.web.controller.simulator;

import com.zdtech.platform.framework.entity.MsgFieldSet;
import com.zdtech.platform.framework.entity.MsgFormat;
import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.framework.entity.SimSystem;
import com.zdtech.platform.service.message.MsgFieldSetService;
import com.zdtech.platform.service.message.MsgFormatService;
import com.zdtech.platform.service.simulator.SimSystemService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by htma on 2016/5/9.
 */
@Controller
@RequestMapping("/sim/system")
class SimSystemController {
    private static Logger logger = LoggerFactory.getLogger(SimSystemController.class);
    @Autowired
    private SimSystemService simSystemService;
    @Autowired
    private MsgFieldSetService msgFieldSetService;
    @Autowired
    private MsgFormatService msgFormatService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String fieldList() {
        return "simulater/system/simulation-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "simulater/system/simulation-add";
    }

    @RequestMapping(value = "/getAll")
    @ResponseBody
    public Map<String,Object> getAll(){
        Map<String,Object> ret = null;
        try {
            ret = simSystemService.getAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }
    @RequestMapping(value="/get/{id}")
    @ResponseBody
    public SimSystem getSim(@PathVariable("id") Long id){
        return simSystemService.get(id);
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delSim(@RequestParam("ids[]")Long[] ids) {
        Result ret = new Result();
        try{
            List<Long> idList = new ArrayList<>();
            for (Long id:ids){
                idList.add(id);
            }
            simSystemService.deleteSims(idList);
            logger.info("@C|true|删除仿真系统成功！");
        }catch(Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|删除仿真系统失败！");
        }
        return ret;
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result addSim(SimSystem simSystem,String headFieldSetId,String msgFormatId){
        Result ret = new Result();
        try {
            MsgFieldSet msgFieldSet = null;
            MsgFormat msgFormat =null;
            if (!StringUtils.isEmpty(headFieldSetId)){
                msgFieldSet = msgFieldSetService.get(Long.parseLong(headFieldSetId));
            }
            if (!StringUtils.isEmpty(msgFormatId)){
                msgFormat =msgFormatService .get(Long.parseLong(msgFormatId));
            }
            Date date = new Date();
            simSystem.setCreateTime(date);
            simSystem.setHeadFieldSet(msgFieldSet);
            simSystem.setMsgFormat(msgFormat);
            simSystemService.addSim(simSystem);
            logger.info("@C|true|添加或修改仿真系统成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|添加或修改仿真系统失败！");
        }
        return  ret;
    }

    @RequestMapping(value = "/getAllSimSystemOrder", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> getAllSimSystemOrder(@RequestParam(required = false)String type){
        return simSystemService.getAllSimSystemOrder(type);
    }

}
