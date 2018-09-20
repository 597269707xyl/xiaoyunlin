package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.MsgDataType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by leepan on 2016/5/4.
 */
public interface MsgDataTypeDao extends JpaRepository<MsgDataType, Long>, JpaSpecificationExecutor<MsgDataType> {
    @Query(value="select m from MsgDataType m where m.name=?1 or m.code=?2")
    MsgDataType findByNameOrCode(String name, String code);
    @Query(value="select m from MsgDataType m where m.name=?1 and m.serial=?2")
    MsgDataType findByNameandStand(String name, String serial);
}
