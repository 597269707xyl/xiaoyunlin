package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.SimSysinsConf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yjli on 2017/3/17.
 */
public interface SimSysinsConfDao extends JpaRepository<SimSysinsConf, Long>, JpaSpecificationExecutor<SimSysinsConf> {

    @Query(value = "select t from SimSysinsConf t where t.simSysinsId = ?1")
    public List<SimSysinsConf> findBySimSysinsId(Long simSysinsId);

    @Modifying
    @Transactional
    @Query(value = "delete from SimSysinsConf t where t.id in (?1)")
    int deleteByIds(List<Long> ids);

    @Query(value = "select t.paramValue from SimSysinsConf t where t.simSysinsId=?1 and t.paramKey=?2")
    String findByInsIdAndConf(Long id, String fe_echo_feno);

    @Query(value = "select t from SimSysinsConf t where t.paramKey = 'nxy_sys_date'")
    List<SimSysinsConf> findSysDate();
}
