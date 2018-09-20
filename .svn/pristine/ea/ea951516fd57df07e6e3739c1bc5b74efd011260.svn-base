package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.MsgDataTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by leepan on 2016/5/4.
 */
public interface MsgDataTypeEnumDao extends JpaRepository<MsgDataTypeEnum, Long>, JpaSpecificationExecutor<MsgDataTypeEnum> {
    @Query(value="select s from MsgDataTypeEnum s where s.dataType.id=?1")
    List<MsgDataTypeEnum> findByDataId(Long id);
}
