package com.zdtech.platform.simserver.service;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.service.SysCodeService;
import com.zdtech.platform.simserver.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CommonService
 *
 * @author panli
 * @date 2016/7/25
 */
@Service("commonService")
public class CommonService {
    private static Logger logger = LoggerFactory.getLogger(CommonService.class);

    @Value("${server.address}")
    private String serverAddress;
    @Autowired
    private SysConfDao sysConfDao;
    @Autowired
    private SysCodeService codeService;
    @Autowired
    private SimSysinsConfDao simSysinsConfDao;
    @Autowired
    private SysAdapterConfDao adapterConfDao;

    public Integer getServerPort(){
        String value = getSysConf(Constants.SIMULATOR_SERVER_CATEGORY,Constants.SIMULATOR_SERVER_PORT);
        return Integer.parseInt(value);
    }

    public String getServerHost(){
        String value = getSysConf(Constants.SIMULATOR_SERVER_CATEGORY,Constants.SIMULATOR_SERVER_IP);
        return value;
    }
    public Integer getCorePoolSize(){
        String value = getSysConf(Constants.SIMULATOR_SERVER_CATEGORY,Constants.SIMULATOR_SERVER_CORE_POOL_SIZE);
        return Integer.parseInt(value);
    }

    public Integer getMaxPoolSize(){
        String value = getSysConf(Constants.SIMULATOR_SERVER_CATEGORY,Constants.SIMULATOR_SERVER_MAX_POOL_SIZE);
        return Integer.parseInt(value);
    }

    public Integer getKeepAlive(){
        String value = getSysConf(Constants.SIMULATOR_SERVER_CATEGORY,Constants.SIMULATOR_SERVER_KEEP_ALIVE_SECONDS);
        return Integer.parseInt(value);
    }
    public Integer getWorkQueue(){
        String value = getSysConf(Constants.SIMULATOR_SERVER_CATEGORY,Constants.SIMULATOR_SERVER_WORK_QUEUE);
        return Integer.parseInt(value);
    }

    public String getLogUrl(){
        String value = getSysConf(Constants.SIMULATOR_SERVER_CATEGORY,Constants.SIMULATOR_SERVER_LOG_NOTIFY_URL);
        return value;
    }

    public String getSysConf(String category,String key){
        String value = codeService.getConfValueByCategoryAndKey(category,key);
        if (StringUtils.isNotEmpty(value)){
            return value;
        }
        SysConf sysConf = sysConfDao.findByCategoryAndKey(category,key);
        if (sysConf == null || sysConf.getKeyVal() == null){
            return null;
        }
        return sysConf.getKeyVal();
    }


//    public Map<String,String> getSimInsConfig(Long simId) {
//        Map<String,String> ret = new HashMap<>();
//        List<SimSysinsConf> list = simSysinsConfDao.findBySimSysinsId(simId);
//        if (list != null){
//            for (SimSysinsConf conf:list){
//                ret.put(conf.getParamKey(),conf.getParamValue());
//                WLSimService.simInsMap.put(simId + conf.getParamKey(), conf.getParamValue());
//            }
//        }
//        return ret;
//    }

    public Map<String,String> getAdapterConfig(Long adapterId) {
        Map<String,String> ret = new HashMap<>();
        List<SysAdapterConf> list = adapterConfDao.findBySimAdapterId(adapterId);
        if (list != null){
            for (SysAdapterConf conf:list){
                ret.put(conf.getParamKey(),conf.getParamValue());
                WLSimService.simInsMap.put(adapterId + conf.getParamKey(), conf.getParamValue());
            }
        }
        return ret;
    }
}
