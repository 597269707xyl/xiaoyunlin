package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.MsgField;
import com.zdtech.platform.framework.entity.SimMessage;
import com.zdtech.platform.framework.entity.SimSysInsMessage;
import org.hibernate.annotations.SortComparator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * SimMessageDao
 *
 * @author xiaolanli
 * @date  2016/5/9.
 */
public interface SimMessageDao extends JpaRepository<SimMessage, Long>, JpaSpecificationExecutor<SimMessage> {
    @Query(value="select s from SimMessage s join s.simSystem where s.id=?1")
     SimMessage findByid(Long id);

    @Query(value="select s from SimMessage s where s.name=?1")
    SimMessage findByName(String name);

    @Query(value="select s from SimMessage s where s.type=?1")
    List<SimMessage> findByType(String type);

    @Query(value="select s from SimMessage s where s.mesgType=?1")
    SimMessage findByMesgType(String str);

    @Query(value="select s from SimMessage s where s.id =?1 And s.name=?2")
    List<SimMessage> findByIdNotAndName(Long id, String name);
    @Query(value="select s from SimMessage s join s.simSystem where s.simSystem.id=?1")
    List<SimMessage> findBySystemid(Long id);
    @Query(value="select s from SimMessage s where s.name=?1 and s.trsCode=?2 and s.simSystem.id=?3")
    SimMessage findByNameAndTrs(String name, String trsCode, Long simid);
    @Query(value = "select s.id from SimMessage s where s.code =?1 and s.modelFileContent = ?2")
    Long findIdByCodeAndContent(String code,String content);
    @Query(value="select s from SimMessage s where s.mesgType=?1 and s.simSystem.id=?2")
    SimMessage findByMesgTypeAndSimId(String mesgType, Long simid);

    @Query(value = "select t from SimMessage t where t.simSystem.id = ?1")
    List<SimMessage> findMsgsBySystemId(Long systemId);
    @Query(value="select s from SimMessage s where s.trsCode = ?1 and s.msgTypeCode = ?2")
    List<SimMessage> getMsgByTrsAndMsgTypeCode(String trsCode, String msgTypeCode);
}
