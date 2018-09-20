package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.NxyDefect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by huangbo on 2018/8/1.
 */
public interface NxyDefectDao extends JpaRepository<NxyDefect, Long>, JpaSpecificationExecutor<NxyDefect> {
    @Query(value = "select count(d) from NxyDefect d where d.nxyFuncItem.id=?1 and d.createTime between ?2 and ?3")
    public Long countAllDefectByItemId(Long itemId, Date createFrom, Date createTo);

    @Query(value = "select count(d) from NxyDefect d where d.testProject.id=?1 and d.createTime between ?2 and ?3")
    public Long countAllDefectByProjectId(Long projectId, Date createFrom, Date createTo);

    @Query(value = "select count(d) from NxyDefect d where d.testProject.id=?1 and d.fixStatus = true")
    public Long countAllFixedDefectByItemId(Long itemId);

    @Query(value = "select count(d) from NxyDefect d where d.testProject.id=?1 and d.fixStatus = true")
    public Long countAllFixedDefectByProjectId(Long projectId);
}