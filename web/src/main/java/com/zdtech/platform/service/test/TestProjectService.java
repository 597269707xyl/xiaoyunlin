package com.zdtech.platform.service.test;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.service.GenericService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by huangbo on 2016/5/9.
 */
@Service
public class TestProjectService {
    @Autowired
    private TestProjectDao testProjectDao;
    @Autowired
    private SimSystemInstanceDao simSystemInstanceDao;
    @Autowired
    private TestProjectUserDao testProjectUserDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GenericService genericService;
    @Autowired
    private NxyFuncItemDao nxyFuncItemDao;
    @Autowired
    private NxyFuncUsecaseDao nxyFuncUsecaseDao;


    public Map<String, Object> findTestProjects(Map<String, Object> params, Pageable page, Long sysUserId) {
        Pageable p = page;
        if (page != null) {
            p = new PageRequest(page.getPageNumber() < 1 ? 0 : page.getPageNumber() - 1, page.getPageSize(), page.getSort());
        }
        Map<String, Object> map = genericService.commonQuery("testProject", params, p);
        List<TestProject> tempProjects = (List<TestProject>) map.get("rows");
        List<Map<String, Object>> transRows = new ArrayList<>();
        for (TestProject tempForLoop1 : tempProjects) {
            try {
                Map<String, Object> transRow = BeanUtils.describe(tempForLoop1);
                if (tempForLoop1.getProjectUsers() != null && tempForLoop1.getProjectUsers().size() > 0) {
                    StringBuilder tempUser = new StringBuilder();
                    for (TestProjectUser tempForLoop2 : tempForLoop1.getProjectUsers()) {
                        tempUser.append(",");
                        tempUser.append(tempForLoop2.getPk().getUser().getName());
                    }
                    tempUser.deleteCharAt(0);
                    transRow.put("userName", tempUser.toString());
                }
                transRow.put("insName", tempForLoop1.getInstance() == null ? "" : tempForLoop1.getInstance().getName());
                transRows.add(transRow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        map.put("rows", transRows);
        return map;
    }

    public void addMembers(Long projectSetId, List<Long> memberList) {
        TestProject project = get(projectSetId);
        if (project != null) {
            testProjectUserDao.deleteByProjectId(projectSetId);
            List<TestProjectUser> testProjectUsers = new ArrayList<>();
            if (memberList != null) {
                for (Long tempForLoop : memberList) {
                    TestProjectUser testProjectUser = new TestProjectUser();
                    testProjectUser.setTestProject(project);
                    User temp = userDao.findOne(tempForLoop);
                    testProjectUser.setUser(temp);
                    testProjectUsers.add(testProjectUser);
                }
            }
            testProjectUserDao.save(testProjectUsers);
        }
    }

    public TestProject get(Long id) {
        return testProjectDao.findOne(id);
    }


    public void delTestProject(Long id) {
        TestProject testProject = testProjectDao.findOne(id);
        if (testProject != null) {
            testProjectDao.delete(testProject);
        }
    }

    public Result addProject(TestProject project, Long instanceId) {
        Result ret = new Result();
        if (project.getId() == null) {
            if (isExistOfProjectName(project.getName())) {
                ret.setSuccess(false);
                ret.setMsg("项目名称已存在");
                return ret;
            }
            if (isExistOfProjectNo(project.getAbb())) {
                ret.setSuccess(false);
                ret.setMsg("项目标识已存在");
                return ret;
            }
            project.setStarttime(new Date());
            SimSystemInstance instance = simSystemInstanceDao.findOne(instanceId);
            project.setInstance(instance);
            testProjectDao.save(project);
            ret.setMsg("添加成功");
            ret.setSuccess(true);
        } else {
            TestProject p = testProjectDao.getOne(project.getId());
            if (p != null) {
                String oldNo = p.getAbb();
                String oldName = p.getName();
                if (!oldName.equalsIgnoreCase(project.getName()) && isExistOfProjectName(project.getName())) {
                    ret.setSuccess(false);
                    ret.setMsg("项目名称已存在");
                    return ret;
                }
                if (!oldNo.equalsIgnoreCase(project.getAbb()) && isExistOfProjectNo(project.getAbb())) {
                    ret.setSuccess(false);
                    ret.setMsg("项目标识已存在");
                    return ret;
                }
                p.setAbb(project.getAbb());
                p.setDescript(project.getDescript());
                p.setEndtime(project.getEndtime());
                p.setName(project.getName());
                p.setTestsystem(project.getTestsystem());
                SimSystemInstance instance = simSystemInstanceDao.findOne(instanceId);
                p.setInstance(instance);
                p.setType(project.getType());
                testProjectDao.save(p);
                ret.setSuccess(true);
                ret.setMsg("操作成功");
            } else {
                ret.setSuccess(false);
                ret.setMsg("数据已不存在");
            }
        }
        return ret;
    }

    private boolean isExistOfProjectName(String name) {
        return testProjectDao.countOfName(name) > 0;
    }

    private boolean isExistOfProjectNo(String no) {
        return testProjectDao.countOfNo(no) > 0;
    }

    public void deleteProjects(Long[] ids) {
        if (ids != null && ids.length > 0) {
            for (Long id : ids) {
                delTestProject(id);
            }
        }
    }

    /**
     * @author  huangbo
     * @create  2018/7/26
     **/
    public Map<String, Object> getStatisticsOfUsecase(Long id, String mark, String type) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> transData = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();

        Long[] counts = {0L, 0L, 0L};
        if (type.equals("item")) {
            String itemName = nxyFuncItemDao.getOne(id).getName();
            itemName = getItemName(id, itemName);
            transData.put("itemName", itemName);
            transData.put("projectName", nxyFuncItemDao.getOne(id).getTestProject().getName());
            counts = getCount(id, counts, mark);
        } else {
            counts[0] += nxyFuncUsecaseDao.countAllUsecaseByProjectIdAndMark(id, mark);
            counts[1] += nxyFuncUsecaseDao.countSucUsecaseByProjectIdAndMark(id, mark);
            counts[2] += nxyFuncUsecaseDao.countSucUsecaseByProjectIdAndMark(id, mark);
            transData.put("projectName", testProjectDao.getOne(id).getName());
        }
        transData.put("total", counts[0]);
        transData.put("suc", counts[1]);
        transData.put("fail", counts[2]);
        transData.put("other", counts[0] - counts[1] - counts[2]);
        list.add(transData);
        map.put("rows", list);
        map.put("total", 1);
        return transData;
    }

    private Long[] getCount(Long id, Long[] counts, String mark) {
        List<Long> childrenId = nxyFuncItemDao.getIdByParentId(id);
        if (childrenId.size() == 0) {
            counts[0] += nxyFuncUsecaseDao.countAllUsecaseByItemIdAndMark(id, mark);
            counts[1] += nxyFuncUsecaseDao.countSucUsecaseByItemIdAndMark(id, mark);
            counts[2] += nxyFuncUsecaseDao.countFailUsecaseByItemIdAndMark(id, mark);
        } else {
            for (Long temp : childrenId) {
                counts = getCount(temp, counts, mark);
            }
        }
        return counts;
    }

    private String getItemName(Long id, String itemName) {
        Long parentId = nxyFuncItemDao.getOne(id).getParentId();
        if (parentId != null) {
            itemName = nxyFuncItemDao.getOne(parentId).getName() + "-" + itemName;
            itemName = getItemName(parentId, itemName);
        }
        return itemName;
    }

    public List<TestProject> findSendProject(){
        return testProjectDao.findSendProject();
    }
}
