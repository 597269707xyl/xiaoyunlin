package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.Backup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * Created by huangbo on 2016/10/19.
 */
public interface BackupDao extends JpaRepository<Backup,Long>,JpaSpecificationExecutor<Backup> {
}
