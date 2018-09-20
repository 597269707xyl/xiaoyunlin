package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.SimMsgFieldCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * MsgFieldCodeDao
 *
 * @author panli
 * @date 2017/3/3
 */
public interface SimMsgFieldCodeDao extends JpaRepository<SimMsgFieldCode, Long>, JpaSpecificationExecutor<SimMsgFieldCode> {
    @Transactional
    @Modifying
    @Query("delete from SimMsgFieldCode c where c.msgField.id = ?1")
    void deleteBySimMessageFieldId(Long id);
}
