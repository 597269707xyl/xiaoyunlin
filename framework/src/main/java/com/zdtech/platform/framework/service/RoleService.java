package com.zdtech.platform.framework.service;

import com.google.common.collect.Lists;
import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.framework.entity.Role;
import com.zdtech.platform.framework.repository.FuncDao;
import com.zdtech.platform.framework.repository.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author lcheng
 * @version 1.0
 *          ${tags}
 */
@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private FuncDao funcDao;


    public List<Role> getRoles() {
        return roleDao.findAll();
    }

    public Role getRole(Long id) {
        return roleDao.findOne(id);
    }

    public Result addRole(Role role) {
        Result result = null;
        if (role.getId() == null) {
            Role role1 = roleDao.findByName(role.getName());//查询数据库中是否存在该name字段相同的记录
            if (null != role1) {
                result = new Result(false,"该角色名已经存在!");
            } else {
                roleDao.save(role);
                result = new Result(true,"添加角色成功");
            }
        } else {
            boolean exist = roleDao.findByIdNotAndName(role.getId(), role.getName()).size() > 0 ? true : false;
            if (exist) {
                result = new Result(false,"该角色名已经存在!");;
            } else {
                Role r = roleDao.findOne(role.getId());
                r.setName(role.getName());
                r.setNotes(role.getNotes());
                r.setRole(role.getRole());

                roleDao.save(r);
                result = new Result(true,"添加角色成功");
            }
        }
        return result;
    }

    public void deleteRoles(List<Long> ids){
        for (Long id:ids){
            Role role = getRole(id);
            if (role.getUsers().size() < 1){
                deleteRole(id);
            }
        }
    }
    public void deleteRole(Long id) {
        roleDao.delete(id);
    }

    public void updateRoleFunc(Long rid, Set<Long> funcIds) {
        Role role = roleDao.findOne(rid);
        role.getFuncs().clear();
        for (Long fid : funcIds) {
            role.getFuncs().add(funcDao.getOne(fid));
        }
        roleDao.save(role);
    }
}
