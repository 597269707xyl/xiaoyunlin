package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author lcheng
 * @version 1.0
 *          ${tags}
 */
public interface RoleDao extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    List<Role> findByRole(String role);

    List<Role> findByIdNotAndRole(Long uid, String role);

    List<Role> findByIdNotAndName(Long id, String name);

    @Query(value = "select k from Role k where  k.name=:name")
    public Role findByName(@Param("name") String name);
}
