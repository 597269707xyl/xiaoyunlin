package com.zdtech.platform.web.controller.simulator;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.network.Client;
import com.zdtech.platform.framework.network.entity.*;
import com.zdtech.platform.framework.network.tcp.NettyClient;
import com.zdtech.platform.framework.repository.SimSysinsConfDao;
import com.zdtech.platform.framework.repository.SysAdapterDao;
import com.zdtech.platform.service.simulator.SimSystemIntanceService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * SimInsServerController
 *
 * @author panli
 * @date 2016/6/16
 */
@Controller
@RequestMapping("/sim/instance/server")
public class SimInsServerController {
    private static Logger logger = LoggerFactory.getLogger(SimInsServerController.class);
    @Autowired
    private SimSystemIntanceService simSystemIntanceService;
    @Autowired
    private SysAdapterDao adapterDao;
    @Autowired
    private SimSysinsConfDao simSysinsConfDao;

    @RequestMapping(value = "/startServer", method = RequestMethod.POST)
    @ResponseBody
    public Result startServer(final Long id) {
        Result ret = new Result();
        try {
            SimSystemInstance ssi = simSystemIntanceService.get(id);
            SysAdapter adapter = ssi.getAdapter();
            if (adapter == null){
                ret.setSuccess(false);
                ret.setMsg("实例启动失败，未指定适配器");
                return ret;
            }
            String protocol = ssi.getSimSystem().getProtocol();
            String tcpMode = ssi.getTcpMode();
            if (StringUtils.isNotEmpty(protocol)&&protocol.equalsIgnoreCase("TCP")&&
                    StringUtils.isNotEmpty(tcpMode)&&tcpMode.equalsIgnoreCase("FULLDUPLEX")){
                String feno = simSysinsConfDao.findByInsIdAndConf(id,"FE_ECHO_FENO");
                if (StringUtils.isEmpty(feno)){
                    ret.setSuccess(false);
                    ret.setMsg("实例启动失败，FE仿真实例请在实例参数中配置FE编号");
                    return ret;
                }
            }
            Client client = new NettyClient(ssi.getInsServerIp(),ssi.getInsServerPort());
            client.connect();
            Message msg = new SimStartMessage(id, adapter.getId());
            Request req = new Request(msg);
            client.send(req);
            client.close();
            ret.setSuccess(true);
            ret.setMsg("启动命令发送成功，请刷新查看实例运行状态");
            logger.info("@C|true|启动成功");
        } catch (Exception e) {
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("启动失败");
            logger.error("@C|false|启动失败");
        }
        return ret;
    }

    @RequestMapping(value = "/stopServer", method = RequestMethod.POST)
    @ResponseBody
    public Result stopServer(final Long id){
        Result ret = new Result();
        try {
            SimSystemInstance ssi = simSystemIntanceService.get(id);
            SysAdapter adapter = ssi.getAdapter();
            if (adapter == null){
                ret.setSuccess(false);
                ret.setMsg("实例停止失败，未指定适配器");
                return ret;
            }
            Client client = new NettyClient(ssi.getInsServerIp(),ssi.getInsServerPort());
            client.connect();
            Message msg = new SimStopMessage(id, adapter.getId());
            Request req = new Request(msg);
            client.send(req);
            client.close();
            ret.setSuccess(true);
            ret.setMsg("启动命令发送成功，请刷新查看实例运行状态");
            logger.info("@C|true|停止成功");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("停止失败");
            logger.error("@C|false|停止失败");
    }
        return ret;
    }

    @RequestMapping(value = "/setReply", method = RequestMethod.POST)
    @ResponseBody
    public Result setReply(final Long id, boolean responseModel){
        Result ret = new Result();
        try {
            SimSystemInstance ssi = simSystemIntanceService.get(id);
            SysAdapter adapter = ssi.getAdapter();
            if (adapter == null){
                ret.setSuccess(false);
                ret.setMsg("设置来账应答方式失败，未指定适配器");
                return ret;
            }
            adapter.setResponseModel(responseModel);
            adapterDao.save(adapter);
            ret.setSuccess(true);
            ret.setMsg("设置来账应答方式成功");
            logger.info("@C|true|设置来账应答方式成功");
        }catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("设置来账应答方式失败");
            logger.error("@C|false|设置来账应答方式失败");
    }
        return ret;
    }

    @RequestMapping(value = "/setInternalTime", method = RequestMethod.POST)
    @ResponseBody
    public Result setInternalTime(final Long id, int internalTime){
        Result ret = new Result();
        try {
            SimSystemInstance ssi = simSystemIntanceService.get(id);
            SysAdapter adapter = ssi.getAdapter();
            if (adapter == null){
                ret.setSuccess(false);
                ret.setMsg("设置关联交易缓冲时间失败，未指定适配器");
                return ret;
            }
            adapter.setInternalTime(internalTime*1000);
            adapterDao.save(adapter);
            ret.setSuccess(true);
            Client client = new NettyClient(ssi.getInsServerIp(),ssi.getInsServerPort());
            client.connect();
            Message msg = new InternalTimeMessage(id, ssi.getAdapter().getId(), internalTime*1000);
            Request req = new Request(msg);
            client.send(req);
            client.close();
            ret.setMsg("设置关联交易缓冲时间成功");
            logger.info("@C|true|设置关联交易缓冲时间成功");
        }catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("设置关联交易缓冲时间失败");
            logger.error("@C|false|设置关联交易缓冲时间失败");
    }
        return ret;
    }

    @RequestMapping(value = "/setReply", method = RequestMethod.GET)
    public String stopServerPage(Model model, Long id){
        model.addAttribute("id", id);
        return "test/simulatorserver/set-reply";
    }

    @RequestMapping(value = "/setInternalTime", method = RequestMethod.GET)
    public String setInternalTimePage(Model model, Long id){
        model.addAttribute("id", id);
        return "test/simulatorserver/set-internal-time";
    }
}
