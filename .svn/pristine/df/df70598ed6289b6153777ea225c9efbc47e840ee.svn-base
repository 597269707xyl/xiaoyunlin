package com.zdtech.platform.framework.service;

import com.zdtech.platform.framework.entity.SysCode;
import com.zdtech.platform.framework.entity.SysConf;
import com.zdtech.platform.framework.repository.SysCodeDao;
import com.zdtech.platform.framework.repository.SysConfDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lcheng on 2015/5/13.
 */
@Service
public class SysCodeService {

    @Autowired
    private SysCodeDao codeDao;
    @Autowired
    private SysConfDao confDao;

    private static Map<String, Map<String, String>> cacheMap = new LinkedHashMap<>();
    private static Map<String, Map<String, String>> cacheConfMap = new LinkedHashMap<>();

    @PostConstruct
    public void loadCodes(){
        Sort sort = new Sort(Sort.Direction.ASC,"category","seqNo");
        List<SysCode> codes = codeDao.findAll(sort);
        List<SysConf> confs = confDao.findAll();
        cacheMap.clear();
        cacheConfMap.clear();
        for (SysCode code : codes){
            if (!cacheMap.containsKey(code.getCategory())){
                cacheMap.put(code.getCategory(),new LinkedHashMap<String, String>());
            }
            cacheMap.get(code.getCategory()).put(code.getKey(),code.getValue());
        }
        for (SysConf conf:confs){
            if (!cacheConfMap.containsKey(conf.getCategory())){
                cacheConfMap.put(conf.getCategory(),new LinkedHashMap<String, String>());
            }
            cacheConfMap.get(conf.getCategory()).put(conf.getKey(),conf.getKeyVal());
        }
    }
    public String getConfValueByCategoryAndKey(String category,String key){
        if (cacheConfMap.containsKey(category)){
            Map<String,String> map = cacheConfMap.get(category);
            if (map.containsKey(key)){
                return map.get(key);
            }
        }
        return null;
    }

    public Map<String, String> getCategoryCodes(String category) {
        Map<String, String> result = new LinkedHashMap<>();
        if (category == null) {
            return null;
        }
        if (cacheMap.containsKey(category)) {
            return cacheMap.get(category);
        }

        List<SysCode> codes = codeDao.findByCategoryOrderBySeqNoAscKeyAsc(category);
        for (SysCode code : codes) {
            result.put(code.getKey(), code.getValue());
        }
//        cacheMap.put(category, result);
        return result;
    }

    public List<SysCode> getCategoryCodeList(String category) {
        List<SysCode> codes = codeDao.findByCategoryOrderBySeqNoAscKeyAsc(category);
        return codes;
    }

    public Page<SysCode> getCodes(Pageable page) {
        return codeDao.findAll(page);
    }

    public Page<SysCode> getCodes(String category, Pageable page) {
        return codeDao.findByCategoryOrderBySeqNoAscKeyAsc(category, page);
    }

    public boolean addOrUpdate(SysCode code) {
        boolean isNew = code.getId() == null ? true : false;
        if (isNew) {
            int count = codeDao.countByKey(code.getKey());
            if (count > 0 || codeDao.countByCategoryAndValue(code.getCategory(),code.getValue())>0) {
                return false;
            }
        } else {
            long id = code.getId();
            SysCode exist = codeDao.findOne(id);
            if (!exist.getKey().equals(code.getKey())) {
                int count = codeDao.countByKey(code.getKey());
                if (count > 0) {
                    return false;
                }
            }

            if (!exist.getValue().equals(code.getValue())){
                int count = codeDao.countByCategoryAndValue(
                        code.getCategory(),code.getValue());
                if (count > 0) {
                    return false;
                }
            }

        }
        codeDao.save(code);
        return true;
    }

    public void deleteSysCode(SysCode code){
        if (code!=null){
            String key = code.getKey();
            codeDao.delete(code);
            List<SysCode> children = getCategoryCodeList(key);
            for (SysCode sysCode : children){
                deleteSysCode(sysCode);
            }
        }
    }
}
