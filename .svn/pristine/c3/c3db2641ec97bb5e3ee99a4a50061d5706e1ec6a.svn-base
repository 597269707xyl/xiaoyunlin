package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.NxyFuncConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by yjli on 2017/10/12.
 */
public interface NxyFuncConfigDao extends JpaRepository<NxyFuncConfig, Long>, JpaSpecificationExecutor<NxyFuncConfig> {

    @Query(value = "select t from NxyFuncConfig t where t.variableEn = ?1 and t.itemId = ?2")
    List<NxyFuncConfig> findByVariableEn(String variableEn, Long itemId);

    @Query(value = "select count(t) from NxyFuncConfig t where t.variableEn = ?1 and t.itemId = ?2")
    int findByVariableEnCount(String variableEn, Long itemId);

    @Modifying
    @Query(value = "delete from NxyFuncConfig t where t.itemId = ?1")
    int deleteByItemId(Long itemId);

    @Query(value = "select t from NxyFuncConfig t where t.itemId = ?1")
    List<NxyFuncConfig> findByItemId(Long itemId);
}
