package com.zdtech.platform.framework.rbac;

import com.zdtech.platform.framework.entity.SysUser;
import com.zdtech.platform.framework.entity.User;
import com.zdtech.platform.framework.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author lcheng
 * @version 1.0
 *          ${tags}
 */
public class ShiroDbRealm extends AuthorizingRealm {

    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        /*ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        Long uid = shiroUser.getId();
        List<String> roles = userService.getUserRoleStrs(uid);
        List<String> perms = userService.getUserPermStrs(uid);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(roles);
        info.addStringPermissions(perms);
        return info;*/
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String sysName = upToken.getUsername();
        SysUser sysUser = userService.getSysUser(sysName);
        if (sysUser == null){
            throw new CredentialsException();
        }
        String loginPwd = new String(upToken.getPassword());
        String realPwd = sysUser.getPassWd(),salt = sysUser.getSalt();
        String encrypt = userService.encryptPassword(loginPwd, salt);
        if (!realPwd.equals(encrypt)){
            throw new CredentialsException();
        }
        User user = userService.getUserBySysUserId(sysUser.getId());
        if (sysUser != null) {
            ShiroUser shiroUser = new ShiroUser(sysUser.getId(),user.getId(),sysUser.getSysName(),user.getName());
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(shiroUser, sysUser.getPassWd(),
                    ByteSource.Util.bytes(sysUser.getSalt()), getName());
            return info;
        }
        return null;
    }

    @PostConstruct
    public void initCredentialsMatcher() {
        CredentialsMatcher matcher = new InternalCredentialMatcher();
        setCredentialsMatcher(matcher);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
