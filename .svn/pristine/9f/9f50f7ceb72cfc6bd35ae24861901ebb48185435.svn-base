package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.NxyFuncUsecaseExec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by yjli on 2017/9/6.
 */
public interface NxyFuncUsecaseExecDao extends JpaRepository<NxyFuncUsecaseExec, Long>, JpaSpecificationExecutor<NxyFuncUsecaseExec> {

    @Query(value = "select count(t.id) from NxyFuncUsecaseExec t where t.nxyFuncUsecase.id = ?1")
    int countByUsecaseId(Long id);


    @Query(value = "select t from NxyFuncUsecaseExec t where t.nxyFuncUsecase.id = ?1 order by t.round desc ")
    List<NxyFuncUsecaseExec> findByUsecaseId(Long id);

    @Query(value = "select count(t.id) from NxyFuncUsecaseExec t where t.batchId = ?1 and t.result = 'expected'")
    int succCountByBatchId(Long batchId);

    @Query(value = "select t from NxyFuncUsecaseExec t where t.batchId = ?1 and t.result = 'expected'")
    List<NxyFuncUsecaseExec> findByBatchIdAndExpected(Long batchId);

    @Transactional
    @Modifying
    @Query(value = "delete from NxyFuncUsecaseExec t where t.batchId = ?1")
    int deleteByBatchId(Long id);

    @Transactional
    @Modifying
    @Query(value = "delete from NxyFuncUsecaseExec t where t.nxyFuncUsecase.id = ?1")
    int deleteByUsecaseId(Long id);
}
