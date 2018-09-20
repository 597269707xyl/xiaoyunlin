package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.NxyFuncCaseSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by lyj on 2018/1/11.
 */
public interface NxyFuncCaseSendDao extends JpaRepository<NxyFuncCaseSend, Long>, JpaSpecificationExecutor<NxyFuncCaseSend> {

    @Query(value = "select t from NxyFuncCaseSend t where t.caseRecv.id = ?1")
    NxyFuncCaseSend findByRecvId(Long id);
}
