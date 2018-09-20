package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.SimMsgFieldValue;
import com.zdtech.platform.framework.entity.SimSysinsMsgFieldValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SimMessageFieldDao
 *
 * @author wzx
 * @date  2016/8/3.
 */
public interface SimSysinsMsgFieldValueDao extends JpaRepository<SimSysinsMsgFieldValue, Long>, JpaSpecificationExecutor<SimSysinsMsgFieldValue> {
    @Query(value="select s from SimSysinsMsgFieldValue s  where s.simMessageField.id=?1")
    List<SimSysinsMsgFieldValue> findByFid(Long id);
    @Transactional
    @Modifying
    @Query(value="delete from SimSysinsMsgFieldValue s  where s.simMessageField.id=?1 and s.flag=0")
    void deleteByFid(Long id);
}
