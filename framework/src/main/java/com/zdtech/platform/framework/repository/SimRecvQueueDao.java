package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.SimRecvQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yjli on 2017/8/18.
 */
public interface SimRecvQueueDao extends JpaRepository<SimRecvQueue, Long>, JpaSpecificationExecutor<SimRecvQueue> {
    @Query(value="select s from SimRecvQueue s where s.simSystemInstance.id=?1")
    List<SimRecvQueue> fingByInstanceId(Long instanceId);

    @Modifying
    @Transactional
    @Query(value = "delete from SimRecvQueue t where t.id in (?1)")
    int deleteByIds(List<Long> ids);

    @Query(value="select s from SimRecvQueue s group by s.queueNameRecv")
    List<SimRecvQueue> fingAllDistinct();
}
