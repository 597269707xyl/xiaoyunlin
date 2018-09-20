package com.zdtech.platform.framework.service;

import com.zdtech.platform.framework.entity.SysConf;
import com.zdtech.platform.framework.repository.SysConfDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by htma on 2016/5/17.
 */
@Service
public class SysConfService {

    @Autowired
    private SysConfDao sysConfDao;

    public List<SysConf> getCategoryConfList(String category) {
        List<SysConf> confs = sysConfDao.findByCategory(category);
        return confs;
    }

    public boolean addOrUpdate(SysConf conf) {
        boolean isNew = conf.getId() == null ? true : false;
        if (isNew) {
            int count = sysConfDao.countByKey(conf.getKey());
            if (count > 0 || sysConfDao.countByCategoryAndValue(conf.getCategory(),conf.getValue())>0) {
                return false;
            }
        } else {
            long id = conf.getId();
            SysConf exist = sysConfDao.findOne(id);
            if (!exist.getKey().equals(conf.getKey())) {
                int count = sysConfDao.countByKey(conf.getKey());
                if (count > 0) {
                    return false;
                }
            }

            if (!exist.getValue().equals(conf.getValue())){
                int count = sysConfDao.countByCategoryAndValue(
                        conf.getCategory(),conf.getValue());
                if (count > 0) {
                    return false;
                }
            }
        }
        sysConfDao.save(conf);
        return true;
    }

    public void deleteSysConf(SysConf conf){
        if (conf!=null){
            String key = conf.getKey();
            sysConfDao.delete(conf);
            List<SysConf> children = getCategoryConfList(key);
            for (SysConf sysConf : children){
                deleteSysConf(sysConf);
            }
        }
    }

}
