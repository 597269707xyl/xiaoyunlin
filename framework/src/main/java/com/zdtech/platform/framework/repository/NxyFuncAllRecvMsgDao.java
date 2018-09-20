package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.NxyFuncAllRecvMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by admin-win8 on 2018/1/22.
 */
public interface NxyFuncAllRecvMsgDao extends JpaRepository<NxyFuncAllRecvMsg, Long>, JpaSpecificationExecutor<NxyFuncAllRecvMsg> {
}
