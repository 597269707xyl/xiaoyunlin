package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.MsgField;
import com.zdtech.platform.framework.entity.SimSysInsMessage;
import com.zdtech.platform.framework.entity.SimSystemInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

/**
 * SimSysInsMessageDao
 *
 * @author panli
 * @date  2016/5/17.
 */
public interface SimSysInsMessageDao extends JpaRepository<SimSysInsMessage, Long>, JpaSpecificationExecutor<SimSysInsMessage> {
    @Query(value="select s from SimSysInsMessage s where s.name=?1")
    SimSysInsMessage findByName(String name);
    @Query(value="select s from SimSysInsMessage s where s.name=?1 and( s.trsCode=?2 or s.msgType=?3) and s.systemInstance.id=?4")
    SimSysInsMessage findByNameAndTrs(String name, String trsCode,String msgType,Long simid);
    @Query(value="select s from SimSysInsMessage s where s.msgType=?1")
    SimSysInsMessage findByMsgType(String msgType);
    @Query(value="select s from SimSysInsMessage s where s.type=?1")
    List<SimSysInsMessage> findByType(String type);
    @Query(value="select s from SimSysInsMessage s where s.systemInstance.id=?1 and s.trsCode = ?2 and s.msgTypeCode = ?3")
    List<SimSysInsMessage> getMsgByTrsAndMsgTypeCode(Long simInsId, String trsCode, String msgTypeCode);
    @Query(value = "select t.systemInstance from SimSysInsMessage t where t.id = ?1")
    SimSystemInstance findSimInsByMessageId(Long id);
    @Query(value = "select t2.rep_msg_id from sim_sysins_message t1,sim_sysins_reply_rule t2 where t1.id = t2.req_msg_id and t1.id = ?1",nativeQuery = true)
    List<BigInteger> findReplyMsgId(Long reqId);
   /* @Query(value = "select s.trsCode from sim_sysins_message s where s.msgType = ?1")
    String findIdByMsgType(String msgType);*/
    @Query(value = "select t from SimSysInsMessage t where t.systemInstance.id = ?1 and t.msgType = ?2")
    List<SimSysInsMessage> findMessageByMsgType(Long simInsId,String msgType);

    @Query(value = "select t from SimSysInsMessage t where t.msgType = ?1")
    SimSysInsMessage findEcdsMsgIdByType(String msgType);

    @Query(value="select s from SimSysInsMessage s where s.msgType=?1 and s.systemInstance.id=?2")
    SimSysInsMessage findByMsgTypeAndSimId(String msgType, Long simid);

    @Query(value = "select t from SimSysInsMessage t where t.systemInstance.id = ?1")
    List<SimSysInsMessage> findMsgsBySystemId(Long systemId);

    @Query(value = "select t.msgField from SimSysInsMessageField t where t.simSysInsMessage.id = ?1 order by t.seqNo asc ")
    List<MsgField> findMsgFields(Long msgId);

    @Query(value = "select t.msgField from SimSysInsMessageField t where t.simSysInsMessage.systemInstance.id = ?1 and t.simSysInsMessage.trsCode = ?2 order by t.seqNo asc")
    List<MsgField> findMesageFieldsBySimIdAndTrscode(Long simInsId, String tTxnCd);

    @Query(value = "select t from SimSysInsMessage t where t.systemInstance.id = ?1 and t.trsCode = ?2")
    List<SimSysInsMessage> getMsgByTrsCode(Long simInsId, String trsCode);

    @Query(value = "select t from  SimSysInsMessage t where  t.type=?1 and t.msgType=?2")
    List<SimSysInsMessage> findByTypeAndMsgType(String type, String msgType);

    @Query(value = "select t.msgField from SimSysInsMessageField t where t.simSysInsMessage.id = ?1 and t.fieldType='HEAD' order by t.seqNo asc ")
    List<MsgField> findMsgHeadFields(Long msgId);

    @Query(value = "select t from SimSysInsMessage t where t.msgType = ?1")
    List<SimSysInsMessage> findMessageByMsgType(String msgType);

    @Query(value = "select t from  SimSysInsMessage t where  t.type=?1 and t.msgType=?2 and t.systemInstance.id=?3")
    List<SimSysInsMessage> findByTypeAndMsgTypeAndSysInsId(String type, String msgType, Long sysInsId);
}
