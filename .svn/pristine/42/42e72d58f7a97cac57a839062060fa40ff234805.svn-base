package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.MsgField;
import com.zdtech.platform.framework.entity.MsgFieldSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by leepan on 2016/5/4.
 */
public interface MsgFieldSetDao extends JpaRepository<MsgFieldSet, Long>, JpaSpecificationExecutor<MsgFieldSet> {
    @Query(value = "select t from MsgFieldSet t where t.setType = ?1")
    List<MsgFieldSet> findBySetType(String setType);
}
