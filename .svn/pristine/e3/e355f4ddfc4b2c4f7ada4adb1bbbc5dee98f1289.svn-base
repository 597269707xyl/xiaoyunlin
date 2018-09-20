package com.zdtech.platform.web.controller.funcexec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.rbac.ShiroUser;
import com.zdtech.platform.framework.repository.NxyFuncConfigDao;
import com.zdtech.platform.framework.repository.NxyFuncItemDao;
import com.zdtech.platform.framework.utils.FileUtils;
import com.zdtech.platform.framework.utils.XMLConverter;
import com.zdtech.platform.service.funcexec.FuncConfigService;
import com.zdtech.platform.utils.ExcelUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yjli on 2017/10/12.
 */
@Controller
@RequestMapping("/func/config")
public class FuncConfigController {
    private Logger logger = LoggerFactory.getLogger(FuncConfigController.class);

    @Autowired
    private FuncConfigService funcConfigService;
    @Autowired
    private NxyFuncConfigDao nxyFuncConfigDao;
    @Autowired
    private NxyFuncItemDao nxyFuncItemDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/list")
    public String list(){
        return "/funcexec/config/func-config-list";
    }

    @RequestMapping(value = "/query")
    @ResponseBody
    public Map<String, Object> query(@RequestParam Map<String, Object> params, @PageableDefault Pageable page){
        return funcConfigService.query(params, page);
    }

    @RequestMapping("/addmain")
    public String addmainPage(){
        return "/funcexec/config/func-config-addmain";
    }

    @RequestMapping("/addsecond")
    public String addsecondPage(){
        return "/funcexec/config/func-config-addsecond";
    }

    @RequestMapping("/get/{id}")
    @ResponseBody
    public NxyFuncConfig getOne(@PathVariable("id") Long id){
        NxyFuncConfig config = nxyFuncConfigDao.getOne(id);
        return config;
    }

    @RequestMapping("/add")
    @ResponseBody
    public Result addOrUpdate(NxyFuncConfig config, Long itemId){
        Result ret = new Result();
        try {
            if(config.getId() == null){
                ret.setMsg("添加成功!");
                List<NxyFuncConfig> list = nxyFuncConfigDao.findByVariableEn(config.getVariableEn(), itemId);
                if(list.size()>0){
                    ret.setSuccess(false);
                    ret.setMsg("该标识已存在!");
                    return ret;
                }
            } else {
                ret.setMsg("修改成功!");
            }
            nxyFuncConfigDao.save(config);
            logger.info("添加或修改成功!");
        } catch (Exception e){
            ret.setSuccess(false);
            if(config.getId() == null){
                ret.setMsg("添加失败!");
            } else {
                ret.setMsg("修改失败!");
            }
        }
        return ret;
    }

