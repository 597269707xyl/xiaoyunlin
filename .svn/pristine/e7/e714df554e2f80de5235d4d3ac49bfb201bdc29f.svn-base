package com.zdtech.platform.web.controller.system;

import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.framework.entity.SysConf;
import com.zdtech.platform.framework.repository.SysConfDao;
import com.zdtech.platform.framework.service.SysCodeService;
import com.zdtech.platform.framework.service.SysConfService;
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
 * Created by htma on 2016/5/17.
 */
@Controller
@RequestMapping(value = "/sys/conf")
public class SysConfController {
    private static Logger logger = LoggerFactory.getLogger(SysConfController.class);

    @Autowired
    private SysConfService sysConfService;
    @Autowired
    private SysConfDao sysConfDao;
    @Autowired
    private SysCodeService codeService;

    @RequestMapping(value = "/list")
    public String funcList() {
        return "system/conf/conf-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addConf() {
        return "system/conf/conf-add";
    }

    @RequestMapping(value = "/get/{id}")
    @ResponseBody
    public SysConf get(@PathVariable Long id) {
        SysConf conf = sysConfDao.findOne(id);
        return conf;
    }

    @RequestMapping(value = "/treeList")
    @ResponseBody
    public List<Map<String, Object>> treeList(Long id) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<SysConf> confs = new ArrayList<>();
        if (id == null) {
            confs = sysConfDao.findByCategory("SYS_CONF");
        } else {
            SysConf conf = sysConfDao.findOne(id);
            confs = sysConfDao.findByCategory(conf.getKey());
        }
        try {
            for (SysConf conf : confs) {
                Map<String, Object> map = BeanUtils.describe(conf);
                Long count = sysConfDao.countByCategory(conf.getKey());
                boolean isLeaf = count > 0 ? false : true;
                map.put("state", isLeaf ? "open" : "closed");
                map.put("isLeaf", isLeaf);
                result.add(map);
            }
        } catch (Exception e) {
        }
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result add(SysConf conf) {
        Result result = null;
        try {
            boolean added = sysConfService.addOrUpdate(conf);
            if (added) {
                codeService.loadCodes();
                result = new Result(true, "");
                logger.info("@S|true|添加或修改系统配置成功！");
            } else {
                result = new Result(false, "系统中已经有相同[配置名称]或[键]的配置！");
                logger.warn("@S|false|系统中已有相同[键]或[配置名称]的配置！");
            }
        } catch (Exception e) {
            result = new Result(false, "添加或修改配置失败!");
            logger.error("@S|false|添加或修改配置失败!");
        }
        return result;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delConf(Long id) {
        Result result = null;
        try {
            SysConf conf = sysConfDao.findOne(id);
            sysConfService.deleteSysConf(conf);
            codeService.loadCodes();
            result = new Result(true, "");
            logger.info("@S|true|删除系统配置成功！");
        } catch (Exception e) {
            result = new Result(false, "");
            logger.error("@S|false|删除配置失败!");
        }
        return result;
    }
}
