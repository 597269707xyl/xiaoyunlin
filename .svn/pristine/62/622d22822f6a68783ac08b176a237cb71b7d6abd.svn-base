package com.zdtech.platform.framework.repository;

import com.zdtech.platform.framework.entity.Func;
import com.zdtech.platform.framework.entity.Role;
import com.zdtech.platform.framework.entity.SysUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lcheng
 * @version 1.0
 *          ${tags}
 */
public interface SysUserDao extends JpaRepository<SysUser, Long> {

    public List<SysUser> findBySysName(String name);


    @Query(value = "select distinct f from SysUser u join u.roles r join r.funcs f where u.id =?1 and f.parent = ?2 and f.type=?3 " +
            "order by f.level,f.seqNo")
    public List<Func> findUserRoleFunc(Long uid, Long menuId, Func.Type type);

    @Query(value = "select distinct f from SysUser u join u.roles r join r.funcs f where u.id =?1 and f.parent is null and f.type=?2 " +
            "order by f.level,f.seqNo")
    public List<Func> findUserRoleFunc(Long uid,Func.Type type);

    @Query(value = "select distinct r from SysUser u join u.roles r where u.id =?1")
    public List<Role> findRoleByUser(Long uid);

    @Transactional
    @Modifying
    @Query(value = "update SysUser u set u.passWd = ?2,u.state = ?3 where u.id =?1")
    void updatePassword(Long uid, String newPwd, SysUser.State state);


}