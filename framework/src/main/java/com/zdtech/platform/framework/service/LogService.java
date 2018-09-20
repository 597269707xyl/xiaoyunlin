package com.zdtech.platform.framework.service;

import com.zdtech.platform.framework.entity.Log;
import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.framework.repository.LogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by admin on 2016/5/4.
 */
@Service
public class LogService {

    @Autowired
    private LogDao logDao;

    public List<Log> getLogs() {
        return logDao.findAll();
    }

    public Result addLog(Log log) {
        Result result = null;
        return result;
    }


/*    public Result addLog(Log log) {
        Result result = null;
        if (log.getId() == null) {
            Func func1 = logDao.findByName(log.getName());
            if (null != func1) {
                result = new Result(false,"该角色名已经存在!");
            } else {
                funcDao.save(func);
                result = new Result(true,"添加角色成功");
            }
        } else {
            boolean exist = funcDao.findByIdNotAndName(func.getId(), func.getName()).size() > 0 ? true : false;
            if (exist) {
                result = new Result(false,"该角色名已经存在!");;
            } else {
                Func r = funcDao.findOne(func.getId());
                r.setName(func.getName());
                r.setUrl(func.getUrl());
                r.setId(func.getId());

                funcDao.save(r);
                result = new Result(true,"添加角色成功");
            }
        }
        return result;
    }*/
}
