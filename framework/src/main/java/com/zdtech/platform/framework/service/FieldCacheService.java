package com.zdtech.platform.framework.service;

import com.zdtech.platform.framework.entity.MsgField;
import com.zdtech.platform.framework.entity.SimSysinsConf;
import com.zdtech.platform.framework.repository.MsgFieldDao;
import com.zdtech.platform.framework.repository.SimSysinsConfDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * FieldCatchService
 *
 * @author panli
 * @date 2017/2/28
 */
@Service
public class FieldCacheService {
    @Autowired
    private MsgFieldDao msgFieldDao;

    private static Map<String,MsgField> cacheSelfHeadFieldMap = new LinkedHashMap<>();
    private static Map<Long,MsgField> cacheSelfHeadIdMap = new LinkedHashMap<>();
    private static List<MsgField> cacheSelfHeadSortList = new ArrayList<>();
    private static Map<String,Long> cacheSelfBodyFieldMap = new LinkedHashMap<>();
    private static Map<Long,String> cacheSelfIdFieldIdMap = new LinkedHashMap<>();
    private static Map<Long,String> cacheSelfPinFieldMap = new LinkedHashMap<>();
    private static Map<String,Long> cacheSelfZzzFieldMap = new LinkedHashMap<>();

    private static Map<String,String> cacheXmlFieldMap = new LinkedHashMap<>();
    private static Map<String,Long> cacheJhFieldMap = new LinkedHashMap<>();

    @PostConstruct
    public void loadMsgFields(){
        cacheSelfHeadFieldMap.clear();
        cacheSelfHeadIdMap.clear();
        cacheSelfBodyFieldMap.clear();
        cacheSelfHeadSortList.clear();
        cacheSelfIdFieldIdMap.clear();
        cacheSelfPinFieldMap.clear();
        cacheSelfZzzFieldMap.clear();
        cacheXmlFieldMap.clear();
        cacheJhFieldMap.clear();
        List<MsgField> fields = msgFieldDao.findSelfFieldsByType("SELF","HEAD");
        if (fields.size() >=14){
            for (int i = 0; i < 14;i++){
                MsgField field = fields.get(i);
                cacheSelfHeadSortList.add(field);
                if (!cacheSelfHeadFieldMap.containsKey(field.getFieldId())){
                    cacheSelfHeadFieldMap.put(field.getFieldId(),field);
                }
                if (!cacheSelfHeadIdMap.containsKey(field.getId())){
                    cacheSelfHeadIdMap.put(field.getId(),field);
                }
                if (!cacheSelfIdFieldIdMap.containsKey(field.getId())){
                    cacheSelfIdFieldIdMap.put(field.getId(),field.getFieldId());
                }
                if (!cacheSelfBodyFieldMap.containsKey(field.getFieldId())){
                    cacheSelfBodyFieldMap.put(field.getFieldId(),field.getId());
                }
            }
        }

        List<MsgField> bodyFields = msgFieldDao.findSelfFieldsByType("SELF","BODY");
        for (MsgField field:bodyFields){
            if (!cacheSelfBodyFieldMap.containsKey(field.getFieldId())){
                cacheSelfBodyFieldMap.put(field.getFieldId(),field.getId());
            }
            if (!cacheSelfIdFieldIdMap.containsKey(field.getId())){
                cacheSelfIdFieldIdMap.put(field.getId(),field.getFieldId());
            }
            if (field.getFieldId().equalsIgnoreCase("2027")||field.getFieldId().equalsIgnoreCase("4107")||
                    field.getFieldId().equalsIgnoreCase("4108")||field.getFieldId().equalsIgnoreCase("4096")||
                    field.getFieldId().equalsIgnoreCase("4394")||field.getFieldId().equalsIgnoreCase("4395")){
                cacheSelfPinFieldMap.put(field.getId(),field.getFieldId());
            }
            if (field.getFieldId().equalsIgnoreCase("zzzx")||field.getFieldId().equalsIgnoreCase("zzzy")||
                    field.getFieldId().equalsIgnoreCase("zzzw")||field.getFieldId().equalsIgnoreCase("zzzz")){
                cacheSelfZzzFieldMap.put(field.getFieldId(),field.getId());
            }
        }
        List<MsgField> xmlBodyFields = msgFieldDao.findSelfFieldsByType("XML","BODY");
        for (MsgField field:xmlBodyFields){
            if (!cacheXmlFieldMap.containsKey(field.getFieldId())){
                cacheXmlFieldMap.put(field.getFieldId(),field.getNameZh());
            }
        }
        MsgField f = msgFieldDao.findByFieldId("TMsgTyp");
        if (f != null){
            cacheJhFieldMap.put(f.getFieldId(),f.getId());
        }
        f = msgFieldDao.findByFieldId("FilLen");
        if (f != null){
            cacheJhFieldMap.put(f.getFieldId(),f.getId());
        }
        f = msgFieldDao.findByFieldId("AppCod");
        if (f != null){
            cacheJhFieldMap.put(f.getFieldId(),f.getId());
        }
    }

    public Map<String, MsgField> getSelfHeadFieldMap() {
        return cacheSelfHeadFieldMap;
    }

    public Map<Long, MsgField> getSelfHeadIdMap() {
        return cacheSelfHeadIdMap;
    }

    public List<MsgField> getSelfHeadSortList() {
        return cacheSelfHeadSortList;
    }

    public Map<String, Long> getSelfBodyFieldMap() {
        return cacheSelfBodyFieldMap;
    }

    public String getFieldIdById(Long id){
        return cacheSelfIdFieldIdMap.get(id);
    }

    public Long getIdByFieldId(String fieldId){
        return cacheSelfBodyFieldMap.get(fieldId);
    }

    public Map<Long, String> getCacheSelfPinFieldMap() {
        return cacheSelfPinFieldMap;
    }

    public void setCacheSelfPinFieldMap(Map<Long, String> cacheSelfPinFieldMap) {
        FieldCacheService.cacheSelfPinFieldMap = cacheSelfPinFieldMap;
    }

    public Map<String,Long> getCacheSelfZzzFieldMap() {
        return cacheSelfZzzFieldMap;
    }

    public static String getXmlFieldNameZh(String fieldId){
        if (cacheXmlFieldMap.containsKey(fieldId)){
            String name = cacheXmlFieldMap.get(fieldId);
            if (StringUtils.isNotEmpty(name)){
                return name;
            }
        }
        return "";
    }

    public static void setXmlFieldNameZh(String fieldId, String nameZh){
        cacheXmlFieldMap.put(fieldId, nameZh);
    }

    public Map<String, Long> getCacheJhFieldMap() {
        return cacheJhFieldMap;
    }

}
