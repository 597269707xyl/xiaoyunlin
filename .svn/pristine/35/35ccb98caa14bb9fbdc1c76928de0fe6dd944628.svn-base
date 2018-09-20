package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.NxyFuncEpccMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by lyj on 2018/1/9.
 */
public interface NxyFuncEpccMarkDao extends JpaRepository<NxyFuncEpccMark, Long>, JpaSpecificationExecutor<NxyFuncEpccMark> {

    @Query(value = "select count(t) from NxyFuncEpccMark t where t.msgCode=?1 and t.standard=?2")
    int countByCodeAndStandard(String msgCode, String standard);

    @Query(value = "select t.fieldId from NxyFuncEpccMark t where t.msgCode=?1 and t.standard=?2")
    String getFieldIdByCodeAndStandard(String msgCode, String standard);
}
