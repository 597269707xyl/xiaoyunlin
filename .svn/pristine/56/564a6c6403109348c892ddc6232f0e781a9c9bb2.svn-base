package com.zdtech.platform.web.controller.funcexec;

import com.zdtech.platform.framework.entity.NxyFuncCaseMark;
import com.zdtech.platform.framework.entity.NxyFuncConfig;
import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.framework.repository.NxyFuncCaseMarkDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by lyj on 2018/1/9.
 */
@Controller
@RequestMapping("/mark")
public class FuncCaseMarkController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private NxyFuncCaseMarkDao markDao;

    @RequestMapping(value = "/list")
    public String list(){
        return "/funcexec/casemark/case-mark-list";
    }

    @RequestMapping("/msgList")
    public String msgList(){
        return "/funcexec/casemark/msg-list";
    }

    @RequestMapping("/addpage")
    public String add(){
        return "/funcexec/casemark/case-mark-add";
    }

    @RequestMapping("/get/{id}")
    @ResponseBody
    public NxyFuncCaseMark getOne(@PathVariable("id") Long id){
        NxyFuncCaseMark mark = markDao.getOne(id);
        return mark;
    }

    @RequestMapping("/add")
    @ResponseBody
    public Result add(NxyFuncCaseMark mark){
        Result ret = new Result();
        try {
            int count = markDao.countByCodeAndStandard(mark.getMsgCode(), mark.getStandard());
            if(count > 0){
                ret.setSuccess(false);
                ret.setMsg("该标识已经存在!");
                return ret;
            }
            mark.setFieldId(mark.getFieldId().trim());
            markDao.save(mark);
            ret.setMsg("添加成功!");
            logger.info("添加成功!");
        } catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("添加失败!");
        }
        return ret;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delUser(@RequestParam("ids[]")Long[] ids) {
        Result ret = new Result();
        try {
            for (Long id:ids){
                markDao.delete(id);
            }
            logger.info("@S|true|删除成功！");
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
            logger.error("@S|false|删除失败!");
        }
        return  ret;
    }

    @RequestMapping("/update")
    @ResponseBody
    public Result update(@RequestBody List<Map<String, Object>> mapList){
        Result ret = new Result();
        try {
            for(Map<String, Object> map : mapList){
                Long id = Long.parseLong(map.get("id").toString());
                NxyFuncCaseMark mark = markDao.findOne(id);
                String fieldId = map.get("fieldId").toString().trim();
                mark.setFieldId(fieldId);
                markDao.save(mark);
            }
        } catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("修改失败!");
        }
        return ret;
    }
}
