package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.MsgFormatComp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by htma on 2016/5/9.
 */
public interface MsgFormatCompDao extends JpaRepository<MsgFormatComp, Long>, JpaSpecificationExecutor<MsgFormatComp> {
    @Query(value = "select t from MsgFormatComp t where t.msgFormat.id = ?1")
    List<MsgFormatComp> findFormatCompsById(Long id);
}
