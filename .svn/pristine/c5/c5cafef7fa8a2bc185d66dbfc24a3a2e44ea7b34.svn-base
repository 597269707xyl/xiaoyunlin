package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author lcheng
 * @version 1.0
 *          ${tags}
 */
public interface UserDao extends JpaRepository<User,Long>,JpaSpecificationExecutor<User> {

    @Query("from User t where t.id not in ?1")
    public List<User> findUserByNotIn(List<Long> ids);
    @Query("from User u where u.name like ?1")
    Page<User> findUser(String name, Pageable page);
    User findBySysUserId(Long sysUid);


}
