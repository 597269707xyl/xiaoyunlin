package com.zdtech.platform.framework.utils;

import com.zdtech.platform.framework.service.SysCodeService;
import com.zdtech.platform.framework.ui.FieldEnum;

import javax.persistence.PostLoad;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * User: lcheng
 * Date: 2015-05-29
 */
public class EntityCodeListener {

    @PostLoad
    public void postLoad(Object obj) {
        Class clz = obj.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            FieldEnum fieldEnum = field.getAnnotation(FieldEnum.class);
            if (fieldEnum == null)
                continue;
            String enumCode = fieldEnum.code();
            Object codeObj = ObjectUtils.getProperty(obj, field.getName());
            String code = codeObj!=null ? codeObj.toString() : null;
            if (code!=null){
                String value = getDict(enumCode, code);
                ObjectUtils.setProperty(obj, field.getName()+"Dis", value);
            }
        }
    }

    private String getDict(String catalog, String code) {
        SysCodeService codeService = SysUtils.getBean(SysCodeService.class);
        if (codeService == null){
            return code;
        }
        Map<String, String> map = codeService.getCategoryCodes(catalog);
        if (map == null || map.isEmpty())
            return code;
        if (map.containsKey(code))
            return map.get(code);
        return code;
    }
}
