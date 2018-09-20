package com.zdtech.platform.web.controller.simulator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.SimRecvQueueDao;
import com.zdtech.platform.framework.repository.SimSysinsConfDao;
import com.zdtech.platform.framework.repository.SysAdapterDao;
import com.zdtech.platform.framework.repository.SysAdapterConfDao;
import com.zdtech.platform.framework.repository.SysConfDao;
import com.zdtech.platform.framework.service.FieldCacheService;
import com.zdtech.platform.framework.service.SysCodeService;
import com.zdtech.platform.service.simulator.SimSystemIntanceService;
import com.zdtech.platform.service.simulator.SimSystemService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by htma on 2016/5/12.
 */
@Controller
@RequestMapping("/sim/instance")
class SimSystemIntanceController {
    private static Logger logger = LoggerFactory.getLogger(SimSystemIntanceController.class);
    @Autowired
    private SimSystemIntanceService simSystemIntanceService;

    @Autowired
    private SimSystemService simSystemService;
    @Autowired
    private SimSysinsConfDao simSysinsConfDao;
    @Autowired
    private SysCodeService sysCodeService;
    @Autowired
    private SysConfDao sysConfDao;
    @Autowired
    private SimRecvQueueDao simRecvQueueDao;
    @Autowired
    private SysAdapterDao sysAdapterDao;
    @Autowired
    private SysAdapterConfDao sysAdapterConfDao;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String fieldList() {
        return "simulater/instance/instance-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        SysConf conf = sysConfDao.findByCategoryAndKey("SIMULATOR_SERVER","IP");
        String ip = conf == null?"":conf.getKeyVal();
        conf = sysConfDao.findByCategoryAndKey("SIMULATOR_SERVER","PORT");
        String port = conf == null?"":conf.getKeyVal();
        model.addAttribute("port",port);
        model.addAttribute("ip",ip);
        conf = sysConfDao.findByCategoryAndKey("SIMULATOR_SERVER","ECDS_IMME_SEND_QUEUE");
        String immeSendQueue = conf.getKeyVal();
        conf = sysConfDao.findByCategoryAndKey("SIMULATOR_SERVER","ECDS_SEND_QUEUE");
        String sendQueue = conf.getKeyVal();
        model.addAttribute("sendQueue",sendQueue);
        model.addAttribute("immeSendQueue",immeSendQueue);
        conf = sysConfDao.findByCategoryAndKey("SIMULATOR_SERVER","ECDS_IMME_RECV_QUEUE");
        String immeRecvQueue = conf.getKeyVal();
        conf = sysConfDao.findByCategoryAndKey("SIMULATOR_SERVER","ECDS_RECV_QUEUE");
        String recvQueue = conf.getKeyVal();
        model.addAttribute("receiveQueue",recvQueue);
        model.addAttribute("immeRecvQueue",immeRecvQueue);
        return "simulater/instance/instance-add";
    }

