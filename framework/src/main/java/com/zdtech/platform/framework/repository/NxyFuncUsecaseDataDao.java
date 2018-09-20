package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.NxyFuncUsecaseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by yjli on 2017/9/6.
 */
public interface NxyFuncUsecaseDataDao extends JpaRepository<NxyFuncUsecaseData, Long>, JpaSpecificationExecutor<NxyFuncUsecaseData> {

    @Query(value = "select t from NxyFuncUsecaseData t where t.nxyFuncUsecase.id = ?1 order by t.seqNo asc ")
    List<NxyFuncUsecaseData> findByUsecaseId(Long id);

    @Query(value = "select t from NxyFuncUsecaseData t where t.nxyFuncUsecase.id = ?1 and t.seqNo = ?2")
    List<NxyFuncUsecaseData> findNextUseCaseData(Long ucId, Integer seqNo);

    @Query(value = "select count(t.id) from NxyFuncUsecaseData t where t.nxyFuncUsecase.id = ?1")
    int countByUsecaseId(Long id);

    @Modifying
    @Query(value = "delete from NxyFuncUsecaseData t where t.id in (?1)")
    int delByIds(List<Long> ids);

    @Query(value = "SELECT count(d.id) FROM nxy_func_usecase_data d LEFT JOIN nxy_func_usecase_exec e ON d.usecase_id = e.usecase_id WHERE e.batch_id=?1", nativeQuery = true)
    Long countByBatchId(Long id);

    @Query(value = "select t from NxyFuncUsecaseData t where t.simMessage.id = ?1")
    List<NxyFuncUsecaseData> findByMessageId(Long id);

    @Query(value = "select t from NxyFuncUsecaseData t where t.messageCode = ?1 and t.nxyFuncUsecase.caseNumber=?2 and t.simMessage.id=?3 order by t.nxyFuncUsecase.seqNo desc , t.nxyFuncUsecase.id desc ")
    List<NxyFuncUsecaseData> findByMessageCodeAndCaseNo(String messageCode, String caseNumber, Long simId);
}
