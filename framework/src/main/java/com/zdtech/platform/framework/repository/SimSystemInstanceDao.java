package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.SimSystem;
import com.zdtech.platform.framework.entity.SimSystemInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.repository.query.Param;

/**
 * Created by htma on 2016/5/12.
 */
public interface SimSystemInstanceDao extends JpaRepository<SimSystemInstance, Long>, JpaSpecificationExecutor<SimSystemInstance> {

    @Query(value="select s from SimSystemInstance s join s.simSystem where s.simSystem.msgType=?1")
    List<SimSystemInstance> fingByType(String type);
    @Query(value = "select i.id from SimSystemInstance i where  i.name=:name")
    Long findIdByName(@Param("name") String name);
    @Query(value="select s from SimSystemInstance s where s.id=?1")
    SimSystemInstance findByid(Long simid);

    @Query(value = "select t.id,t.name from  sim_system_instance t order by convert(t.name using gbk) asc",nativeQuery = true)
    List<Object[]> findSimSystemOrder();

    @Query(value = "select t.id,t.name from  sim_system_instance t left join sim_system t1 on t.system_id = t1.id where t1.msg_type = ?1 order by convert(t.name using gbk) asc",nativeQuery = true)
    List<Object[]> findSimSystemOrder(String type);

    @Query(value = "select count(t.id) from sim_system_instance t where t.state != 1",nativeQuery = true)
    int countOfNoFinished();

    @Query(value="select distinct(s.adapter.id) from SimSystemInstance s")
    List<Long> findAdapterListDistinct();

    @Query(value="select distinct(s.adapter.id) from SimSystemInstance s where s.id <> ?1")
    List<Long> findAdapterListByInstanceIdDistinct(Long instanceId);
}
