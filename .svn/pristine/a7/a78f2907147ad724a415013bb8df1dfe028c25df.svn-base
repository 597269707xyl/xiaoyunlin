package com.zdtech.platform.web.controller.test;

import com.zdtech.platform.framework.entity.NxyFuncItem;
import com.zdtech.platform.framework.entity.TestProject;
import com.zdtech.platform.framework.entity.User;
import com.zdtech.platform.framework.repository.NxyFuncItemDao;
import com.zdtech.platform.framework.repository.TestProjectDao;
import com.zdtech.platform.framework.repository.UserDao;
import com.zdtech.platform.service.funcexec.FuncExecService;
import com.zdtech.platform.utils.WordUtils;
import com.zdtech.platform.web.controller.funcexec.TreeNode;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lyj on 2018/7/17.
 */
@Controller
@RequestMapping(value = "/test/task")
public class TestTaskController {

    @Autowired
    private TestProjectDao testProjectDao;
    @Autowired
    private NxyFuncItemDao nxyFuncItemDao;
    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/list")
    public String funcList() {
        return "test/task/task-list";
    }


    @RequestMapping(value = "/assign")
    public String assign() {
        return "test/task/task-assign";
    }

    @RequestMapping(value = "/getAssignMember")
    @ResponseBody
    public List<Map<String, Object>> getAssignMember(){
        List<Map<String, Object>> list = new ArrayList<>();
        List<User> userList = userDao.findAll();
        for(User user : userList){
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("name", user.getName());
            list.add(map);
        }
        return list;
    }

    //功能测试项树形列表数据
    @RequestMapping(value = "/tree")
    @ResponseBody
    public List<TreeNode> tree(@RequestParam("id") Long id, String type, String mark, String caseType) {
        if (id.intValue() == -1) {
            List<TestProject> testProjects = testProjectDao.findAll();
            return projectToTreeNodes(testProjects);
        } else if ("project".equals(type)) {
            List<NxyFuncItem> items = nxyFuncItemDao.findByProjectIdAndMark(id,mark, caseType);
            List<User> users = userDao.findAll();
            Random random = new Random();
            for(NxyFuncItem item : items){
                int i = random.nextInt(users.size());
                item.setName(item.getName()+"-" + users.get(i).getName());
            }
            return itemToTreeNodes(items, "item_one");
        } else if ("item".equals(type) || "item_one".equals(type)) {
            List<NxyFuncItem> items = nxyFuncItemDao.findByParentId(id);
            return itemToTreeNodes(items, null);
        }
        return new ArrayList<>();
    }

    private List<TreeNode> projectToTreeNodes(List<TestProject> testProjects) {
        List<TreeNode> nodes = new ArrayList<>();
        if (testProjects != null) {
            for (TestProject testProject : testProjects) {
                TreeNode node = new TreeNode(testProject.getId(), testProject.getName(), "closed", "project");
                nodes.add(node);
            }
        }
        return nodes;
    }

    private List<TreeNode> itemToTreeNodes(List<NxyFuncItem> items, String type) {
        List<TreeNode> nodes = new ArrayList<>();
        if (items != null) {
            for (NxyFuncItem item : items) {
                String state = "closed";
                int count = nxyFuncItemDao.countByParentId(item.getId());
                if(count == 0) state = "open";
                TreeNode node = new TreeNode(item.getId(), item.getName(), state, type==null?"item":type);
                nodes.add(node);
            }
        }
        return nodes;
    }

    @RequestMapping("/report")
    public void report(Long batchId, HttpServletResponse response) {
        try {
            Date now = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dates = sf.format(now);
            String wordFilePath = this.getClass().getResource("/ywcsbg.docx").getPath();
            XWPFDocument doc = WordUtils.generateWord(null, wordFilePath);
            response.reset();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("UTF-8");
            String fileDisplay = new String(("测试报告" + dates + ".docx").getBytes("GB2312"), "iso8859-1");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileDisplay);
            // 输出资源内容到相应对象
            OutputStream outs = response.getOutputStream();
            doc.write(outs);
            IOUtils.closeQuietly(outs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
