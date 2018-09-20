package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.SysAdapterQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by huangbo on 2018/7/17.
 */
public interface SysAdapterQueueDao extends JpaRepository<SysAdapterQueue,Long>,JpaSpecificationExecutor<SysAdapterQueue> {
    @Query(value = "select q from SysAdapterQueue q where q.adapterId = ?1 and q.type = ?2")
    List<SysAdapterQueue> findByAdapterAndType(Long id, String send);

    @Modifying
    @Transactional
    @Query(value = "delete from SysAdapterQueue q where q.adapterId =?1")
    void deleteAllByAdapterId(Long id);

    @Query(value = "select q from SysAdapterQueue q where q.adapterId = ?1")
    List<SysAdapterQueue> findByAdapter(Long id);
}
