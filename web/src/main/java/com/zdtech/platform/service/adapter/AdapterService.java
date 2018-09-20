package com.zdtech.platform.service.adapter;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huangbo on 2018/8/9.
 */
@Service
public class AdapterService {
    @Autowired
    private SysAdapterDao sysAdapterDao;
    @Autowired
    private SysAdapterHttpDao sysAdapterHttpDao;
    @Autowired
    private SysAdapterMqDao sysAdapterMqDao;
    @Autowired
    private SysAdapterTcpDao sysAdapterTcpDao;
    @Autowired
    private SysAdapterConfDao sysAdapterConfDao;
    @Autowired
    private SysAdapterQueueDao sysAdapterQueueDao;

    public Result copyAdapter(Long[] ids) {
        Result result = new Result();
        Long newId;
        try {
            for (int i = ids.length - 1; i >= 0; i--) {
                SysAdapter sysAdapter = sysAdapterDao.findOne(ids[i]);
                String type = sysAdapter.getAdapterType();
                SysAdapter newAdapter = new SysAdapter();
                newAdapter.setAdapterType(type);
                newAdapter.setName(sysAdapter.getName());
                newAdapter.setAdapterStatus(sysAdapter.getAdapterStatus());
                newAdapter.setResponseModel(sysAdapter.getResponseModel());
                newAdapter.setInternalTime(sysAdapter.getInternalTime());
                newAdapter.setDescription(sysAdapter.getDescription());
                switch (type) {
                    case "HTTP":
                        SysAdapterHttp sysAdapterHttp = sysAdapterHttpDao.findOne(ids[i]);
                        SysAdapterHttp newAdapterHttp = new SysAdapterHttp();
                        newAdapterHttp.setAdapterType(type);
                        newAdapterHttp.setName(sysAdapter.getName());
                        newAdapterHttp.setAdapterStatus(sysAdapter.getAdapterStatus());
                        newAdapterHttp.setResponseModel(sysAdapter.getResponseModel());
                        newAdapterHttp.setInternalTime(sysAdapter.getInternalTime());
                        newAdapterHttp.setDescription(sysAdapter.getDescription());
                        newAdapterHttp.setLocalUrl(sysAdapterHttp.getLocalUrl());
                        newAdapterHttp.setRemoteUrl(sysAdapterHttp.getRemoteUrl());
                        sysAdapterHttpDao.save(newAdapterHttp);
                        newId = newAdapterHttp.getId();
                        break;
                    case "TCP":
                        SysAdapterTcp sysAdapterTcp = sysAdapterTcpDao.findOne(ids[i]);
                        SysAdapterTcp newAdapterTcp = new SysAdapterTcp();

                        newAdapterTcp.setAdapterType(type);
                        newAdapterTcp.setName(sysAdapter.getName());
                        newAdapterTcp.setAdapterStatus(sysAdapter.getAdapterStatus());
                        newAdapterTcp.setResponseModel(sysAdapter.getResponseModel());
                        newAdapterTcp.setInternalTime(sysAdapter.getInternalTime());
                        newAdapterTcp.setDescription(sysAdapter.getDescription());

                        newAdapterTcp.setHeartbeatRecvData(sysAdapterTcp.getHeartbeatRecvData());
                        newAdapterTcp.setHeartbeatSendData(sysAdapterTcp.getHeartbeatSendData());
                        newAdapterTcp.setHeartbeatFlag(sysAdapterTcp.isHeartbeatFlag());
                        newAdapterTcp.setClientIp(sysAdapterTcp.getClientIp());
                        newAdapterTcp.setClientPort(sysAdapterTcp.getClientPort());
                        newAdapterTcp.setServerIp(sysAdapterTcp.getServerIp());
                        newAdapterTcp.setServerPort(sysAdapterTcp.getServerPort());
                        newAdapterTcp.setTcpMode(sysAdapterTcp.getTcpMode());
                        sysAdapterTcpDao.save(newAdapterTcp);
                        newId = newAdapterTcp.getId();
                        break;
                    case "MQ":
                        SysAdapterMq sysAdapterMq = sysAdapterMqDao.findOne(ids[i]);
                        SysAdapterMq newAdapterMq = new SysAdapterMq();
                        newAdapterMq.setAdapterType(type);
                        newAdapterMq.setName(sysAdapter.getName());
                        newAdapterMq.setAdapterStatus(sysAdapter.getAdapterStatus());
                        newAdapterMq.setResponseModel(sysAdapter.getResponseModel());
                        newAdapterMq.setInternalTime(sysAdapter.getInternalTime());
                        newAdapterMq.setDescription(sysAdapter.getDescription());
                        newAdapterMq.setBankNo(sysAdapterMq.getBankNo());
                        sysAdapterMqDao.save(newAdapterMq);
                        newId = newAdapterMq.getId();
                        saveNewQueue(ids[i], newId);
                        break;
                    default:
                        newId = null;
                        break;

                }
                if (newId != null) {
                    saveNewConf(ids[i], newId);
                }
            }
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }
        return result;
    }

