package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.MsgField;
import com.zdtech.platform.framework.entity.MsgFieldSet;
import com.zdtech.platform.framework.entity.MsgFieldSetComp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by leepan on 2016/5/4.
 */
public interface MsgFieldSetCompDao extends JpaRepository<MsgFieldSetComp, Long>, JpaSpecificationExecutor<MsgFieldSetComp> {
    @Query(value="select m from MsgFieldSetComp m join m.field where m.fieldSet.id=?1")
    List<MsgFieldSetComp> findByheadsetid(Long id);
    @Query(value="select m from MsgFieldSetComp m join m.field where m.fieldSet.id=?2 and m.field.id=?1")
    MsgFieldSetComp findBySIDAndFID(Long id, Long fieldSetId);
}
