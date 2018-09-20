package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.SimMessage;
import com.zdtech.platform.framework.entity.SimMessageField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SimMessageFieldDao
 *
 * @author xiaolanli
 * @date  2016/5/11.
 */
public interface SimMessageFieldDao extends JpaRepository<SimMessageField, Long>, JpaSpecificationExecutor<SimMessageField> {
    @Query(value="select s from SimMessageField s join s.msgField where s.simMessage.id=?1 order by s.seq_no asc, s.id asc")
    List<SimMessageField> findByid(Long id);
    @Query(value="select s from SimMessageField s join s.msgField where s.id=?1")
    SimMessageField findByoneid(Long id);
    @Query(value="select s from SimMessageField s where s.msgField.id=?1 and s.simMessage.id=?2")
    SimMessageField findByTwoId(Long id, Long id1);
    @Modifying
    @Transactional
    @Query(value = "delete  from SimMessageField s where s.simMessage.id=?1 and (s.fieldType=?2 or s.fieldType=?3)")
    void deleteByMsgidAndFieldType(Long id, String setType, String setType1);
    @Query(value="select s from SimMessageField s  where s.msgField.id=?1")
    SimMessageField findByFieldId(Long id);
    @Query(value = "delete  from SimMessageField s where s.simMessage.id=?1 and s.fieldType=?2 ")
    void deleteByMsgid(Long id, String setType);

    @Query(value="select s from SimMessageField s where s.msgField.id=?1")
    List<SimMessageField> findByMsgFieldId(Long id);
}
