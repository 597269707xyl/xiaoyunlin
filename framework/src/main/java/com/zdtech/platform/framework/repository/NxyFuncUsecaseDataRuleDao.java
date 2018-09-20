package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.NxyFuncUsecaseDataRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * NxyFuncUsecaseDataRuleDao
 *
 * @author panli
 * @date 2017/9/10
 */
public interface NxyFuncUsecaseDataRuleDao extends JpaRepository<NxyFuncUsecaseDataRule, Long>, JpaSpecificationExecutor<NxyFuncUsecaseDataRule> {
    @Query(value = "select t from NxyFuncUsecaseDataRule t where t.usecaseDataId=?1")
    List<NxyFuncUsecaseDataRule> findByUcDataId(Long id);

    @Transactional
    @Modifying
    @Query(value = "delete from NxyFuncUsecaseDataRule t where t.id in (?1)")
    void delByIds(List<Long> ids);

    @Transactional
    @Modifying
    @Query(value = "delete from NxyFuncUsecaseDataRule t where t.usecaseDataId=?1")
    void deleteByUsecaseDataId(Long id);
}
