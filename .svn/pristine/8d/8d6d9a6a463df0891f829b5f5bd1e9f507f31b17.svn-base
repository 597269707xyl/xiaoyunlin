package com.zdtech.platform.web.controller.system;

import com.zdtech.platform.framework.service.LogService;
import com.zdtech.platform.framework.service.UserService;
import com.zdtech.platform.framework.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by admin on 2016/4/27.
 */
@Controller
@RequestMapping(value = "/sys/log")
public class LogController {
    private static Logger logger = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private LogService logService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String logList() {
        return "system/log/log-list";
    }

    @RequestMapping(value = {"/add","/edit"}, method = RequestMethod.GET)
    public String addLog() {
        return "system/log/log-add";
    }

/*    @RequestMapping(value = "/export")
    public void logExport(@RequestParam Map<String,String> params, HttpServletResponse response){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(new Date());
        String title="测试管理平台导出日志";
        Specification spec = parseRequestParams(params);
        String[] columns = new String[]{"操作人","日志类型","操作时间","IP地址","URL","内容"};
        int[] columnWidth = new int[]{8*256,15*256,20*256,24*256,35*256,60*256};
        ExcelSetting setting = new ExcelSetting(title,columns,columnWidth);
        try {
            response.reset();
            String fileName = title +"-"+date+ ".xlsx";
//            response.setContentType("application/octet-stream");
            response.setContentType("application/x-download");
            response.setCharacterEncoding("UTF-8");
            fileName = new String(fileName.getBytes(), "ISO8859-1");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);

            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            BeanRowExporter exporter = new DefaultBeanRowExporter();
            SpecDataProvider dataProvider = new SpecDataProvider(logDao,spec);
            exporter.export(setting,dataProvider,bos);

            String delRecord = params.get("delRecord");
            if (StringUtils.isNotEmpty(delRecord) && delRecord.equals("true")){
                List<SysLog> logs = logDao.findAll(spec);
                logDao.deleteInBatch(logs);
                log.info("@S|true|日志导出并删除,类型:[{}],操作人:[{}],日期:[{}]--[{}]",
                        params.get("type"),params.get("userName"),params.get("fromDate"),params.get("toDate"));
            }else{
                log.info("@S|true|日志导出,类型:[{}],操作人:[{}],日期:[{}]--[{}]",
                        params.get("type"),params.get("userName"),params.get("fromDate"),params.get("toDate"));
            }
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @RequestMapping(value = {"/add","/edit"}, method = RequestMethod.POST)
    @ResponseBody
    public Result addLog(Log log) {
        Result ret = new Result();
        try {
            ret = logService.addLog(log);
        }catch (Exception e){
            ret.setSuccess(false);
            ret.setMsg("操作失败");
        }
        return  ret;
    }
}
