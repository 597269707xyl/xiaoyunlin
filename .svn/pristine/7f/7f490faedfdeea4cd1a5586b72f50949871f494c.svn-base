package com.zdtech.platform.service.defect;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.persistence.DynamicSpecifications;
import com.zdtech.platform.framework.persistence.SearchFilter;
import com.zdtech.platform.framework.rbac.ShiroUser;
import com.zdtech.platform.framework.repository.NxyDefectDao;
import com.zdtech.platform.framework.repository.NxyFuncItemDao;
import com.zdtech.platform.framework.repository.NxyFuncUsecaseDao;
import com.zdtech.platform.framework.repository.TestProjectDao;
import com.zdtech.platform.framework.service.GenericService;
import com.zdtech.platform.framework.service.UserService;
import com.zdtech.platform.utils.ExcelUtils;
import com.zdtech.platform.web.controller.funcexec.TreeNode;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huangbo on 2018/8/1.
 */
@Service
public class DefectService {
    @Autowired
    private NxyDefectDao nxyDefectDao;
    @Autowired
    private NxyFuncUsecaseDao nxyFuncUsecaseDao;
    @Autowired
    private UserService userService;
    @Autowired
    private TestProjectDao testProjectDao;
    @Autowired
    private NxyFuncItemDao nxyFuncItemDao;
    @Autowired
    private GenericService genericService;

    public void addImpDefect(NxyDefect nxyDefect, Long usecaseId) {
        NxyFuncUsecase nxyFuncUsecase = nxyFuncUsecaseDao.findOne(usecaseId);
        NxyFuncItem nxyFuncItem = nxyFuncUsecase.getNxyFuncItem();
        TestProject testProject = nxyFuncItem.getTestProject();
        nxyDefect.setNxyFuncUsecase(nxyFuncUsecase);
        nxyDefect.setNxyFuncItem(nxyFuncItem);
        nxyDefect.setTestProject(testProject);
        ShiroUser shiroUser = userService.getCurrentUser();
        nxyDefect.setCreateUser(shiroUser.getName());
        nxyDefect.setMark(nxyFuncItem.getMark());
        nxyDefect.setCreateTime(new Date());
        nxyDefect.setType("IMP");
        nxyDefect.setFixStatus(false);
        nxyDefectDao.save(nxyDefect);
    }

    public void addUdDefect(NxyDefect nxyDefect, Long testItem, String type) {
        if (type.equals("project")) {
            nxyDefect.setTestProject(testProjectDao.findOne(testItem));
        } else if (type.equals("item")) {
            nxyDefect.setNxyFuncItem(nxyFuncItemDao.findOne(testItem));
            nxyDefect.setTestProject(nxyFuncItemDao.findOne(testItem).getTestProject());
        }
        if (nxyDefect.getId() == null) {
            nxyDefect.setCreateUser(userService.getCurrentUser().getName());
            nxyDefect.setCreateTime(new Date());
            nxyDefect.setType("UD");
            if (nxyDefect.getFixStatus()) {
                nxyDefect.setFixUser(userService.getCurrentUser().getName());
            }
            nxyDefectDao.save(nxyDefect);
        } else {
            NxyDefect originalDefect = nxyDefectDao.findOne(nxyDefect.getId());
            originalDefect.setTestProject(nxyDefect.getTestProject());
            originalDefect.setAbbreviation(nxyDefect.getAbbreviation());
            originalDefect.setFixStatus(nxyDefect.getFixStatus());
            originalDefect.setGrade(nxyDefect.getGrade());
            if (originalDefect.getType().equals("UD")) {
                originalDefect.setMark(nxyDefect.getMark());
            }
            if (null != nxyDefect.getNxyFuncItem()) {
                originalDefect.setNxyFuncItem(nxyDefect.getNxyFuncItem());
            }
            if (null != nxyDefect.getDescript()) {
                originalDefect.setDescript(nxyDefect.getDescript());
            }
            if (nxyDefect.getFixStatus()) {
                originalDefect.setFixTime(nxyDefect.getFixTime());
                if (!originalDefect.getFixStatus()) {
                    originalDefect.setFixUser(userService.getCurrentUser().getName());
                }
            } else {
                originalDefect.setFixTime(null);
                originalDefect.setFixUser(null);
            }
            nxyDefectDao.save(originalDefect);
        }


    }