    @RequestMapping(value = "/get/{id}")
    @ResponseBody
    public SimSystemInstance getSimInstance(@PathVariable("id") Long id) {
        return simSystemIntanceService.get(id);
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delSimSysIntances(@RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        try {
            List<Long> idList = new ArrayList<>();
            for (Long id : ids) {
                idList.add(id);
            }
            for (Long id:ids){
                SimSystemInstance sim = simSystemIntanceService.get(id);
                SimSystemInstance.SysInsState state = sim.getState();
                if (state != SimSystemInstance.SysInsState.Finished){
                    ret.setSuccess(false);
                    ret.setMsg(String.format("请先将仿真实例[%s]停止后再删除",sim.getName()));
                    return ret;
                }
            }
            simSystemIntanceService.deleteSimSysIntances(idList);
            logger.info("@C|true|删除仿真实例成功！");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|删除仿真实例失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result addSimSysInstance(SimSystemInstance simSystemInstance, String simSystemId, String recvQueueList, Long adapterId) {
        Result ret = new Result();
        try {
            SimSystem simSystem = null;
            SysAdapter adapter = null;
            if(StringUtils.isNotEmpty(recvQueueList)){
                List<SimRecvQueue> list = new ObjectMapper().readValue(recvQueueList, new TypeReference<List<SimRecvQueue>>() {});
                if(list != null){
                    for(SimRecvQueue queue : list){
                        queue.setSimSystemInstance(simSystemInstance);
                        queue.setId(null);
                    }
                }
                simSystemInstance.setSimRecvQueueData(list);
            }
            if (!StringUtils.isEmpty(simSystemId)) {
                simSystem = simSystemService.get(Long.parseLong(simSystemId));
            }
            if(adapterId != null){
                adapter = sysAdapterDao.findOne(adapterId);
            }
            simSystemInstance.setSimSystem(simSystem);
            simSystemInstance.setAdapter(adapter);
            simSystemInstance.setConnectFlag(true);
            if(StringUtils.isNotEmpty(simSystemInstance.getTcpMode())){
                simSystemInstance.setHeartbeatFlag(true);
                //全双工
                if("FULLDUPLEX".equals(simSystemInstance.getTcpMode())){
                    SysConf conf = sysConfDao.findByCategoryAndKey("SIMULATOR_SERVER","FE_HEARTBEAT_DATA");
                    String heartbeatData = conf.getKeyVal();
                    simSystemInstance.setHeartbeatSendData(heartbeatData);
                    simSystemInstance.setHeartbeatRecvData(heartbeatData);
                } else {//半双工
                    SysConf conf = sysConfDao.findByCategoryAndKey("SIMULATOR_SERVER","MB_HEARTBEAT_DATA");
                    String heartbeatData = conf.getKeyVal();
                    simSystemInstance.setHeartbeatSendData(heartbeatData);
                    simSystemInstance.setHeartbeatRecvData(heartbeatData);
                }
            } else {
                simSystemInstance.setHeartbeatFlag(false);
            }
            ret = simSystemIntanceService.addSimSysInstance(simSystemInstance);
            logger.info("@C|true|添加或修改仿真实例成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@C|false|添加或修改仿真实例失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/state", method = RequestMethod.GET)
    public String stateList() {
        return "test/simulatorserver/simulator-instance-state";
    }

    @RequestMapping(value = "/getAllSimSystemOrder", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> getAllSimSystemOrder(@RequestParam(required = false)String type){
        return simSystemIntanceService.getAllSimSystemOrder(type);
    }

    @RequestMapping(value = "/copy", method = RequestMethod.GET)
    public String simInstanceCopy(){
        return "simulater/instance/instance-copy";
    }

    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    @ResponseBody
    public Result simInstanceCopy(Long srcId,SimSystemInstance instance, String recvQueueList){
        Result ret = new Result();
        try {
            if(StringUtils.isNotEmpty(recvQueueList)){
                List<SimRecvQueue> list = new ObjectMapper().readValue(recvQueueList, new TypeReference<List<SimRecvQueue>>() {});
                if(list != null){
                    for(SimRecvQueue queue : list){
                        queue.setSimSystemInstance(instance);
                        queue.setId(null);
                    }
                }
                instance.setSimRecvQueueData(list);
            }
            if(instance.getSimRecvQueueData() == null || instance.getSimRecvQueueData().isEmpty()){
                List<SimRecvQueue> queueList = simRecvQueueDao.fingByInstanceId(srcId);
                List<SimRecvQueue> list = new ArrayList<>();
                for(SimRecvQueue queue : queueList){
                    SimRecvQueue simRecvQueue = new SimRecvQueue();
                    simRecvQueue.setCcsidRecv(queue.getCcsidRecv());
                    simRecvQueue.setChannelRecv(queue.getChannelRecv());
                    simRecvQueue.setHostNameRecv(queue.getHostNameRecv());
                    simRecvQueue.setPasswordRecv(queue.getPasswordRecv());
                    simRecvQueue.setPortRecv(queue.getPortRecv());
                    simRecvQueue.setQueueManagerRecv(queue.getQueueManagerRecv());
                    simRecvQueue.setQueueNameRecv(queue.getQueueNameRecv());
                    simRecvQueue.setReceiveQueueRecv(queue.getReceiveQueueRecv());
                    simRecvQueue.setUserIdRecv(queue.getUserIdRecv());
                    simRecvQueue.setSimSystemInstance(instance);
                    list.add(simRecvQueue);
                }
                instance.setSimRecvQueueData(list);
            }
            ret = simSystemIntanceService.cloneSimInstance(srcId,instance);
        }catch (Exception e){
            ret.setMsg("操作失败");
            ret.setSuccess(false);
        }
        return ret;
    }

    @RequestMapping(value = "/paramList", method = RequestMethod.GET)
    public String paramList(){
        return "simulater/instance/param-list";
    }

    @RequestMapping(value = "/paramListData/{id}")
    @ResponseBody
    public List<SimSysinsConf> paramListData(@PathVariable("id") Long simSysinsId){
        List<SimSysinsConf> list = simSysinsConfDao.findBySimSysinsId(simSysinsId);
        return list;
    }

    @RequestMapping(value = "/paramSelect", method = RequestMethod.GET)
    public String paramSelect(){
        return "simulater/instance/param-select";
    }

    @RequestMapping(value = "/paramSet", method = RequestMethod.GET)
    public String paramSet(){
        return "simulater/instance/set-param";
    }

    @RequestMapping(value = "/saveParam", method = RequestMethod.POST)
    @ResponseBody
    public Result saveParam(SimSysinsConf conf){
        Result ret = new Result();
        try {
            simSysinsConfDao.save(conf);
        }catch (Exception e){
            ret.setMsg("操作失败");
            ret.setSuccess(false);
        }
        return ret;
    }

    @RequestMapping(value = "/dict/{category}/{adapterId}", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> dict(@PathVariable("category") String category, @PathVariable("adapterId") Long adapterId) {
        Map<String, String> dict = sysCodeService.getCategoryCodes(category);
        List<SysAdapterConf> list = sysAdapterConfDao.findBySimAdapterId(adapterId);
        List<Map<String, String>> rnt = new ArrayList<Map<String, String>>();
        if (dict == null || dict.isEmpty())
            return rnt;
        Map<String, String> map = null;
        for (String key : dict.keySet()) {
            if(hasConf(key, list)) continue;
            map = new HashMap<String, String>();
            map.put("id", key);
            map.put("text", dict.get(key));
            rnt.add(map);
        }
        return rnt;
    }
    private boolean hasConf(String key, List<SysAdapterConf> list){
        for(SysAdapterConf c : list){
            if(key.equals(c.getParamKey()))
                return true;
        }
        return false;
    }

    @RequestMapping(value = "/deleteParam", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteParam(@RequestParam("ids[]") Long[] ids){
        Result ret = new Result();
        try {
            for(Long id : ids){
                SimSysinsConf conf = simSysinsConfDao.findOne(id);
            }
            simSysinsConfDao.deleteByIds(Arrays.asList(ids));
        }catch (Exception e){
            ret.setMsg("操作失败");
            ret.setSuccess(false);
        }
        return ret;
    }

    @RequestMapping(value = "/recvQueue", method = RequestMethod.GET)
    public String recvQueue() {
        return "simulater/instance/recv-queue-data";
    }

    @RequestMapping(value = "/recvQueueData", method = RequestMethod.POST)
    @ResponseBody
    public List<SimRecvQueue> recvQueueData(Long instanceId){
        if(instanceId.intValue() == 0){
            return new ArrayList<SimRecvQueue>();
        }
        return simRecvQueueDao.fingByInstanceId(instanceId);
    }

    @RequestMapping(value = "/addRecvQueue", method = RequestMethod.GET)
    public String addRecvQueue(Long instanceId, Model model) {
        model.addAttribute("instanceId", instanceId);
        return "simulater/instance/recv-queue-add";
    }

    @RequestMapping(value = "/addRecvQueueData", method = RequestMethod.POST)
    @ResponseBody
    public Result addRecvQueueData(SimRecvQueue simRecvQueue, Long instanceId){
        Result ret = new Result();
        try {
            SimSystemInstance instance = simSystemIntanceService.get(instanceId);
            simRecvQueue.setSimSystemInstance(instance);
            simRecvQueueDao.save(simRecvQueue);
            ret.setSuccess(true);
        }catch (Exception e){
            ret.setMsg("操作失败");
            ret.setSuccess(false);
        }
        return ret;
    }

    @RequestMapping(value = "/deleteRecvQueue", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteRecvQueue(@RequestParam("ids[]") Long[] ids){
        Result ret = new Result();
        try {
            simRecvQueueDao.deleteByIds(Arrays.asList(ids));
        }catch (Exception e){
            ret.setMsg("操作失败");
            ret.setSuccess(false);
        }
        return ret;
    }

    @RequestMapping(value = "/selectRecvQueue", method = RequestMethod.GET)
    public String selectRecvQueue() {
        return "simulater/instance/recv-queue-select";
    }

    @RequestMapping(value = "/selectRecvQueueData", method = RequestMethod.POST)
    @ResponseBody
    public List<SimRecvQueue> selectRecvQueueData(Long instanceId){
        if(instanceId.intValue() == 0){
            return simRecvQueueDao.fingAllDistinct();
        }
        return simSystemIntanceService.fingAllByInstanceId(instanceId);
    }

    @RequestMapping(value = "/addRecvQueueDataList", method = RequestMethod.POST)
    @ResponseBody
    public Result addRecvQueueDataList(List<SimRecvQueue> queues, Long instanceId){
        Result ret = new Result();
        try {
            SimSystemInstance instance = simSystemIntanceService.get(instanceId);
            for(SimRecvQueue queue : queues){
                queue.setSimSystemInstance(instance);
            }
            simRecvQueueDao.save(queues);
            ret.setSuccess(true);
        }catch (Exception e){
            ret.setMsg("操作失败");
            ret.setSuccess(false);
        }
        return ret;
    }
}
