package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.SysAdapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by huangbo on 2018/7/12.
 */
public interface SysAdapterDao extends JpaRepository<SysAdapter,Long>,JpaSpecificationExecutor<SysAdapter> {

    @Query(value = "select t from SysAdapter t where t.adapterType=?1 order by t.id desc")
    List<SysAdapter> getListByType(String type);
}
