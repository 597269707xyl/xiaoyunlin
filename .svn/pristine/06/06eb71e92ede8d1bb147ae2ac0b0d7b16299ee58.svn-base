package com.zdtech.platform.web.controller.test;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.rbac.ShiroUser;
import com.zdtech.platform.framework.repository.SimSystemInstanceDao;
import com.zdtech.platform.framework.service.UserService;
import com.zdtech.platform.service.test.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.*;

/**
 * Created by huangbo on 2016/5/9.
 */
@Controller
@RequestMapping(value = "/proj/proj")
public class TestProjectController {
    private static Logger logger = LoggerFactory.getLogger(TestProjectController.class);
    @Autowired
    private TestProjectService testProjectService;
    @Autowired
    private UserService userService;
    @Autowired
    private SimSystemInstanceDao simSystemInstanceDao;

    @RequestMapping(value = "/list")
    public String funcList() {
        return "test/project/project-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "test/project/project-add";
    }

    @RequestMapping(value = {"/new/add"}, method = RequestMethod.POST)
    @ResponseBody
    public Result addProject(TestProject project, Long instanceId) {
        Result ret = new Result();
        try {
            ret = testProjectService.addProject(project, instanceId);
            logger.info("@P|true|添加或修改项目成功");
        } catch (Exception e) {
            ret.setMsg("操作失败");
            ret.setSuccess(false);
            logger.info("@P|false|添加或修改项目失败");
        }
        return ret;
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> query(@RequestParam Map<String, Object> params, @PageableDefault Pageable page) {
        Map<String, Object> ret = new HashMap<>();
        ShiroUser shiroUser = userService.getCurrentUser();
        Long sysUserId = shiroUser.getId();
        ret = testProjectService.findTestProjects(params, page, sysUserId);
        return ret;
    }

    @RequestMapping(value = "/insname")
    @ResponseBody
    public List<SimSystemInstance> allSimSystemInstance() {
        List<SimSystemInstance> simSystemInstances = simSystemInstanceDao.findAll();
        return simSystemInstances;
    }


    @RequestMapping(value = "/assign", method = RequestMethod.GET)
    public String assign(String id, Model model) {
        model.addAttribute("projectSetId", id);
        return "test/project/project-assign";
    }


    @RequestMapping(value = "/addMembers", method = RequestMethod.POST)
    @ResponseBody
    public Result addMembers(Long projectSetId, @RequestParam(value = "id[]", required = false) Long[] ids) {
        Result ret = new Result();
        try {
            List<Long> list = new ArrayList<>();
            if (ids != null) {
                list = Arrays.asList(ids);
            } else {
                list = null;
            }
            testProjectService.addMembers(projectSetId, list);
            logger.info("@P|true|指定项目成员成功");
        } catch (Exception e) {
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@P|false|指定项目成员失败");
        }
        return ret;
    }

    @RequestMapping(value = "/getUnselectedUser/{id}", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> getUnselectedUser(@PathVariable("id") Long id) {
        Set<TestProjectUser> testProjectUsers = testProjectService.get(id).getProjectUsers();
        List<Long> selectedIds = new ArrayList<Long>();
        for (TestProjectUser testProjectUser : testProjectUsers) {
            selectedIds.add(testProjectUser.getUser().getId());
        }
        List<Map<String, String>> temp1 = new ArrayList<Map<String, String>>();
        List<User> list = userService.getUnselectedUser(selectedIds);
        if (list == null || list.size() == 0)
            return temp1;
        Map<String, String> map = null;
        for (User user : list) {
            map = new HashMap<String, String>();
            map.put("id", user.getId().toString());
            map.put("name", user.getName());
            temp1.add(map);
        }
        return temp1;
    }


    @RequestMapping(value = "/getSelectedUser/{id}", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> getSelectedUser(@PathVariable("id") Long id) {
        Set<TestProjectUser> testProjectUsers = testProjectService.get(id).getProjectUsers();
        List<User> selectedUsers = new ArrayList<User>();
        for (TestProjectUser testProjectUser : testProjectUsers) {
            selectedUsers.add(testProjectUser.getUser());
        }
        List<Map<String, String>> temp1 = new ArrayList<Map<String, String>>();
        Map<String, String> map = null;
        for (User user : selectedUsers) {
            map = new HashMap<String, String>();
            map.put("id", user.getId().toString());
            map.put("name", user.getName());
            temp1.add(map);
        }
        return temp1;
    }

    @RequestMapping(value = "/getMemberId", method = RequestMethod.POST)
    @ResponseBody
    public int getMemberId(@RequestParam(required = false) Long id) {
        return testProjectService.get(id).getProjectUsers().size();
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delTestProject(@RequestParam("id") Long id) {
        Result ret = new Result();
        try {
            testProjectService.delTestProject(id);
            logger.info("@P|true|删除项目成功");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@P|false|删除项目失败");
        }
        return ret;
    }

    @RequestMapping(value = "/deleteProjects", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteProjects(@RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        try {
            testProjectService.deleteProjects(ids);
            logger.info("@P|true|删除项目成功");
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@P|false|删除项目失败");
        }
        return ret;
    }


    @RequestMapping(value = "/allocateSimulators")
    public String allocateSimulators() {
        return "test/project/project-allocate-simulator";
    }

    @RequestMapping(value = "/getSimulators")
    public String getSimulators(Long projectId) {
        return "test/project/project-simulator-select";
    }

    @RequestMapping(value = "/get/{id}")
    @ResponseBody
    public TestProject getFunc(@PathVariable("id") Long id) {
        TestProject project = testProjectService.get(id);
        return project;
    }

    /**
     * @author huangbo
     * @create 2018/7/16
     **/
    @RequestMapping(value = "/fileList")
    public String showfileList() {
        return "test/project/project-file-list";
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadFile(Long itemId, @RequestParam(required = false) CommonsMultipartFile file) {
        Result result = new Result();
        return result;
    }

    /**
     * @author huangbo
     * @create 2018/7/26
     **/
    @RequestMapping(value = "/getStatistics")
    @ResponseBody
    public Map<String, Object> getUsecase(@RequestParam Map<String, Object> params) {
        if (null != params.get("id")&&!params.get("id").equals("")) {
            Long id = Long.valueOf(params.get("id").toString());
            String mark = params.get("mark").toString();
            String type = params.get("type").toString();
            Map<String, Object> map = testProjectService.getStatisticsOfUsecase(id, mark, type);
            return map;
        }
        return null;
    }

    @RequestMapping(value = "/findAll")
    @ResponseBody
    public List<TestProject> findAll(String type) {
        if ("config".equals(type)) {
            List<TestProject> list = testProjectService.findSendProject();
            TestProject project = new TestProject();
            project.setId(-1L);
            project.setName("公共属性");
            list.add(project);
            return list;
        }
        return testProjectService.findSendProject();
    }
}
