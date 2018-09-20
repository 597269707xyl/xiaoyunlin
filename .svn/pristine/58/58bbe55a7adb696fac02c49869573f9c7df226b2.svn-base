package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.NxyUsecaseExecBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by yjli on 2017/9/12.
 */
public interface NxyUsecaseExecBatchDao extends JpaRepository<NxyUsecaseExecBatch, Long>, JpaSpecificationExecutor<NxyUsecaseExecBatch> {

    @Query(value = "select count(t.id) from NxyUsecaseExecBatch t where t.itemId in (?1) and t.itemType = ?2 and t.status = 'starting'")
    int countByItemIdsAndTypeWithStarting(List<Long> ids, String type);
}
