package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.NxyFuncItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by yjli on 2017/9/6.
 */
public interface NxyFuncItemDao extends JpaRepository<NxyFuncItem, Long>, JpaSpecificationExecutor<NxyFuncItem> {

    @Query(value = "select t from NxyFuncItem t where t.testProject.id=?1 and t.parentId is null")
    List<NxyFuncItem> findByProjectId(Long id);

    @Query(value = "select t from NxyFuncItem t where t.parentId=?1")
    List<NxyFuncItem> findByParentId(Long id);

    @Query(value = "select count(t.id) from NxyFuncItem t where t.parentId=?1")
    int countByParentId(Long id);

    @Modifying
    @Query(value = "delete from NxyFuncItem t where t.id in (?1)")
    int delByItemIds(List<Long> ids);

    @Query(value = "select t from NxyFuncItem t where t.testProject.id=?1")
    List<NxyFuncItem> findItemsByProjectId(Long id);

    @Query(value = "select t from NxyFuncItem t where t.testProject.id=?1 and t.parentId is null and t.mark=?2 and t.type=?3")
    List<NxyFuncItem> findByProjectIdAndMark(Long id, String mark, String type);

    @Query(value = "select i.id from NxyFuncItem i where i.parentId=?1")
    List<Long> getIdByParentId(Long id);
}
