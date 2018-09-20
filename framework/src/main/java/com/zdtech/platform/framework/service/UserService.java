package com.zdtech.platform.framework.service;

import com.zdtech.platform.framework.entity.Role;
import com.zdtech.platform.framework.entity.SysUser;
import com.zdtech.platform.framework.entity.User;
import com.zdtech.platform.framework.rbac.ShiroUser;
import com.zdtech.platform.framework.repository.RoleDao;
import com.zdtech.platform.framework.repository.SysUserDao;
import com.zdtech.platform.framework.repository.UserDao;
import com.zdtech.platform.framework.utils.Digests;
import com.zdtech.platform.framework.utils.Encodes;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by leepan on 2016/4/20.
 */
@Service
public class UserService {
    private static final int SALT_SIZE = 8;

    @Autowired
    private UserDao userDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private RoleDao roleDao;

    public ShiroUser getCurrentUser() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal != null && principal instanceof ShiroUser) {
            return (ShiroUser) principal;
        }
        return null;
    }
    public SysUser getSysUser(String sysName) {
        List<SysUser> users = sysUserDao.findBySysName(sysName);
        return users.size() > 0 ? users.get(0) : null;
    }
    public  SysUser getSysUser(Long id){
        return sysUserDao.findOne(id);
    }
    public User getUserBySysUserId(Long sysUid){
        return userDao.findBySysUserId(sysUid);
    }

    public Map<String,Object> findUsers(Map<String,Object> params,Pageable page){
        return null;
    }

    public void addSysUser(SysUser sysUser, User user, String roleIds) {
        checkNotNull(sysUser);
        //=============获得角色================
        Set<Role> roles = new HashSet<>();
        if (StringUtils.isNotEmpty(roleIds)) {
            String[] ids = roleIds.split(",");
            for (String rid : ids) {
                roles.add(roleDao.getOne(Long.valueOf(rid)));
            }
        }
        //
        if (user.getId() == null) {//添加新用户
            encryptPassword(sysUser);
            sysUser.setId(null);
            sysUser.setState(SysUser.State.New);
            if (roles.size() > 0) {
                sysUser.setRoles(roles);
            }
            sysUserDao.save(sysUser);
            user.setSysUser(sysUser);
            userDao.save(user);
        } else {//更新用户
            Long userId = user.getId();
            User loadUser = userDao.findOne(userId);
            SysUser loadSysUser = loadUser.getSysUser();
            loadSysUser.setSysName(sysUser.getSysName());
            loadSysUser.setEmail(sysUser.getEmail());
            if (!loadSysUser.getPassWd().equals(sysUser.getPassWd())) {
                loadSysUser.setPassWd(sysUser.getPassWd());
                encryptPassword(loadSysUser);
            }
            loadSysUser.setRoles(roles);
            sysUserDao.save(loadSysUser);

            user.setSysUser(loadSysUser);
            userDao.save(user);
        }
    }
    private void encryptPassword(SysUser user) {
        if (StringUtils.isEmpty(user.getSalt())) {
            String hexSalt = Digests.generateHexSalt(SALT_SIZE);
            user.setSalt(hexSalt);
        }
        byte[] pwdBytes = Digests.sha1(user.getPassWd().getBytes(), user.getSalt().getBytes());
        user.setPassWd(Encodes.encodeHex(pwdBytes));
    }
    public User getUser(Long uid) {
        return userDao.findOne(uid);
    }

    public void delUsers(List<Long> idList) {
        if (idList == null || idList.size() < 1){
            return;
        }
        for (Long id:idList){
            delUser(id);
        }
    }

    private void delUser(Long id) {
        User user = userDao.findOne(id);
        if (user != null) {
            SysUser sysUser = user.getSysUser();
            userDao.delete(user);
            sysUserDao.delete(sysUser);
        }
    }

    public List<User> getUnselectedUser(List<Long> unselectedIds) {
        if (unselectedIds == null || unselectedIds.size() == 0)
            return userDao.findAll();
        else
            return userDao.findUserByNotIn(unselectedIds);
    }

        public List<User> getSelectedUser(List<Long> unselectedIds) {
            if (unselectedIds == null || unselectedIds.size() == 0)
                return userDao.findAll();
        else
            return userDao.findUserByNotIn(unselectedIds);
    }

    /*public void updateUserTasks(Long rid, Set<Long> taskIds) {
        User user = userDao.findOne(rid);
        user.getTestTasks().clear();
        for (Long tempForLoop : taskIds){
            user.getTestTasks().add(testTaskDao.findOne(tempForLoop));
        }
        userDao.save(user);
    }*/

    public boolean updatePassword(Long id,String oldPwd,String newPwd){
        SysUser sysUser = sysUserDao.findOne(id);
        if (sysUser==null)
            return false;
        String old = sysUser.getPassWd(),salt = sysUser.getSalt();
        String encrypt = encryptPassword(oldPwd, salt);
        if (!old.equals(encrypt)){
            return false;
        }
        String newEncrypt = encryptPassword(newPwd,salt);
        //sysUser.setPassWd(newEncrypt);
        sysUserDao.updatePassword(id, newEncrypt, SysUser.State.Normal);
        return true;
    }
    public String encryptPassword(String pwd,String salt){
        byte[] pwdBytes = Digests.sha1(pwd.getBytes(), salt.getBytes());
        return Encodes.encodeHex(pwdBytes);
    }

    public void resetPwd(List<Long> ids) {
        for (Long id:ids){
            SysUser user = sysUserDao.findOne(id);
//            String newEncrypt = encryptPassword("1111111a",user.getSalt());
            String newEncrypt = encryptPassword("12345678",user.getSalt());
            sysUserDao.updatePassword(id, newEncrypt, SysUser.State.New);
        }
    }
}
