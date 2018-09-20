package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.NxyFuncCaseMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by lyj on 2018/1/9.
 */
public interface NxyFuncCaseMarkDao extends JpaRepository<NxyFuncCaseMark, Long>, JpaSpecificationExecutor<NxyFuncCaseMark> {

    @Query(value = "select count(t) from NxyFuncCaseMark t where t.msgCode=?1 and t.standard=?2")
    int countByCodeAndStandard(String msgCode, String standard);

    @Query(value = "select t.fieldId from NxyFuncCaseMark t where t.msgCode=?1 and t.standard=?2")
    String getFieldIdByCodeAndStandard(String msgCode, String standard);
}
