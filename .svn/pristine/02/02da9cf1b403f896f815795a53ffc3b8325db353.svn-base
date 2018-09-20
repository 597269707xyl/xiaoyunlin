package com.zdtech.platform.web.controller;

import com.zdtech.platform.framework.service.GenericService;
import com.zdtech.platform.framework.service.SysCodeService;
import com.zdtech.platform.framework.ui.EntityInfo;
import com.zdtech.platform.framework.ui.EntityManager;
import com.zdtech.platform.framework.ui.FieldInfo;
import com.zdtech.platform.framework.utils.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: qfxu
 * Date: 2015-05-13
 */
@Controller
@RequestMapping(value = "/generic")
public class GenericController {

    @Autowired
    private GenericService genericService;

    @Autowired
    private SysCodeService sysCodeService;

    @RequestMapping(value = "/query/{entityName}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> query(@PathVariable("entityName") String entityName, int page, int rows, String sort, String order, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        EntityInfo entityInfo = EntityManager.getInstance().getEntityInfo(entityName);
        if (entityInfo == null)
            return map;

        Map<String, Object> params = getParams(request, entityInfo);
        int total = genericService.queryTotal(entityInfo, params);
        int pageIndex = page < 1?0:page - 1;
        List<Object> datas = genericService.queryRows(entityInfo, params, pageIndex, rows, sort, order);
        List<Object> transRows = trans(entityInfo, datas);
        map.put("rows", transRows);
        map.put("total", total);
        return map;
    }

    @RequestMapping(value = "queryAll/{entityName}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> query(@PathVariable("entityName") String entityName, String sort, String order, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        EntityInfo entityInfo = EntityManager.getInstance().getEntityInfo(entityName);
        if (entityInfo == null)
            return map;

        Map<String, Object> params = getParams(request, entityInfo);
        int total = genericService.queryTotal(entityInfo, params);
        List<Object> rows = genericService.queryRows(entityInfo, params, sort, order);
        List<Object> transRows = trans(entityInfo, rows);
        map.put("rows", transRows);
        map.put("total", total);
        return map;
    }

    @RequestMapping(value = "commonQuery/{tag}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> commonQuery(@PathVariable String tag, @RequestParam Map<String, Object> params,
                                           @PageableDefault Pageable page) {
        Pageable p = page;
        if (page != null){
            p = new PageRequest(page.getPageNumber() < 1?0:page.getPageNumber() - 1,page.getPageSize(),page.getSort());
        }
        return genericService.commonQuery(tag, params, p);
    }

    @RequestMapping(value = "/dict/{category}", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> dict(@PathVariable("category") String category) {
        Map<String, String> dict = sysCodeService.getCategoryCodes(category);
        List<Map<String, String>> rnt = new ArrayList<Map<String, String>>();
        if (dict == null || dict.isEmpty())
            return rnt;
        Map<String, String> map = null;
        for (String key : dict.keySet()) {
            map = new HashMap<String, String>();
            map.put("id", key);
            map.put("text", dict.get(key));
            rnt.add(map);
        }
        return rnt;
    }

    @RequestMapping(value = "/userSel")
    public String userSel() {
        return "/test/user-sel";
    }

    private Map<String, Object> getParams(HttpServletRequest request, EntityInfo entityInfo) {
        Map<String, String[]> params = request.getParameterMap();
        if (params == null || params.isEmpty())
            return null;
        Map<String, Object> rnt = new HashMap<String, Object>();
        for (String key : params.keySet()) {
            if (key.equals("page") || key.equals("rows") || key.equals("sort") || key.equals("order"))
                continue;
            String[] values = params.get(key);
            if (values != null && values.length > 0) {
                String value = values[0];
                if (StringUtils.isEmpty(value))
                    continue;
                FieldInfo fieldInfo = entityInfo.getFieldInfo(key);
                if (fieldInfo == null)
                    continue;
                String fieldType = fieldInfo.getType().getSimpleName();
                Object obj = null;
                if (fieldType.equals("Timestamp")) {
                    obj = Timestamp.valueOf(value);
                } else if ((fieldType.equals("Integer")) || (fieldType.equals("int"))) {
                    obj = Integer.valueOf(value);
                } else if ((fieldType.equals("Long")) || (fieldType.equals("long"))) {
                    obj = Long.valueOf(value);
                } else if ((fieldType.equals("Character")) || (fieldType.equals("char"))) {
                    obj = Character.valueOf(value.charAt(0));
                } else if (fieldType.equals("String")) {
                    obj = "%" + value + "%";
                } else if (fieldType.equals("Boolean")) {
                    obj = Boolean.valueOf(value);
                } else {
                    obj = value;
                }
                rnt.put(key, obj);
            }
        }
        return rnt;
    }

    public List<Object> trans(EntityInfo entityInfo, List<Object> rows) {
        if (rows == null || rows.size() == 0)
            return rows;
        List<String> enumFields = entityInfo.getEnumFields();
        if (enumFields == null || enumFields.size() == 0)
            return rows;
        List<Object> rnt = new ArrayList();
        for (Object obj : rows) {
            for (String enumField : enumFields) {
                FieldInfo fieldInfo = entityInfo.getFieldInfo(enumField);
                if (fieldInfo == null)
                    continue;
                String enumCode = fieldInfo.getEnumCode();
                String code = (String) ObjectUtils.getProperty(obj, enumField);
                Map<String, String> dict = sysCodeService.getCategoryCodes(enumCode);
                String value = code;
                if (dict.containsKey(code)) {
                    value = dict.get(code);
                }
                ObjectUtils.setProperty(obj, enumField, value);
            }
            rnt.add(obj);
        }
        return rnt;
    }
}