    private void saveNewQueue(Long id, Long newId) {
        List<SysAdapterQueue> sysAdapterQueueList = sysAdapterQueueDao.findByAdapter(id);
        for (SysAdapterQueue sysAdapterQueue : sysAdapterQueueList) {
            SysAdapterQueue newQueue = new SysAdapterQueue();
            newQueue.setAdapterId(newId);
            newQueue.setType(sysAdapterQueue.getType());
            newQueue.setCcsid(sysAdapterQueue.getCcsid());
            newQueue.setChannel(sysAdapterQueue.getChannel());
            newQueue.setIp(sysAdapterQueue.getIp());
            newQueue.setPassword(sysAdapterQueue.getPassword());
            newQueue.setPort(sysAdapterQueue.getPort());
            newQueue.setQueue(sysAdapterQueue.getQueue());
            newQueue.setQueueMgr(sysAdapterQueue.getQueueMgr());
            newQueue.setUser(sysAdapterQueue.getUser());
            sysAdapterQueueDao.save(newQueue);
        }
    }

    private void saveNewConf(Long id, Long newId) {
        List<SysAdapterConf> sysAdapterConfList = sysAdapterConfDao.findBySimAdapterId(id);
        for (SysAdapterConf sysAdapterConf : sysAdapterConfList) {
            SysAdapterConf newConf = new SysAdapterConf();
            newConf.setAdapterId(newId);
            newConf.setParamKey(sysAdapterConf.getParamKey());
            newConf.setParamKeyName(sysAdapterConf.getParamKeyName());
            newConf.setParamValue(sysAdapterConf.getParamValue());
            sysAdapterConfDao.save(newConf);
        }
    }

    public Result addAdapter(SysAdapter sysAdapter, SysAdapterHttp sysAdapterHttp, SysAdapterMq sysAdapterMq, SysAdapterTcp sysAdapterTcp,String type, String sendQueue, String recvQueue){
        Result result = new Result();
        try {
            sysAdapter.setAdapterType(type);
            switch (type) {
                case "HTTP":
                    sysAdapterHttp.setAdapterType(type);
                    sysAdapterHttp.setInternalTime(sysAdapterHttp.getInternalTime()==null?0:sysAdapterHttp.getInternalTime());
                    sysAdapterHttp.setResponseModel(sysAdapterHttp.getResponseModel()==null?true:sysAdapterHttp.getResponseModel());
                    sysAdapterHttp.setAdapterStatus(sysAdapterHttp.getAdapterStatus()==null?false:sysAdapterHttp.getAdapterStatus());
                    sysAdapterHttpDao.save(sysAdapterHttp);
                    break;
                case "TCP":
                    sysAdapterTcp.setAdapterType(type);
                    sysAdapterTcp.setInternalTime(sysAdapterTcp.getInternalTime()==null?0:sysAdapterTcp.getInternalTime());
                    sysAdapterTcp.setResponseModel(sysAdapterTcp.getResponseModel()==null?true:sysAdapterTcp.getResponseModel());
                    sysAdapterTcp.setAdapterStatus(sysAdapterTcp.getAdapterStatus()==null?false:sysAdapterTcp.getAdapterStatus());
                    sysAdapterTcp.setHeartbeatFlag(true);
                    sysAdapterTcp.setHeartbeatRecvData("00000000");
                    sysAdapterTcp.setHeartbeatSendData("00000000");
                    sysAdapterTcpDao.save(sysAdapterTcp);
                    break;
                case "MQ":
                    sysAdapterMq.setAdapterType(type);
                    sysAdapterMq.setInternalTime(sysAdapterMq.getInternalTime()==null?0:sysAdapterMq.getInternalTime());
                    sysAdapterMq.setResponseModel(sysAdapterMq.getResponseModel()==null?true:sysAdapterMq.getResponseModel());
                    sysAdapterMq.setAdapterStatus(sysAdapterMq.getAdapterStatus()==null?false:sysAdapterMq.getAdapterStatus());
                    sysAdapterMqDao.save(sysAdapterMq);
                    JSONArray sendJson = JSONArray.fromObject(sendQueue);
                    List<SysAdapterQueue> sendQueueList= (List<SysAdapterQueue>)JSONArray.toCollection(sendJson, SysAdapterQueue.class);
                    JSONArray recvJson = JSONArray.fromObject(recvQueue);
                    List<SysAdapterQueue> recvQueueList= (List<SysAdapterQueue>)JSONArray.toCollection(recvJson, SysAdapterQueue.class);
                    sysAdapterQueueDao.deleteAllByAdapterId(sysAdapterMq.getId());
                    for (SysAdapterQueue send:sendQueueList){
                        send.setAdapterId(sysAdapterMq.getId());
                        send.setType("send");
                        sysAdapterQueueDao.save(send);
                    }
                    for (SysAdapterQueue recv:recvQueueList){
                        recv.setAdapterId(sysAdapterMq.getId());
                        recv.setType("recv");
                        sysAdapterQueueDao.save(recv);

                    }
                    break;
                default:
                    break;
            }

            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg("创建适配器失败");
        }
        return result;
    }
}
