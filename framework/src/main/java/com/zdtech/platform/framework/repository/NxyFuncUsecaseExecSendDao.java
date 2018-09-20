package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.NxyFuncUsecaseExecSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by yjli on 2017/9/6.
 */
public interface
NxyFuncUsecaseExecSendDao extends JpaRepository<NxyFuncUsecaseExecSend, Long>, JpaSpecificationExecutor<NxyFuncUsecaseExecSend> {

    @Query(value = "select t from NxyFuncUsecaseExecSend t where t.nxyFuncUsecaseExec.id=?1 and t.step=?2")
    List<NxyFuncUsecaseExecSend> findByExecIdAndStep(Long id, int step);


    @Query(value = "select t from NxyFuncUsecaseExecSend t where t.nxyFuncUsecaseExec.id=?1")
    List<NxyFuncUsecaseExecSend> findByExecId(Long id);

    @Query(value = "SELECT count(s.id) FROM nxy_func_usecase_exec_send s LEFT JOIN nxy_func_usecase_exec e ON s.usecase_exec_id = e.id WHERE e.batch_id=?1", nativeQuery = true)
    Long countByBatchId(Long id);

    @Query(value = "SELECT count(s.id) FROM nxy_func_usecase_exec_send s LEFT JOIN nxy_func_usecase_exec e ON s.usecase_exec_id=e.id WHERE e.batch_id=1 and s.result <> 'execsucc'", nativeQuery = true)
    Long countExecResultByBatchId(Long id);
}
