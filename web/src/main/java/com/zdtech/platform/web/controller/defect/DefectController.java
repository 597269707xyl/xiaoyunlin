package com.zdtech.platform.web.controller.defect;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.NxyDefectDao;
import com.zdtech.platform.service.defect.DefectService;
import com.zdtech.platform.web.controller.funcexec.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by huangbo on 2018/8/1.
 */
@Controller
@RequestMapping(value = "/defect")
public class DefectController {
    private static Logger logger = LoggerFactory.getLogger(DefectController.class);
    @Autowired
    private DefectService defectService;
    @Autowired
    private NxyDefectDao nxyDefectDao;

    @RequestMapping(value = "/list")
    public String list() {
        return "defect/defect-list";
    }

    @RequestMapping(value = "/statistics/list")
    public String statistics() {
        return "defect/defect-statistics-list";
    }

    @RequestMapping(value = "/addImp")
    public String addImp() {
        return "defect/defect-imp-add";
    }

    @RequestMapping(value = "/addUd")
    public String addUd() {
        return "defect/defect-ud-add";
    }

    @RequestMapping(value = "/addImp", method = RequestMethod.POST)
    @ResponseBody
    public Result addImpDefect(NxyDefect nxyDefect, Long usecaseId) {
        Result result = new Result();
        try {
            defectService.addImpDefect(nxyDefect, usecaseId);
            logger.info("@P|true|添加项目成功");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg("操作失败");
            logger.error("@P|false|添加项目失败");
        }
        return result;
    }

    @RequestMapping(value = "/addUd", method = RequestMethod.POST)
    @ResponseBody
    public Result addUdDefect(NxyDefect nxyDefect, Long testItem1, String stype) {
        Result result = new Result();
        try {
            defectService.addUdDefect(nxyDefect, testItem1, stype);
            logger.info("@P|true|添加项目成功");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg("操作失败");
            logger.error("@P|false|添加项目失败");
        }
        return result;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result del(@RequestParam("ids[]") Long[] ids) {
        Result result = new Result();
        try {
            for (Long id : ids) {
                nxyDefectDao.delete(id);
                result.setSuccess(true);
            }
        } catch (Exception e) {
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value = "/get/{id}")
    @ResponseBody
    public Map<String,Object> edit(@PathVariable("id") Long id) {
        Map<String,Object>map = defectService.getDefectEdited(id);
        return map;
    }

    @RequestMapping(value = "/getStatistics")
    @ResponseBody
    public Map<String, Object> getStatistics(@RequestParam Map<String, Object> params) {
        if (params.get("id") != null) {
            Map<String, Object> map = defectService.getStatistics(params);
            return map;
        }
        return null;
    }

    @RequestMapping(value = "/getDefectMatched")
    @ResponseBody
    public Map<String, Object> getDefectMatched(@RequestParam Map<String, Object> params, @PageableDefault Pageable page) {
        Pageable p = page;
        if (page != null) {
            p = new PageRequest(page.getPageNumber() < 1 ? 0 : page.getPageNumber() - 1, page.getPageSize(), page.getSort());
        }
        if (params.size() > 2) {
            Map<String, Object> map = defectService.getDefectMatched(params, p);
            return map;
        }
        return null;
    }

    @RequestMapping(value = "/getDefect")
    @ResponseBody
    public Map<String, Object> getDefect(@RequestParam Map<String, Object> params, @PageableDefault Pageable page) {
        Pageable p = page;
        if (page != null) {
            p = new PageRequest(page.getPageNumber() < 1 ? 0 : page.getPageNumber() - 1, page.getPageSize(), page.getSort());
        }

        Map<String, Object> map = defectService.getDefect(params, p);
        return map;
    }

    @RequestMapping(value = "/exportExcel")
    public void exportExcel(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        defectService.exportExcel(params, response);
    }

    @RequestMapping(value = "/setFixed", method = RequestMethod.POST)
    @ResponseBody
    public Result setFixed(@RequestParam("ids[]") Long[] ids) {
        Result result = defectService.setFixed(ids);
        return result;
    }

    @RequestMapping(value = "/getAllItemTree")
    @ResponseBody
    public List<TreeNode> getAllItemTree(){
        return defectService.getAllItemTree();
    }
}
