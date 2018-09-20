package com.zdtech.platform.web.controller.funcexec;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.utils.FileUtils;
import com.zdtech.platform.service.funcexec.FuncUsecaseService;
import com.zdtech.platform.utils.ExcelUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * FuncExecQueryController
 *
 * @author panli
 * @date 2017/9/8
 */
@Controller
@RequestMapping("/func/execquery")
public class FuncExecQueryController {
    private Logger logger = LoggerFactory.getLogger(FuncExecQueryController.class);
    @Autowired
    private NxyFuncUsecaseExecDao nxyFuncUsecaseExecDao;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private SysCodeDao sysCodeDao;
    @Autowired
    private NxyFuncUsecaseExecSendDao nxyFuncUsecaseExecSendDao;
    @Autowired
    private TestProjectDao testProjectDao;
    @Autowired
    private NxyFuncItemDao nxyFuncItemDao;
    @Autowired
    private NxyUsecaseExecBatchDao batchDao;
    @Autowired
    private FuncUsecaseService funcUsecaseService;
    @Autowired
    private NxyFuncUsecaseExecRecvDao recvDao;
    @Autowired
    private SysConfDao confDao;

    private String getToolType(){
        SysConf conf = confDao.findByCategoryAndKey("SIMULATOR_SERVER", "TOOL_TYPE");
        if(conf != null){
            return conf.getKeyVal();
        }
        return "atsp";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        return "/funcexec/usecase/func-exec-query-list";
    }
    @RequestMapping(value = "/query",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getUsecaseByItem(@RequestParam Map<String, Object> params, @PageableDefault Pageable page) {
        Pageable p = page;
        if (page != null) {
            p = new PageRequest(page.getPageNumber() < 1 ? 0 : page.getPageNumber() - 1, page.getPageSize(), page.getSort());
        }
        if (params.size() > 3) {
            String itemType = params.get("type").toString();
            String itemId = params.get("nxyFuncItem.id").toString();
            String result = "";
            if (itemType.equalsIgnoreCase("project")){
                List<NxyFuncItem> items = nxyFuncItemDao.findItemsByProjectId(Long.parseLong(itemId));
                if (items == null || items.size() < 1){
                    return null;
                }
                for (NxyFuncItem item:items){
                    result = result + "," +item.getId().toString();
                }
            }else {
                javax.persistence.Query query1 = entityManager.createNativeQuery(String.format("SELECT getChildList(%s)",itemId));
                Object obj = query1.getSingleResult();
                result = obj.toString();

            }
            result = result.substring(1);
            String sql = String.format("select * from nxy_func_usecase where item_id in (%s) and 1=1",result);
            if (params.containsKey("no|like")){
                sql = sql + String.format(" and no like '%%%s%%'",params.get("no|like"));
            }
            if (params.containsKey("caseNumber|like")){
                sql = sql + String.format(" and case_number like '%%%s%%'",params.get("caseNumber|like"));
            }
            if (params.containsKey("purpose|like")){
                sql = sql + String.format(" and purpose like '%%%s%%'",params.get("purpose|like"));
            }
            if (params.containsKey("result")){
                sql = sql + String.format(" and result = '%s'",params.get("result"));
            }
            javax.persistence.Query query = entityManager.createNativeQuery(sql);
            query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map<String,Object>> list = null;
            if (p != null) {
                int pageNumber = page.getPageNumber();
                int pageSize = page.getPageSize();
                list = query.setFirstResult((pageNumber-1)*pageSize).setMaxResults(page.getPageSize()).getResultList();
            } else {
                list = query.getResultList();
            }
            for (Map<String,Object> row:list){
                int number = nxyFuncUsecaseExecDao.countByUsecaseId(Long.parseLong(row.get("id").toString()));
                row.put("number",number);
                SysCode code = sysCodeDao.findByCategoryAndKey("UC_RESULT",row.get("result").toString());
                row.put("resultDis",code.getValue());
            }
            sql = sql.replaceFirst("select \\*","select count(1)");
            query = entityManager.createNativeQuery(sql);
            Object o = query.getSingleResult();
            String total = o.toString();
            Map<String, Object> ret = new HashMap<>();
            ret.put("rows", list);
            ret.put("total",total);
            return ret;
        }
        return null;
    }

    @RequestMapping(value = "/batchList", method = RequestMethod.GET)
    public String batchList(Model model) {
        model.addAttribute("toolType", getToolType());
        return "/funcexec/usecase/func-exec-batch-list";
    }

    @RequestMapping(value = "/batchListData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> batchListData(@RequestParam Map<String, Object> params, @PageableDefault Pageable page){
        Pageable p = page;
        javax.persistence.Query query;
        if (page != null) {
            p = new PageRequest(page.getPageNumber() < 1 ? 0 : page.getPageNumber() - 1, page.getPageSize(), page.getSort());
        }
        String sql = "select * from nxy_usecase_exec_batch where 1=1";
        if (params.containsKey("startTime")){
            sql = sql + String.format(" and begin_time >= '%s'",params.get("startTime"));
        }
        if (params.containsKey("endTime")){
            sql = sql + String.format(" and begin_time <= '%s'",params.get("endTime"));
        }
        if (params.containsKey("execUser")){
            sql = sql + String.format(" and user_name like '%%%s%%'",params.get("execUser"));
        }
        if (params.containsKey("testItem")){
            Long testItem = Long.parseLong(params.get("testItem").toString());
            query = entityManager.createNativeQuery(String.format("SELECT getChildList(%s)",testItem));
            Object obj = query.getSingleResult();
            String result = obj.toString();
            if(!result.isEmpty()){
                sql = sql + String.format(" and item_id in (%s)",result.substring(1));
            }
        }
        System.out.println(sql);
        query = entityManager.createNativeQuery(sql + " order by begin_time DESC ");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> list = null;
        if (p != null) {
            int pageNumber = page.getPageNumber();
            int pageSize = page.getPageSize();
            list = query.setFirstResult((pageNumber-1)*pageSize).setMaxResults(page.getPageSize()).getResultList();
        } else {
            list = query.getResultList();
        }
        deleteBatch(list); //如果测试项或者项目已经删除则把批量执行记录删除，这个地方是过滤是否有测试项或者项目删除的记录
        if (p != null) {
            int pageNumber = page.getPageNumber();
            int pageSize = page.getPageSize();
            list = query.setFirstResult((pageNumber-1)*pageSize).setMaxResults(page.getPageSize()).getResultList();
        } else {
            list = query.getResultList();
        }
            for (Map<String,Object> row:list){
                if("project".equals(row.get("item_type"))){
                    TestProject project = testProjectDao.findOne(Long.parseLong(row.get("item_id").toString()));
                    row.put("itemName", project.getName());
                } else {
                    StringBuffer sb = new StringBuffer("");
                    NxyFuncItem item = nxyFuncItemDao.findOne(Long.parseLong(row.get("item_id").toString()));
                    getItemName(item, sb);

                    row.put("itemName", sb.toString());
                }
                row.put("begin_time", row.get("begin_time").toString().substring(0, 19));
                if("starting".equals(row.get("status").toString())){
                    Long batchId = Long.parseLong(row.get("id").toString());
                    boolean flag = funcUsecaseService.batch(batchId, Integer.valueOf(row.get("uc_count").toString()));
                    NxyUsecaseExecBatch batch = batchDao.findOne(batchId);
                    row.put("uc_error_count", batch.getUcErrorCount());
                    row.put("uc_succ_count", batch.getUcSuccCount());
                    row.put("rate", batch.getRate());
                    if("starting".equals(batch.getStatus()) && !flag){
                        row.put("status", "执行中");
                    } else if("starting".equals(batch.getStatus()) && flag) {
                        batch.setStatus("fail");
                        batchDao.save(batch);
                        row.put("status", "执行失败");
                    } else {
                        row.put("status", "执行完成");
                    }
                } else if("finished".equals(row.get("status").toString())) {
                    row.put("status", "执行完成");
                } else {
                    row.put("status", "执行失败");
                }
            }
        sql = sql.replaceFirst("select \\*","select count(1)");
        query = entityManager.createNativeQuery(sql);
        Object o = query.getSingleResult();
        String total = o.toString();
        Map<String, Object> ret = new HashMap<>();
        ret.put("rows", list);
        ret.put("total",total);
        return ret;
    }

    private void deleteBatch(List<Map<String,Object>> list){
        for (Map<String, Object> row:list){
            if("project".equals(row.get("item_type"))){
                TestProject project = testProjectDao.findOne(Long.parseLong(row.get("item_id").toString()));
                if(project == null){
                    batchDao.delete(Long.parseLong(row.get("id").toString()));
                }
            } else {
                NxyFuncItem item = nxyFuncItemDao.findOne(Long.parseLong(row.get("item_id").toString()));
                if(item == null){
                    batchDao.delete(Long.parseLong(row.get("id").toString()));
                }
            }
        }
    }

    private void getItemName(NxyFuncItem item, StringBuffer sb){
        sb.append(item.getName() + "<-");
        if(item.getParentId() == null){
            sb.append(item.getTestProject().getName());
        } else {
            NxyFuncItem item1 = nxyFuncItemDao.findOne(item.getParentId());
            getItemName(item1, sb);
        }
    }

    @RequestMapping(value = "/execList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getUsecase(@RequestParam Map<String, Object> params, @PageableDefault Pageable page){
        Pageable p = page;
        if (page != null) {
            p = new PageRequest(page.getPageNumber() < 1 ? 0 : page.getPageNumber() - 1, page.getPageSize(), page.getSort());
        }
        Map<String, Object> ret = new HashMap<>();
        Object result = params.get("batchId");
        javax.persistence.Query query;
        String sql = String.format("select u.*,c.result as result1,c.id as id1,c.descript from nxy_func_usecase u right join nxy_func_usecase_exec c on u.id = c.usecase_id where c.batch_id = %s ",result);
        if (params.containsKey("no|like")){
            sql = sql + String.format(" and u.no like '%%%s%%'",params.get("no|like"));
        }
        if (params.containsKey("caseNumber|like")){
            sql = sql + String.format(" and u.case_number like '%%%s%%'",params.get("caseNumber|like"));
        }
        if (params.containsKey("purpose|like")){
            sql = sql + String.format(" and u.purpose like '%%%s%%'",params.get("purpose|like"));
        }
        if (params.containsKey("result")){
            sql = sql + String.format(" and c.result = '%s'",params.get("result"));
        }
        if(params.containsKey("search")){
            String search = params.get("search").toString();
            if("succ".equals(search)){
                sql += " and c.result = 'expected' ";
            } else {
                sql += " and c.result != 'expected' ";
            }
        }
        query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> list = null;
        if (page != null) {
            int pageNumber = p.getPageNumber();
            int pageSize = p.getPageSize();
            list = query.setFirstResult((pageNumber)*pageSize).setMaxResults(page.getPageSize()).getResultList();
        } else {
            list= query.getResultList();
        }
        for (Map<String,Object> row:list){
            SysCode code = sysCodeDao.findByCategoryAndKey("UC_RESULT",row.get("result1").toString());
            row.put("resultDis",code.getValue());
            row.put("id", row.get("id1"));
            row.put("result", row.get("result1").toString());
        }
        sql = sql.replaceFirst("select u\\.\\*,c\\.result as result1,c\\.id as id1,c\\.descript","select count(1)");
        query = entityManager.createNativeQuery(sql);
        Object o = query.getSingleResult();
        String total = o.toString();
        ret.put("rows", list);
        ret.put("total",total);
        return ret;
    }

    private void getItemNameNew(NxyFuncItem item, StringBuffer sb){
        sb .append(item.getName() + "-") ;
        if(item.getParentId() == null){
            sb.append(item.getTestProject().getName() + "-");
        } else {
            NxyFuncItem item1 = nxyFuncItemDao.findOne(item.getParentId());
            getItemNameNew(item1, sb);
        }
    }

    @RequestMapping(value = "/exportExcel")
    public void exportExcel(String ids, HttpServletResponse response){
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String[] headers = {"案例编号", "用例标识", "用例目的", "用例步骤", "预期结果", "报文标识号", "发送数据", "接收数据", "执行结果", "失败原因"};
            List<Map<String, Object>> data = new ArrayList<>();
            String[] idStrs = ids.split(",");
            String title = "";
            for(String idStr : idStrs){
                Long id = Long.parseLong(idStr);
                Map<String, Object> map = new HashMap<>();
                NxyUsecaseExecBatch batch = batchDao.findOne(id);
                if("project".equals(batch.getItemType())){
                    TestProject project = testProjectDao.findOne(batch.getItemId());
                    title = project.getName();
                } else {
                    StringBuffer sb = new StringBuffer("");
                    NxyFuncItem item = nxyFuncItemDao.findOne(batch.getItemId());
//                    getItemName(item, sb);
                    getItemNameNew(item, sb);
                    String[] arr = sb.toString().split("-");
                    sb = new StringBuffer("");
                    for (int i=arr.length-1; i>=0; i--){
                        String str = arr[i];
                        if(!str.isEmpty()){
                            sb.append(str + "-");
                        }
                    }
                    title = sb.toString();
                }
                title = title + "-" + sf.format(batch.getBeginTime());
                List<NxyFuncUsecaseExcel> usecaseExcels = new ArrayList();
                javax.persistence.Query query;
                String sql = String.format("select u.*,c.result as result1,c.id as id1 from nxy_func_usecase u right join nxy_func_usecase_exec c on u.id = c.usecase_id where c.batch_id = %s ",id);
                query = entityManager.createNativeQuery(sql);
                query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List<Map<String,Object>> list = query.getResultList();
                for (Map<String,Object> row:list){
                    NxyFuncUsecaseExcel excel = new NxyFuncUsecaseExcel();
                    excel.setCaseNumber(row.get("case_number") == null ? "" : (String) row.get("case_number"));
                    excel.setNo((String) row.get("no"));
                    excel.setPurpose(row.get("purpose") == null ? "" : (String) row.get("purpose"));
                    excel.setStep(row.get("step") == null ? "" : (String) row.get("step"));
                    excel.setExpected(row.get("expected") == null ? "" : (String) row.get("expected"));
                    List<NxyFuncUsecaseExecSend> sendList = nxyFuncUsecaseExecSendDao.findByExecId(Long.parseLong(row.get("id1").toString()));
                    NxyFuncUsecaseExecSend send = sendList.get(sendList.size()-1);
                    String sendMsg = send.getMsg() == null ? "" : send.getMsg();
                    excel.setSendMsg(sendMsg);
                    excel.setMsgId(send.getMsgId() == null? "" : send.getMsgId());
                    List<NxyFuncUsecaseExecRecv> recvList = recvDao.findBySendId(send.getId());
                    if(recvList.isEmpty()){
                        excel.setRecvMsg("");
                    } else {
                        NxyFuncUsecaseExecRecv recv = recvList.get(recvList.size()-1);
                        excel.setRecvMsg(recv.getMessageMessage());
                    }
                    String result = (String) row.get("result1");
                    if("expected".equals(result)){
                        excel.setResult("通过");
                    } else {
                        SysCode code = sysCodeDao.findByCategoryAndKey("UC_RESULT",row.get("result1").toString());
                        excel.setResult(code.getValue());
                        excel.setRemark(send.getResultDescript());
                    }
                    usecaseExcels.add(excel);
                }
                NxyFuncUsecaseExcel excel = new NxyFuncUsecaseExcel();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                excel.setCaseNumber("执行时间：" + format.format(batch.getBeginTime()) + ", 执行速率" +
                        "：" + batch.getExecSpeed() + "秒，总数：" + batch.getUcCount() +
                        "，通过数：" + batch.getUcSuccCount() + "，失败数：" + batch.getUcErrorCount() +
                        "，通过率：" + batch.getRate());
                excel.setNo("");
                excel.setResult("");
                excel.setPurpose("");
                excel.setStep("");
                excel.setExpected("");
                excel.setMsgId("");
                excel.setRecvMsg("");
                excel.setSendMsg("");
                excel.setRemark("");
                usecaseExcels.add(excel);
                map.put("title", title);
                map.put("dataset", usecaseExcels);
                data.add(map);
            }

            response.reset();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("UTF-8");
            String fileDisplay = new String((title + ".xls").getBytes("GB2312"), "iso8859-1");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileDisplay);
            // 输出资源内容到相应对象
            ExcelUtils excelUtils = new ExcelUtils();
            excelUtils.exportExcels(headers, data, response.getOutputStream());
            logger.info("@K|true|导出成功！");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result del(Long id){
        Result ret = new Result();
        try {
            batchDao.delete(id);
            nxyFuncUsecaseExecDao.deleteByBatchId(id);
        } catch (Exception e){
            e.printStackTrace();
            ret.setMsg("删除失败");
            ret.setSuccess(false);
        }
        return ret;
    }

    @RequestMapping(value = "/dels", method = RequestMethod.POST)
    @ResponseBody
    public Result dels(@RequestParam("ids[]")Long[] ids){
        Result ret = new Result();
        try {
            for(Long id : ids){
                nxyFuncUsecaseExecDao.deleteByUsecaseId(id);
            }
        } catch (Exception e){
            e.printStackTrace();
            ret.setMsg("删除失败");
            ret.setSuccess(false);
        }
        return ret;
    }

    @RequestMapping(value = "/uploadData")
    public void uploadData(Long id, HttpServletResponse response){
        try {
            java.util.Date now = new java.util.Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dates = sf.format(now);
            String[] headers = {"发送报文", "报文标识号", "发送数据", "接收报文", "接收数据", "执行结果", "失败原因"};
            List<NxyFuncUsecaseExecExcel> usecaseExcels = new ArrayList();
            NxyFuncUsecaseExec exec = nxyFuncUsecaseExecDao.findOne(id);
            String title = exec.getNxyFuncUsecase().getCaseNumber();
            List<NxyFuncUsecaseExecSend> sendList = nxyFuncUsecaseExecSendDao.findByExecId(id);
            for (NxyFuncUsecaseExecSend send : sendList){
                NxyFuncUsecaseExecExcel excel = new NxyFuncUsecaseExecExcel();
                String sendMsg = send.getMsg() == null ? "" : send.getMsg();
                excel.setSendCode(send.getMessageCode());
                excel.setSendMsg(sendMsg);
                excel.setMsgId(send.getMsgId() == null? "" : send.getMsgId());
                List<NxyFuncUsecaseExecRecv> recvList = recvDao.findBySendId(send.getId());
                if(recvList.isEmpty()){
                    excel.setRecvMsg("");
                } else {
                    NxyFuncUsecaseExecRecv recv = recvList.get(recvList.size()-1);
                    excel.setRecvMsg(recv.getMessageMessage());
                    excel.setRecvCode(recv.getMessageCode());
                }
                String result = send.getResult();
                if("expected".equals(result)){
                    excel.setResult("通过");
                } else {
                    SysCode code = sysCodeDao.findByCategoryAndKey("UC_RESULT",result);
                    excel.setResult(code.getValue());
                    excel.setRemark(send.getResultDescript());
                }
                usecaseExcels.add(excel);
            }
            response.reset();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("UTF-8");
            String fileDisplay = new String(("案例单轮次执行结果" + dates + ".xls").getBytes("GB2312"), "iso8859-1");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileDisplay);
            // 输出资源内容到相应对象
            ExcelUtils excelUtils = new ExcelUtils();
            excelUtils.exportExcel(title, headers, usecaseExcels, response.getOutputStream());
            logger.info("@K|true|导出成功！");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
