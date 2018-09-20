package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.TestProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by admin on 2016/5/9.
 */
public interface TestProjectDao extends JpaRepository<TestProject, Long>, JpaSpecificationExecutor<TestProject> {

    List<TestProject> findByIdNotAndName(Long id, String name);

    @Query(value = "select t from TestProject t where t.id in (?1)")
    List<TestProject> getFieldsByIds(List<Long> ids);

    @Query(value = "select k from TestProject k where  k.name=:name")
    public TestProject findByName(@Param("name") String name);

    @Query(value = "select p from TestProject p join p.projectUsers u join u.pk.user w where w.id = ?1")
    List<TestProject> findByUser(Long sysUserId);

    @Query(value = "select count(t.id) from TestProject t where t.name = ?1")
    int countOfName(String name);

    @Query(value = "select count(t.id) from TestProject t where t.abb = ?1")
    int countOfNo(String no);

    @Query(value = "select p from TestProject p where p.type='send' or p.type='send_recv'")
    List<TestProject> findSendProject();

    @Query(value = "select p from TestProject p where p.type='recv' or p.type='send_recv'")
    List<TestProject> findRecvProject();

    @Query(value = "select p from TestProject p where p.type='case_send' or p.type='case_send_recv'")
    List<TestProject> findCaseSendProject();

    @Query(value = "select p from TestProject p where p.type='case_recv' or p.type='case_send_recv'")
    List<TestProject> findCaseRecvProject();
}