    @RequestMapping("/update")
    @ResponseBody
    public Result update(@RequestBody List<Map<String, Object>> mapList){
        Result ret = new Result();
        try {
            for(Map<String, Object> map : mapList){
                Long id = Long.parseLong(map.get("id").toString());
                NxyFuncConfig config = nxyFuncConfigDao.findOne(id);
                String variable_value = map.get("variable_value").toString();
                config.setVariableValue(variable_value);
                nxyFuncConfigDao.save(config);
            }
        } catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("修改失败!");
        }
        return ret;
    }

    @RequestMapping("/getAll")
    @ResponseBody
    public List getAll(){
        return funcConfigService.getAll();
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delUser(@RequestParam("ids[]")Long[] ids) {
        Result ret = new Result();
        try {
            for (Long id:ids){
               nxyFuncConfigDao.delete(id);
            }
            logger.info("@S|true|删除成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@S|false|删除失败!");
        }
        return  ret;
    }

    @RequestMapping("/copyPage")
    public String copy(){
        return "/funcexec/config/func-config-copy";
    }

    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    @ResponseBody
    public Result configcopy(@RequestParam("configIds[]") Long[] configIds, @RequestParam("id") Long id) {
        Result ret = new Result();
        try{
            ret = funcConfigService.copyConfig(configIds, id);
        } catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("复制失败!");
        }
        return ret;
    }

    @RequestMapping("/downloadPage")
    public String downloadPage(){
        return "/funcexec/config/func-config-download";
    }

    @RequestMapping(value = "/download")
    public void download(Long itemId, HttpServletResponse response){
        try {
            java.util.Date now = new java.util.Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dates = sf.format(now);
            String[] headers = {"名称", "标识", "值"};
            List<NxyFuncConfig> configList = funcConfigService.getByItemId(itemId);
            List<NxyFuncConfigExcel> data = new ArrayList();
            String title = "公共属性";
            String sql = "select name from nxy_func_item where id=" + itemId;
            String name = jdbcTemplate.queryForObject(sql, String.class);
            if(name != null){
                title = name;
            }
            for(NxyFuncConfig conf : configList){
                NxyFuncConfigExcel excel = new NxyFuncConfigExcel();
                excel.setVariableEn(conf.getVariableEn());
                excel.setVariableZh(conf.getVariableZh());
                excel.setVariableValue(conf.getVariableValue());
                data.add(excel);
            }
            response.reset();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("UTF-8");
            String fileDisplay = new String((title+"配置文件" + dates + ".xls").getBytes("GB2312"), "iso8859-1");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileDisplay);
            // 输出资源内容到相应对象
            ExcelUtils excelUtils = new ExcelUtils();
            excelUtils.exportExcel(title, headers, data, response.getOutputStream());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @RequestMapping("/uploadPage")
    public String uploadPage(){
        return "/funcexec/config/func-config-upload";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Result upload(Long itemId, @RequestParam(required = false) CommonsMultipartFile file) {
        Result result = null;
        if (!file.isEmpty()) {
            try {
                List<Map<String, String>> list = new ExcelUtils<>().readExcel(file);
                logger.info("上传配置文件为：" + new ObjectMapper().writeValueAsString(list));
                if(list == null || list.size()==0){
                    result = new Result();
                    result.setSuccess(false);
                    result.setMsg("上传文件为空!");
                } else {
                    List<NxyFuncConfig> configList = funcConfigService.getByItemId(itemId);
                    List<NxyFuncConfig> configListAdd = new ArrayList<>();
                    List<NxyFuncConfig> configListUpdate = new ArrayList<>();
                    for(Map<String, String> map : list){
                        String variableZh = map.get("0");
                        String variableEn = map.get("1");
                        String variableValue = map.get("2");
                        if(StringUtils.isEmpty(variableEn) || StringUtils.isEmpty(variableZh)){
                            continue;
                        }
                        boolean flag = true;
                        for(NxyFuncConfig config : configList){
                            if(config.getVariableEn().equals(variableEn)){
                                flag = false;
                                config.setVariableZh(variableZh);
                                config.setVariableValue(variableValue);
                                configListUpdate.add(config);
                                break;
                            }
                        }
                        if(flag){
                            NxyFuncConfig config = new NxyFuncConfig();
                            config.setItemId(itemId);
                            config.setVariableZh(variableZh);
                            config.setVariableEn(variableEn);
                            config.setVariableValue(variableValue);
                            config.setCreateTime(new Date());
                            configListAdd.add(config);
                        }
                    }
                    if(configListAdd.size()>0){
                        nxyFuncConfigDao.save(configListAdd);
                    }
                    if(configListUpdate.size()>0){
                        nxyFuncConfigDao.save(configListUpdate);
                    }
                }
                logger.info("上传文件成功");
                result = new Result();
                result.setSuccess(true);
            } catch (Exception e) {
                e.printStackTrace();
                result = new Result();
                result.setSuccess(false);
                result.setMsg("上传文件解析失败!");
            }
        } else {
            result = new Result();
            result.setSuccess(false);
            result.setMsg("上传文件为空!");
        }
        return result;
    }

    @RequestMapping("/getItemListByProject")
    @ResponseBody
    public List<NxyFuncItem> getItemListByProject(Long projectId){
        return nxyFuncItemDao.findByProjectIdAndMark(projectId, "send", "t");
    }
}
