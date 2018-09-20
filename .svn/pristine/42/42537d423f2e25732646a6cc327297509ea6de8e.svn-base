package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.NxyMsgAcc;
import com.zdtech.platform.framework.entity.NxyUsecaseExecBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by admin-win8 on 2017/12/12.
 */
public interface NxyMsgAccDao  extends JpaRepository<NxyMsgAcc, Long>, JpaSpecificationExecutor<NxyMsgAcc> {
    @Query(value = "select t.account from NxyMsgAcc t where t.msgcode = ?1")
    String getAccountByMsgCode(String msgcode);
}
