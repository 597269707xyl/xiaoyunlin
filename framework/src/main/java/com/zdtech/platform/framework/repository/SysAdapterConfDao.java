package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.SysAdapterConf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by htma on 2016/5/12.
 */
public interface SysAdapterConfDao extends JpaRepository<SysAdapterConf, Long>, JpaSpecificationExecutor<SysAdapterConf> {

    @Query(value = "select t from SysAdapterConf t where t.adapterId = ?1")
    public List<SysAdapterConf> findBySimAdapterId(Long adapterId);

    @Modifying
    @Transactional
    @Query(value = "delete from SysAdapterConf t where t.id in (?1)")
    int deleteByIds(List<Long> ids);

    @Query(value = "select t.paramValue from SysAdapterConf t where t.adapterId=?1 and t.paramKey=?2")
    String findByInsIdAndConf(Long id, String fe_echo_feno);

    @Query(value = "select t from SysAdapterConf t where t.paramKey = 'nxy_sys_date'")
    List<SysAdapterConf> findSysDate();
}
