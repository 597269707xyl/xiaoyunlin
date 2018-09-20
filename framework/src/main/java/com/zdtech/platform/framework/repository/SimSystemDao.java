package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.MsgField;
import com.zdtech.platform.framework.entity.SimSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by leepan on 2016/5/4.
 */
public interface SimSystemDao extends JpaRepository<SimSystem, Long>, JpaSpecificationExecutor<SimSystem> {
    @Query(value = "select t from SimSystem t where t.id in (?1)")
    List<SimSystem> getFieldsByIds(List<Long> ids);

    @Query(value="select s from SimSystem s join s.headFieldSet where s.id=?1")
    SimSystem findByid(Long simid);

    @Query(value="select s from SimSystem s where s.msgType=?1")
    List<SimSystem> fingByType(String type);

    @Query(value = "select t.id,t.name,t.protocol from  sim_system t order by convert(t.name using gbk) asc",nativeQuery = true)
    List<Object[]> findSimSystemOrder();

    @Query(value = "select t.id,t.name,t.protocol from  sim_system t where t.msg_type = ?1 order by convert(t.name using gbk) asc",nativeQuery = true)
    List<Object[]> findSimSystemOrder(String type);
}
