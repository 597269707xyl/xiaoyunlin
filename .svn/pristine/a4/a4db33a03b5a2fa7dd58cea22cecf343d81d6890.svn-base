package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.SimMessageField;
import com.zdtech.platform.framework.entity.SimMsgFieldValue;
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
public interface SimMsgFieldValueDao extends JpaRepository<SimMsgFieldValue, Long>, JpaSpecificationExecutor<SimMsgFieldValue> {
    @Query(value="select s from SimMsgFieldValue s  where s.simMessageField.id=?1")
    List<SimMsgFieldValue> findByFid(Long id);
    @Transactional
    @Modifying
    @Query(value="delete from SimMsgFieldValue s  where s.simMessageField.id=?1 and s.flag=0")
    void deleteByFid(Long id);

}
