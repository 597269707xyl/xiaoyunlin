package com.zdtech.platform.web.controller.funcexec;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.NxyFuncCaseMarkDao;
import com.zdtech.platform.framework.repository.NxyFuncEpccMarkDao;
import com.zdtech.platform.framework.repository.NxyFuncUsecaseExecDao;
import com.zdtech.platform.framework.utils.FileUtils;
import com.zdtech.platform.service.funcexec.FuncEpccService;
import com.zdtech.platform.utils.ExcelUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lyj on 2018/1/9.
 */
@Controller
@RequestMapping("/func/epcc")
public class FuncEpccController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private NxyFuncEpccMarkDao epccMarkDao;
    @Autowired
    private FuncEpccService funcEpccService;
    @Autowired
    private NxyFuncUsecaseExecDao usecaseExecDao;

    @RequestMapping(value = "/list")
    public String list(){
        return "/funcexec/epcc/epcc-mark-list";
    }

    @RequestMapping("/msgList")
    public String msgList(){
        return "/funcexec/epcc/msg-list";
    }

    @RequestMapping("/addpage")
    public String add(){
        return "/funcexec/epcc/epcc-mark-add";
    }

    @RequestMapping("/get/{id}")
    @ResponseBody
    public NxyFuncEpccMark getOne(@PathVariable("id") Long id){
        NxyFuncEpccMark mark = epccMarkDao.getOne(id);
        return mark;
    }

    @RequestMapping("/add")
    @ResponseBody
    public Result add(NxyFuncEpccMark mark){
        Result ret = new Result();
        try {
            int count = epccMarkDao.countByCodeAndStandard(mark.getMsgCode(), mark.getStandard());
            if(count > 0){
                ret.setSuccess(false);
                ret.setMsg("该标识已经存在!");
                return ret;
            }
            mark.setFieldId(mark.getFieldId().trim());
            epccMarkDao.save(mark);
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
                epccMarkDao.delete(id);
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
                NxyFuncEpccMark mark = epccMarkDao.findOne(id);
                String fieldId = map.get("fieldId").toString().trim();
                mark.setFieldId(fieldId);
                epccMarkDao.save(mark);
            }
        } catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("修改失败!");
        }
        return ret;
    }

    @RequestMapping(value = "/query")
    public String query(){
        return "/funcexec/epcc/epcc-query-list";
    }

    @RequestMapping(value = "/queryList")
    @ResponseBody
    public Map<String, Object> query(@RequestParam Map<String, Object> params, @PageableDefault Pageable page){
        Map<String, Object> ret = new HashMap<>();
        try {
            ret = funcEpccService.queryList(params, page);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }

    @RequestMapping(value = "/exportExcel")
    public void exportExcel(@RequestParam Map<String, Object> params, HttpServletResponse response){
        try {
            java.util.Date now = new java.util.Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dates = sf.format(now);
            String[] headers = {"案例编号", "用例目的", "用例步骤", "流水号", "发送数据", "接收数据"};
            List<EpccExcel> excelList = new ArrayList<>();
            //添加数据
            Map<String, Object> ret = funcEpccService.queryList(params, null);
            List<Map<String, Object>> result = (List<Map<String, Object>>) ret.get("rows");
            for(Map<String, Object> map : result){
                EpccExcel epccExcel = new EpccExcel();
                epccExcel.setEpccIds(map.get("epcc_ids").toString());
                epccExcel.setSendMsg(map.get("msg").toString());
                epccExcel.setRecvMsg(map.get("message").toString());
                epccExcel.setCaseNo(map.get("case_no").toString());
                Long execId = Long.parseLong(map.get("usecase_exec_id").toString());
                NxyFuncUsecaseExec usecaseExec = usecaseExecDao.findOne(execId);
                NxyFuncUsecase usecase = usecaseExec.getNxyFuncUsecase();
                epccExcel.setPurpose(usecase.getPurpose());
                epccExcel.setStep(usecase.getStep());
                excelList.add(epccExcel);
            }
            logger.info("@K|true|导出成功！");
            response.reset();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("UTF-8");
            String fileDisplay = new String(("EPCC执行结果查询" + dates + ".xls").getBytes("GB2312"), "iso8859-1");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileDisplay);
            // 输出资源内容到相应对象
            byte[] b = new byte[1024];
            int len;
            ExcelUtils excelUtils = new ExcelUtils();
            excelUtils.exportExcel("对比报文信息", headers, excelList, response.getOutputStream());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
