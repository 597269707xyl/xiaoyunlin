package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.TestProjectUser;
import com.zdtech.platform.framework.entity.TestProjectUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by leepan on 2016/5/10.
 */
public interface TestProjectUserDao extends JpaRepository<TestProjectUser, TestProjectUserId>, JpaSpecificationExecutor<TestProjectUser> {
    @Transactional
    @Modifying
    @Query("delete from TestProjectUser t where t.pk.project.id = ?1")
    void deleteByProjectId(Long projectSetId);


/*    @Query("select t.pk.user.id from TestProjectuser t where t.pk.project = ?1")
    public List<Long> findByProjectId (Long id);*/
}
