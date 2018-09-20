package com.zdtech.platform.web.controller.system;

import com.zdtech.platform.framework.entity.Func;
import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.framework.entity.SysMenuModel;
import com.zdtech.platform.framework.entity.TreeNodeModel;
import com.zdtech.platform.framework.rbac.ShiroUser;
import com.zdtech.platform.framework.service.FuncService;
import com.zdtech.platform.framework.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by leepan on 2016/4/20.
 */
@Controller
@RequestMapping(value = "/sys/func")
public class FuncController {
    @Autowired
    private FuncService funcService;
    @Autowired
    private UserService userService;

    private static Logger logger = LoggerFactory.getLogger(FuncController.class);

    @RequestMapping(value = "/list")
    public String funcList(){
        return "system/func/func-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addFunc() {
        return "system/func/func-add";
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Func getFunction(@PathVariable("id") Long id) {
        Func func = funcService.getFunc(id);
        return func;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result addFunc(Func func) {
        Result result = null;
        try {
            result = funcService.addFuncSafely(func);
            logger.info("@S|true|添加或修改功能成功！");
        } catch (Throwable t) {
            result = new Result(false, "添加系统功能失败");
            logger.error("@S|false|添加或修改功能失败！");
        }
        return result;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delFunc(Long id) {
        Result result = null;
        try {
            funcService.delFunc(id);
            result = new Result(true, "");
            logger.info("@S|true|删除功能成功！");
        } catch (Throwable t) {
            result = new Result(false, "");
            logger.error("@S|false|删除功能失败！");
        }
        return result;
    }

    @RequestMapping(value = "/userFunc")
    @ResponseBody
    List<TreeNodeModel> userMenu(Long id){
        ShiroUser shiroUser = userService.getCurrentUser();
        List<TreeNodeModel> funcs = Collections.EMPTY_LIST;
        if (shiroUser != null){
            funcs = funcService.getUserMenu(shiroUser.getId(),id,Func.Type.MenuPage);
        }
        return  funcs;
    }

    @RequestMapping(value = "/allTreeList", method = RequestMethod.GET)
    @ResponseBody
    public List<TreeNodeModel> allFuncTreeList() {
        List<TreeNodeModel> funcList = funcService.getFuncTree();
        return funcList;
    }

    @RequestMapping(value = "/treeList", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> treeList(Long id) {
        List<Func> data = new ArrayList<>();
        List<Map<String, Object>> result = new ArrayList<>(data.size());
        data = funcService.getFuncByParent(id);
        try {
            for (Func func : data) {
                Map<String, Object> map = BeanUtils.describe(func);
                map.put("state", func.isLeaf()?"open":"closed");
                map.put("isLeaf", func.isLeaf());
                result.add(map);
            }
            return result;
        } catch (Exception e) {
        }
        return result;
    }
}
