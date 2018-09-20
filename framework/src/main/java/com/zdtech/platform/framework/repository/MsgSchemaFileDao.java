package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.MsgSchemaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by leepan on 2016/5/4.
 */
public interface MsgSchemaFileDao extends JpaRepository<MsgSchemaFile, Long>, JpaSpecificationExecutor<MsgSchemaFile> {
}
