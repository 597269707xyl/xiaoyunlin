package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.MsgField;
import com.zdtech.platform.framework.entity.SimSysInsMessageField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * SimSysInsMessageFieldDao
 * @author xiaolanli
 * @date 2016/5/18
 */
public interface SimSysInsMessageFieldDao extends JpaRepository<SimSysInsMessageField, Long>, JpaSpecificationExecutor<SimSysInsMessageField> {
    @Query(value="select s from SimSysInsMessageField s join s.msgField where s.simSysInsMessage.id=?1 order by s.seqNo asc, s.id asc")
    List<SimSysInsMessageField> findByid(Long id);
    @Query(value = "select t from SimSysInsMessageField t where t.simSysInsMessage.id=?1 and t.fieldType = ?2 order by t.msgField.fieldId asc ")
    List<SimSysInsMessageField> findByMsgIdAndFieldType(Long id,String type);

    @Modifying
    @Transactional
    @Query(value = "delete  from SimSysInsMessageField s where s.simSysInsMessage.id=?1 and (s.fieldType=?2 or s.fieldType=?3)")
    void deleteByMsgidAndFieldType(Long id, String setType, String setType1);
    @Query(value="select s from SimSysInsMessageField s join s.msgField where s.id=?1")
    SimSysInsMessageField findByoneid(Long id);
    @Query(value="select s from SimSysInsMessageField s join s.msgField where s.msgField.id=?1 and s.simSysInsMessage.id=?2")
    SimSysInsMessageField findByTwoId(Long id, Long simMessageid);
    @Query(value = "delete  from SimSysInsMessageField s where s.simSysInsMessage.id=?1 and s.fieldType=?2 ")
    void deleteByMsgid(Long id, String setType);

    @Query(value = "select t from SimSysInsMessageField t where t.simSysInsMessage.id=?1 and t.fieldType = ?2")
    List<SimSysInsMessageField> findFieldsByType(Long msgId,String fieldType);

    @Query(value = "select t.msgField from SimSysInsMessageField t where t.simSysInsMessage.id=?1 order by t.msgField.fieldId asc ")
    List<MsgField> findBodyFields(Long msgId);

    @Query(value = "select t.message_field_id as fieldId,t.default_value as defaultValue,t.resp_value_type as respType,t.resp_value as respValue from sim_sysins_message_field t where t.sysins_message_id=?1",nativeQuery = true)
    List<Object[]> findReplyFields(Long replyId);

    @Query(value = "select t from SimSysInsMessageField t where t.simSysInsMessage.id=?1 order by t.id asc")
    List<SimSysInsMessageField> findJhSortFields(Long id);

    @Query(value = "select t.msgField.fieldId from SimSysInsMessageField t where t.simSysInsMessage.id=?1 and t.msgField.fieldId != '1013' and t.respValueType='methodvalue' and t.respValue='InvokedClass:keyElements'")
    List<String> getKeyElementsFields(Long messageId);

    @Query(value = "select t.message_field_id,t.resp_value from sim_sysins_message_field t where t.sysins_message_id=?1 and t.resp_value_type='equalvalue'",nativeQuery = true)
    List<Object[]> getEqualValueFields(Long messageId);

    @Query(value = "select t from SimSysInsMessageField t where t.simSysInsMessage.id=?1 order by t.seqNo asc, t.id asc")
    List<SimSysInsMessageField> findByMessageId(Long msgId);

    @Query(value="select s from SimSysInsMessageField s where s.msgField.id=?1")
    List<SimSysInsMessageField> findByMsgFieldId(Long id);
}
