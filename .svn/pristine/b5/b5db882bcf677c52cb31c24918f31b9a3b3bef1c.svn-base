package com.zdtech.platform.web.controller.system;

import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.framework.entity.SysCode;
import com.zdtech.platform.framework.repository.SysCodeDao;
import com.zdtech.platform.framework.service.SysCodeService;
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
import java.util.List;
import java.util.Map;

/**
 * Created by leepan on 2016/5/8.
 */
@Controller
@RequestMapping(value = "/sys/code")
public class CodeController {
    private static Logger logger = LoggerFactory.getLogger(CodeController.class);

    @Autowired
    private SysCodeDao codeDao;
    @Autowired
    private SysCodeService codeService;

    @RequestMapping(value = "/list")
    public String funcList(){
        return "system/code/code-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addCode() {
        return "system/code/code-add";
    }

    @RequestMapping(value = "/get/{id}")
    @ResponseBody
    public SysCode get(@PathVariable Long id) {
        SysCode code = codeDao.findOne(id);
        return code;
    }

    @RequestMapping(value = "/treeList")
    @ResponseBody
    public List<Map<String, Object>> treeList(Long id) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<SysCode> codes = new ArrayList<>();
        if (id == null) {
            codes = codeDao.findByCategoryOrderBySeqNoAscKeyAsc("ROOT");
        } else {
            SysCode code = codeDao.findOne(id);
            codes = codeDao.findByCategoryOrderBySeqNoAscKeyAsc(code.getKey());
        }
        try {
            for (SysCode code : codes) {
                Map<String, Object> map = BeanUtils.describe(code);
                Long count = codeDao.countByCategory(code.getKey());
                boolean isLeaf = count > 0 ? false : true;
                map.put("state",isLeaf?"open":"closed");
                map.put("isLeaf", isLeaf);
                result.add(map);
            }
        } catch (Exception e) {
        }
        return result;
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public Result add(SysCode code){
        Result result = null;
        try{
            boolean added = codeService.addOrUpdate(code);
            if (added){
                codeService.loadCodes();
                result = new Result(true,"");
                logger.info("@S|true|字典添加或修改成功！");
            }else{
                result = new Result(false,"系统中已经有相同[键]或[值]的字典！");
                logger.warn("@S|false|系统中已有相同[键]或[值]的字典！");
            }
        }catch(Exception e){
            result = new Result(false,"添加或修改字典失败!");
            logger.error("@S|false|添加或修改字典失败！");
        }
        return result;
    }

    @RequestMapping(value = "/del",method = RequestMethod.POST)
    @ResponseBody
    public Result delCode(Long id){
        Result result = null;
        try{
            SysCode code = codeDao.findOne(id);
            codeService.deleteSysCode(code);
            codeService.loadCodes();
            result = new Result(true,"");
            logger.info("@S|true|删除字典成功！");
        }catch(Exception e){
            result = new Result(false,"");
            logger.error("@S|false|删除字典失败！");
        }
        return result;
    }
}
