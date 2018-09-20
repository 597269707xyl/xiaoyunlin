package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.TestProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by huangbo on 2018/7/16.
 */
public interface TestProjectFileDao extends JpaRepository<TestProjectFile,Long>,JpaSpecificationExecutor<TestProjectFile> {
}
