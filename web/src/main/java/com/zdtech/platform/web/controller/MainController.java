package com.zdtech.platform.web.controller;

import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.framework.entity.SimSysInsMessageField;
import com.zdtech.platform.framework.entity.SysUser;
import com.zdtech.platform.framework.rbac.ShiroUser;
import com.zdtech.platform.framework.repository.SimSysInsMessageFieldDao;
import com.zdtech.platform.framework.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lcheng。
 * @version 1.0
 *          ${tags}
 */
@Controller
public class MainController {
    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private SimSysInsMessageFieldDao fieldDao;

    @RequestMapping(value = {"/main","/"})
    public String main(Model model) {
        //强制用户修改密码
        ShiroUser user = userService.getCurrentUser();
        if (user == null){
            return "login";
        }
        Long sysUserId = user.getId();
        SysUser sysUser = userService.getSysUser(sysUserId);
        if (sysUser == null){
            return "login";
        }
        SysUser.State state = sysUser.getState();
        model.addAttribute("userState",state);
        model.addAttribute("sysUserId",sysUserId);
        return "main";
    }

    @RequestMapping(value = "/login")
    public String login(Model model) {
        ShiroUser user = userService.getCurrentUser();
        //将登陆操作用日志记录
        logger.info("当前用户：{}",user==null?"未登陆":user.getName());
        if (user != null){
            return main(model);
        }
        return "login";
    }

    @RequestMapping(value = "/resetPwd",method = RequestMethod.POST)
    @ResponseBody
    public Result updatePassword(Long id, String oldPwd, String newPwd){
        Result result = null;
        try{
            boolean action = userService.updatePassword(id,oldPwd,newPwd);
            if (action){
                result = new Result(true,"修改密码成功");
            }else{
                result = new Result(false,"修改密码失败");
            }
            logger.info("@S|true|更新用户密码成功,用户ID:{}",id);
        }catch (Exception e){
            logger.error("密码更新出错!",e);
            logger.error("@S|false|更新用户密码出错,用户ID:{}", id);
            result = new Result(false,"");
        }
        return result;
    }

    @RequestMapping("/ll")
    public String list(Model model){
        List<SimSysInsMessageField> list = fieldDao.findByMessageId(33L);
        List<Map<String, String>> mapList = new ArrayList<>();
        for(SimSysInsMessageField field : list){
            Map<String, String> map = new HashMap<>();
            map.put("nameZh", field.getMsgField().getNameZh());
            map.put("fieldId", field.getMsgField().getFieldId());
            map.put("value", field.getDefaultValue());
            mapList.add(map);
        }
//        model.addAttribute("list", mapList);
        model.addAttribute("list", list);
        return "funcexec/func-usecase-data-detail-form";
    }
}
