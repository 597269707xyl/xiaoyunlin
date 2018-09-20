package com.zdtech.platform.web.controller.system;

import com.google.common.collect.Sets;
import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author lcheng
 * @version 1.0
 *          ${tags}
 */
@Controller
@RequestMapping("/sys/role")
public class RoleController {
    private static Logger logger = LoggerFactory.getLogger(RoleController.class);

     @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String roleList() {
        return "system/role/role-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addUser() {
        return "system/role/role-add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result addUser(Role role) {
        Result ret = new Result();
        try {
            ret = roleService.addRole(role);
            logger.info("@S|true|添加或修改角色成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@S|false|添加或修改角色失败！");
        }
        return  ret;
    }

    @RequestMapping(value="/all")
    @ResponseBody
    public List<Role> allRoles(){
        List<Role> roles = roleService.getRoles();
        return roles;
    }
    @RequestMapping(value="/get/{id}")
    @ResponseBody
    public Role getRole(@PathVariable("id") Long id){
        return roleService.getRole(id);
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delRole(@RequestParam("ids[]")Long[] ids) {
        Result ret = new Result();
        try{
            List<Long> idList = new ArrayList<>();
            for (Long id:ids){
                idList.add(id);
            }
            roleService.deleteRoles(idList);
            logger.info("@S|true|删除角色成功！");
        }catch(Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@S|false|删除角色失败！");
        }
        return ret;
    }

    @RequestMapping(value = "/funAlloc", method = RequestMethod.GET)
    public String funAlloc() {
        return "system/role/role-func-alloc";
    }

    @RequestMapping(value = "/funcs", method = RequestMethod.POST)
    @ResponseBody
    public String roleFuncs(Long rid){
        Role role = roleService.getRole(rid);
        List<Func> funcs= role.getFuncs();
        StringBuilder sb = new StringBuilder();
        for (Func func : funcs){
            sb.append(func.getId().toString());
            sb.append(",");
        }
        if (sb.length()>0 && sb.charAt(sb.length()-1)==','){
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }

    @RequestMapping(value = "/funAlloc", method = RequestMethod.POST)
    @ResponseBody
    public Result addRoleFunc(Long rid, @RequestParam(value = "funcIds[]",required = false) Long[] funcIds) {
        Result result = null;
        Set<Long> func = (funcIds!=null) ? Sets.newHashSet(funcIds) : new HashSet<Long>() ;
        try {
            roleService.updateRoleFunc(rid, func);
            result = new Result(true, "操作成功");
            logger.info("@S|true|为角色指定功能成功！");
        } catch (Throwable t) {
            result = new Result(false, "操作失败");
            logger.error("@S|false|为角色指定功能失败！");
        }
        return result;
    }
}
