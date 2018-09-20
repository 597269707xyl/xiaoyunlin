package com.zdtech.platform.simserver.service;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * SimInsService
 *
 * @author panli
 * @date 2016/7/28
 */
@Service("simInsService")
public class SimInsService {
    private static Logger logger = LoggerFactory.getLogger(SimInsService.class);
    @Value("${update.config}")
    private String config;
    @Autowired
    private SimSystemInstanceDao sisInsDao;
    @Autowired
    private SysAdapterMqDao mqDao;
    @Autowired
    private SysAdapterHttpDao httpDao;
    @Autowired
    private SysAdapterTcpDao tcpDao;
    @Autowired
    private SysAdapterQueueDao queueDao;
    @Autowired
    private CommonService commonService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SimSystemInstance get(Long id){
        return sisInsDao.findOne(id);
    }

    /**
     * 更新仿真实例状态
     * @param id 实例id
     * @param state 状态
     * @return
     */
    public void updateState(Long id,SimSystemInstance.SysInsState state){
        String sql = String.format("update sim_system_instance set state = %d where id = %d",state.ordinal(),id);
        logger.info(sql);
        jdbcTemplate.execute(sql);
    }


    public void updateSimState(Long id,int state,int status,String ip){
        String sql = String.format("update sim_system_instance set state = %d,connect_status = %d,ip = '%s' where id = %d",state,status,ip,id);
        logger.info(sql);
        jdbcTemplate.execute(sql);
    }

    public void updateSimState(Long adapterId,boolean state){
        String sql = String.format("update sys_adapter set adapter_status = %d where id = %d",state?1:0,adapterId);
        logger.info(sql);
        jdbcTemplate.execute(sql);
    }

    //仿真实例状态置位
    public void resetSimInsStatus(String ip) {
//        jdbcTemplate.execute("UPDATE sim_system_instance t set t.state = 1,t.ip = ''");
        jdbcTemplate.execute("UPDATE sys_adapter t set t.adapter_status = 0");
    }

    public Map<String,String> getSimInsConfig(Long simId) {
        return commonService.getAdapterConfig(simId);
    }

    public SysAdapterTcp getTcp(Long adapterId){
        return tcpDao.findOne(adapterId);
    }

    public SysAdapterHttp getHttp(Long adapterId){
        return httpDao.findOne(adapterId);
    }

    public SysAdapterMq getMq(Long adapterId){
        return mqDao.findOne(adapterId);
    }

    public List<SysAdapterQueue> getQueue(Long adapterId){
        return queueDao.findByAdapter(adapterId);
    }
}
