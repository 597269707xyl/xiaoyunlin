package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.NxyFuncCaseBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by lyj on 2018/1/11.
 */
public interface NxyFuncCaseBankDao extends JpaRepository<NxyFuncCaseBank, Long>, JpaSpecificationExecutor<NxyFuncCaseBank> {

    @Query(value = "select count(t) from NxyFuncCaseBank t where t.bankNo=?1 and t.caseNo=?2")
    int count(String bankNo, String caseNo);
}