    public Map<String, Object> getStatistics(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> transData = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Long id = Long.valueOf(params.get("id").toString());
        String type = params.get("type").toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<SearchFilter> filters = new ArrayList<>();
        if (params.get("createFrom") != null) {
            try {
                Date createFrom = format.parse(params.get("createFrom").toString());
                filters.add(new SearchFilter("createTime", SearchFilter.Operator.GTE, createFrom));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (params.get("createTo") != null) {
            try {
                Date createTo = format.parse(params.get("createTo").toString());
                filters.add(new SearchFilter("createTime", SearchFilter.Operator.LTE, createTo));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Long[] counts = {0L, 0L, 0L, 0L, 0L};
        if (type.equals("item")) {
            transData.put("projectName", nxyFuncItemDao.findOne(id).getTestProject().getName());
            String itemName = nxyFuncItemDao.getOne(id).getName();
            itemName = getItemName(id, itemName);
            transData.put("itemName", itemName);

            counts = getCount(id, filters, counts);
            filters.clear();
            Long projectId = nxyFuncItemDao.findOne(id).getTestProject().getId();
            filters.add(new SearchFilter("testProject.id", SearchFilter.Operator.EQ, projectId));
            Specification specification = DynamicSpecifications.bySearchFilter(filters, DynamicSpecifications.ConnType.And);
        } else if (type.equals("project")) {
            filters.add(new SearchFilter("testProject.id", SearchFilter.Operator.EQ, id));
            transData.put("projectName", testProjectDao.findOne(id).getName());
            Specification specification = DynamicSpecifications.bySearchFilter(filters, DynamicSpecifications.ConnType.And);
            counts[0] += nxyDefectDao.count(specification);
            filters.add(new SearchFilter("fixStatus", SearchFilter.Operator.EQ, true));
            specification = DynamicSpecifications.bySearchFilter(filters, DynamicSpecifications.ConnType.And);
            counts[1] += nxyDefectDao.count(specification);
            counts[2] = count(counts[2], filters, "H");
            counts[3] = count(counts[3], filters, "M");
            counts[4] = count(counts[4], filters, "L");
        }
        Long total = nxyDefectDao.count();
        if (total != 0) {
            double partPercent = new BigDecimal((float) counts[0] / total).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            transData.put("partPercent", partPercent * 100);
        }
        if (counts[0] != 0) {
            double fixPercent = new BigDecimal((float) counts[1] / counts[0]).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            transData.put("fixPercent", fixPercent * 100);
        }
        transData.put("total",total);
        transData.put("other", total-counts[0]);
        transData.put("hLevel", counts[2]);
        transData.put("mLevel", counts[3]);
        transData.put("lLevel", counts[4]);
        transData.put("fixed", counts[1]);
        transData.put("unFixed", counts[0] - counts[1]);
        transData.put("sum", counts[0]);
        list.add(transData);
        map.put("rows", list);
        map.put("total", 1);
        return transData;
    }

    public Map<String, Object> getDefectMatched(Map<String, Object> params, Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        if (params.get("testProject.id") != null) {
            map = genericService.commonQuery("nxyDefect", params, pageable);
        } else if (null != params.get("nxyFuncItem.id")) {
            map = getDefectMatchedByItem(params, pageable);
        }
        return map;
    }

    public Map<String, Object> getDefect(Map<String, Object> params, Pageable pageable) {
        Map<String, Object> map;
        if (null != params.get("nxyFuncItem.id")) {
            map = getDefectMatchedByItem(params, pageable);
        } else {
            map = genericService.commonQuery("nxyDefect", params, pageable);
        }
        return map;
    }

    public void exportExcel(Map<String, Object> params, HttpServletResponse httpServletResponse) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = simpleDateFormat.format(new Date());
        String[] excelHeader = {"缺陷标识", "用例标识", "项目名称", "测试项名称", "来往账状态", "修复状态", "缺陷等级", "缺陷类型", "创建人", "创建时间", "修复人", "修复时间"};
        List<NxyDefectExcel> excelList = new ArrayList<>();
        Map<String, Object> map = getDefect(params, null);
        @SuppressWarnings("unchecked")
        List<NxyDefect> nxyDefectList = (List<NxyDefect>) map.get("rows");
        for (NxyDefect nxyDefect : nxyDefectList) {
            NxyDefectExcel nxyDefectExcel = new NxyDefectExcel();
            nxyDefectExcel.setAbbreviation((nxyDefect.getAbbreviation() != null) ? nxyDefect.getAbbreviation() : "");
            nxyDefectExcel.setUsecaseNum((nxyDefect.getNxyFuncUsecase() != null) ? nxyDefect.getNxyFuncUsecase().getNo() : "");
            nxyDefectExcel.setProjectName((nxyDefect.getTestProject() != null) ? nxyDefect.getTestProject().getName() : "");
            nxyDefectExcel.setItemName((nxyDefect.getNxyFuncItem() != null) ? nxyDefect.getNxyFuncItem().getName() : "");
            nxyDefectExcel.setMark((nxyDefect.getMark() == null) ? "" : (nxyDefect.getMark().equals("send") ? "往账" : (nxyDefect.getMark().equals("recv") ? "来账" : "")));
            nxyDefectExcel.setFixStatus(nxyDefect.getFixStatus() ? "已修复" : "未修复");
            nxyDefectExcel.setGrade(nxyDefect.getGrade().equals("H") ? "高" : (nxyDefect.getGrade().equals("M") ? "中" : (nxyDefect.getGrade().equals("L") ? "低" : "")));
            nxyDefectExcel.setType(nxyDefect.getType().equals("UD") ? "自定义" : (nxyDefect.getType().equals("IMP") ? "系统导入" : ""));
            nxyDefectExcel.setCreateUser(nxyDefect.getCreateUser());
            nxyDefectExcel.setCreateTime(nxyDefect.getCreateTime().toString());
            if (nxyDefect.getFixStatus()) {
                nxyDefectExcel.setFixUser(nxyDefect.getFixUser());
                nxyDefectExcel.setFixTime(nxyDefect.getFixTime().toString());
            }
            excelList.add(nxyDefectExcel);
        }
        httpServletResponse.reset();
        httpServletResponse.setContentType("application/x-download");
        httpServletResponse.setCharacterEncoding("UTF-8");
        try {
            String fileDisplay = new String(("缺陷列表导出" + dateString + ".xls").getBytes("GB2312"), "iso8859-1");
            httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" + fileDisplay);
            ExcelUtils excelUtils = new ExcelUtils();
            excelUtils.exportExcel("缺陷列表", excelHeader, excelList, httpServletResponse.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> getDefectMatchedByItem(Map<String, Object> params, Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<SearchFilter> filters = new ArrayList<>();
        Long itemId = Long.valueOf(params.get("nxyFuncItem.id").toString());
        if (params.get("createFrom") != null) {
            try {
                Date createFrom = format.parse(params.get("createFrom").toString());
                filters.add(new SearchFilter("createTime", SearchFilter.Operator.GTE, createFrom));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (params.get("createTo") != null) {
            try {
                Date createTo = format.parse(params.get("createTo").toString());
                filters.add(new SearchFilter("createTime", SearchFilter.Operator.LTE, createTo));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<SearchFilter> filters1 = new ArrayList<>();
        getItemFilter(filters1, itemId);
        Specification specification = DynamicSpecifications.bySearchFilter1(filters, filters1, DynamicSpecifications.ConnType.And);
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        List<NxyDefect> nxyDefectList;
        if (null != pageable) {
            Page page = nxyDefectDao.findAll(specification, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort));
            nxyDefectList = page.getContent();
        } else {
            nxyDefectList = nxyDefectDao.findAll(specification);
        }
        List<Map<String, Object>> newDataList = new ArrayList<>();
        for (NxyDefect nxyDefect : nxyDefectList) {
            try {
                Long currentId = nxyDefect.getNxyFuncItem().getId();
                String currentName = nxyDefect.getNxyFuncItem().getName();
                Map<String, Object> newData = BeanUtils.describe(nxyDefect);
                newData.put("itemName", getItemName(currentId, currentName));
                newDataList.add(newData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        map.put("rows", nxyDefectList);
        map.put("total", nxyDefectList.size());
        return map;
    }

    private void getItemFilter(List<SearchFilter> filters, Long itemId) {
        filters.add(new SearchFilter("nxyFuncItem.id", SearchFilter.Operator.EQ, itemId));
        List<Long> childrenId = nxyFuncItemDao.getIdByParentId(itemId);
        if (childrenId.size() != 0) {
            for (Long id : childrenId) {
                getItemFilter(filters, id);
            }
        }
    }

    private Long[] getCount(Long id, List<SearchFilter> filters, Long[] counts) {
        filters.add(new SearchFilter("nxyFuncItem.id", SearchFilter.Operator.EQ, id));
        Specification specification = DynamicSpecifications.bySearchFilter(filters, DynamicSpecifications.ConnType.And);
        counts[0] += nxyDefectDao.count(specification);
        filters.add(new SearchFilter("fixStatus", SearchFilter.Operator.EQ, true));
        specification = DynamicSpecifications.bySearchFilter(filters, DynamicSpecifications.ConnType.And);
        counts[1] += nxyDefectDao.count(specification);
        counts[2] = count(counts[2], filters, "H");
        counts[3] = count(counts[3], filters, "M");
        counts[4] = count(counts[4], filters, "L");
        List<Long> childrenId = nxyFuncItemDao.getIdByParentId(id);
        if (childrenId.size() != 0) {
            for (Long temp : childrenId) {
                filters.remove(filters.size() - 1);
                filters.remove(filters.size() - 1);
                counts = getCount(temp, filters, counts);
            }
        }
        return counts;
    }

    private Long count(Long count, List<SearchFilter> filters, String level) {
        filters.remove(filters.size() - 1);
        filters.add(new SearchFilter("grade", SearchFilter.Operator.EQ, level));
        Specification specification = DynamicSpecifications.bySearchFilter(filters, DynamicSpecifications.ConnType.And);
        count += nxyDefectDao.count(specification);
        return count;
    }

    private String getItemName(Long id, String itemName) {
        Long parentId = nxyFuncItemDao.getOne(id).getParentId();
        if (parentId != null) {
            itemName = nxyFuncItemDao.getOne(parentId).getName() + "-" + itemName;
            itemName = getItemName(parentId, itemName);
        }
        return itemName;
    }

    public Result setFixed(Long[] ids) {
        Result result = new Result();
        try {
            for (Long id : ids) {
                NxyDefect nxyDefect = nxyDefectDao.findOne(id);
                if (!nxyDefect.getFixStatus()) {
                    nxyDefect.setFixStatus(true);
                    nxyDefect.setFixTime(new Date());
                    nxyDefect.setFixUser(userService.getCurrentUser().getName());
                    nxyDefectDao.save(nxyDefect);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    public Map<String, Object> getDefectEdited(Long id) {
        NxyDefect nxyDefect = nxyDefectDao.findOne(id);
        @SuppressWarnings("unchecked")
        Map<String, Object> map;
        try {
            map = BeanUtils.describe(nxyDefect);
            if (nxyDefect.getFixStatus()) {
                map.put("fixStatus", "1");
            } else {
                map.put("fixStatus", "0");
            }
            map.put("testProject", nxyDefect.getTestProject());
            if (map.get("nxyFuncItem") != null) {
                map.put("nxyFuncItem", nxyDefect.getNxyFuncItem());
                //map.put("testItem", nxyDefect.getNxyFuncItem().getName());
                //map.put("showId",nxyDefect.getNxyFuncItem().getId());
            } /*else {
                map.put("testItem", nxyDefect.getTestProject().getName());
                map.put("showId",nxyDefect.getTestProject().getId());
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    public List<TreeNode> getAllItemTree() {
        List<TestProject> testProjectList;
        List<TreeNode> tree = new ArrayList<>();
        testProjectList = testProjectDao.findSendProject();
        for (TestProject testProject : testProjectList) {
            TreeNode newNode = new TreeNode(testProject.getId(), testProject.getName(), "closed", "project");
            newNode.setChildren(new ArrayList<TreeNode>());
            List<NxyFuncItem> nxyFuncItemList = nxyFuncItemDao.findByProjectId(testProject.getId());
            tree.add(transformToTree(nxyFuncItemList, newNode));
        }
        return tree;
    }

   /* private List<TreeNode> transformToTree(List<TestProject> testProjectList) {
        for (TestProject testProject : testProjectList) {
            List<NxyFuncItem> nxyFuncItemList = nxyFuncItemDao.findByProjectId(testProject.getId());
            transFormToTree(nxyFuncItemList);
        }
    }*/

    private TreeNode transformToTree(List<NxyFuncItem> nxyFuncItemList, TreeNode treeNode) {
        for (NxyFuncItem nxyFuncItem : nxyFuncItemList) {
            TreeNode newNode = new TreeNode(nxyFuncItem.getId(), nxyFuncItem.getName(), "closed", "item");
            List<NxyFuncItem> childrenList = nxyFuncItemDao.findByParentId(nxyFuncItem.getId());
            if (childrenList.size() != 0) {
                newNode.setChildren(new ArrayList<TreeNode>());
                transformToTree(childrenList, newNode);
            } else {
                newNode.setState("open");
            }
            treeNode.getChildren().add(newNode);
        }
        return treeNode;
    }
}
