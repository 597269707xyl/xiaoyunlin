package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.NxyFuncUsecaseExecRecv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by yjli on 2017/9/6.
 */
public interface NxyFuncUsecaseExecRecvDao extends JpaRepository<NxyFuncUsecaseExecRecv, Long>, JpaSpecificationExecutor<NxyFuncUsecaseExecRecv> {

    @Query(value = "select t from NxyFuncUsecaseExecRecv t where t.nxyFuncUsecaseExecSend.id = ?1")
    List<NxyFuncUsecaseExecRecv> findBySendId(Long id);
}
