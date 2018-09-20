package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.MsgFieldCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by huangbo on 2017/3/14.
 */
public interface MsgFieldCodeDao extends JpaRepository<MsgFieldCode, Long>, JpaSpecificationExecutor<MsgFieldCode> {
    @Transactional
    @Modifying
    @Query("delete from MsgFieldCode c where c.msgField.id = ?1")
    void deleteByMsgFieldId(Long id);
}
