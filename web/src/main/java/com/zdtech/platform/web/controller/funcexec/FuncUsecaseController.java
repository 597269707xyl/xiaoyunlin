package com.zdtech.platform.web.controller.funcexec;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.network.Client;
import com.zdtech.platform.framework.network.entity.Message;
import com.zdtech.platform.framework.network.entity.Request;
import com.zdtech.platform.framework.network.entity.SimAutoBatchStopMessage;
import com.zdtech.platform.framework.network.tcp.NettyClient;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.service.SysCodeService;
import com.zdtech.platform.framework.utils.XmlJsonHelper;
import com.zdtech.platform.service.funcexec.FuncExecService;
import com.zdtech.platform.service.funcexec.FuncUsecaseService;
import com.zdtech.platform.thread.BatchExecResultThread;
import com.zdtech.platform.utils.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/**
 * Created by yjli on 2017/9/7.
 */
@Controller
@RequestMapping("/func/usecase")
public class FuncUsecaseController {
    private Logger logger = LoggerFactory.getLogger(FuncUsecaseController.class);

    @Autowired
    private FuncUsecaseService funcUsecaseService;
    @Autowired
    private NxyFuncUsecaseExecDao nxyFuncUsecaseExecDao;
    @Autowired
    private NxyFuncUsecaseDao nxyFuncUsecaseDao;
    @Autowired
    private NxyFuncItemDao nxyFuncItemDao;
    @Autowired
    private NxyFuncUsecaseExecSendDao nxyFuncUsecaseExecSendDao;
    @Autowired
    private NxyFuncUsecaseExecRecvDao nxyFuncUsecaseExecRecvDao;
    @Autowired
    private FuncExecService funcExecService;
    @Autowired
    private SysCodeService sysCodeService;
    @Autowired
    private NxyUsecaseExecBatchDao nxyUsecaseExecBatchDao;
    @Autowired
    private TestProjectDao testProjectDao;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private SysCodeDao sysCodeDao;
    @Autowired
    private NxyFuncUsecaseExecRecvDao recvDao;
    public static ConcurrentMap<Long,BatchExecResultThread> batchMap = new ConcurrentHashMap<>();
    @Autowired
    private SysConfDao confDao;

    private String getToolType(){
        SysConf conf = confDao.findByCategoryAndKey("SIMULATOR_SERVER", "TOOL_TYPE");
        if(conf != null){
            return conf.getKeyVal();
        }
        return "atsp";
    }

    //功能自动化执行页面
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String tolist(Model model) {
        model.addAttribute("toolType", getToolType());
        return "/funcexec/usecase/func-usecase-list";
    }

    /**
     * 用例执行
     * @param o
     * @return
     */
    @RequestMapping(value = "/execUsecase")
    @ResponseBody
    public Result execUsecase(@RequestBody Map<String,Object> o) {
        Result result;
        try {
            List<Integer> ids = (List<Integer>)o.get("ids");
            List<Long> idList = new ArrayList<>();
            for(Integer id : ids){
                idList.add((long)id);
            }
            funcUsecaseService.sendExecMessages_bak(idList, null, null, 0L, "0");
            result = new Result(true, "");
            logger.info("@A|true|用例执行成功!");
        } catch (Exception e) {
            logger.error("用例执行失败！", e);
            result = new Result(false, e.getMessage());
        }
        return result;
    }

    /**
     * 缓冲时间页面
     * @return
     */
    @RequestMapping(value = "/sendInternal", method = RequestMethod.GET)
    public String sendInternal(){
        return "/funcexec/usecase/func-exec-send-internal";
    }


    @RequestMapping(value = "/stopBatch")
    @ResponseBody
    public Result stopBatch(Long batchId){
        Result ret = new Result();
        try{
            NxyUsecaseExecBatch batch = nxyUsecaseExecBatchDao.findOne(batchId);
            NxyFuncItem item = nxyFuncItemDao.findOne(batch.getItemId());
            SimSystemInstance ssi = item.getTestProject().getInstance();
            Client client = new NettyClient(ssi.getInsServerIp(),ssi.getInsServerPort());
            client.connect();
            SimAutoBatchStopMessage testMessage = new SimAutoBatchStopMessage(ssi.getId(), ssi.getAdapter().getId(), batchId);
            Message msg = testMessage;
            Request request = new Request(msg);
            client.send(request);
            client.close();
            batchMap.get(batchId).setStop(true);
            boolean flag = funcUsecaseService.setBatchExecSuccess(batchId);
            batchMap.remove(batchId);
            ret.setMsg("停止成功!");
        } catch (Exception e){
            e.printStackTrace();
            ret.setMsg("停止失败!");
            ret.setSuccess(false);
        }
        return ret;
    }

    /**
     * 测试项下所有用例执行
     * @param o
     * @return
     */
    @RequestMapping(value = "/execUsecaseProject")
    @ResponseBody
    public Result execUsecaseProject(@RequestBody Map<String,Object> o) {
        Result result;
        try {
            String type = o.get("type").toString();
            String id = o.get("id").toString();
            String sendInternalStr = o.get("sendInternal").toString();
            List<Long> ids = new ArrayList<>();
            if("project".equals(type)){
                Long projectId = Long.valueOf(id);
                ids.addAll(BigIntegerToLong(nxyFuncUsecaseDao.findByProjectIdAndMark(projectId)));
            } else {
                Long itemId = Long.valueOf(id);
                ids = getUsecaseIds(itemId, ids);
            }
            if(ids == null || ids.isEmpty()){
                result = new Result(false, "所选测试项目用例为空！");
                return result;
            }
            NxyFuncUsecase usecase = nxyFuncUsecaseDao.findOne(ids.get(0).longValue());
            Double sendInternalDouble = Double.parseDouble(sendInternalStr) * 1000;
            Long sendInternal = sendInternalDouble.longValue();
            Long batchId = funcUsecaseService.sendExecMessages_bak(ids, Long.parseLong(id), type, sendInternal, sendInternalStr);
            result = new Result(true, usecase.getNxyFuncItem().getId()+"");
            logger.info("@A|true|用例执行成功!");
            Map<String, Object> map = new HashMap<>();
            map.put("itemId", usecase.getNxyFuncItem().getId());
            NxyUsecaseExecBatch batch = nxyUsecaseExecBatchDao.findOne(batchId);
            map.put("execTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(batch.getBeginTime()));
            String timeout = sysCodeService.getConfValueByCategoryAndKey("SIMULATOR_SERVER", "NEPS_TIMEOUT");
            map.put("timeOut", formatTime(Long.parseLong(timeout)));
            map.put("batchId", batchId);
            result.setData(map);
            //统计
            if(batchId != null){
                BatchExecResultThread thread = new BatchExecResultThread(batchId, ids.size(), funcUsecaseService);
                thread.start();
            }
        } catch (Exception e) {
            logger.error("用例执行失败！", e);
            String msg = e.getMessage().equals("仿真实例未启动!") ? e.getMessage() : "用例执行失败！";
            result = new Result(false, msg);
        }
        return result;
    }

    private List<Long> BigIntegerToLong(List<BigInteger> ids){
        List<Long> idList = new ArrayList<>();
        for(BigInteger id : ids){
            idList.add(id.longValue());
        }
        return idList;
    }

    private List<Long> getUsecaseIds(Long itemId, List<Long> ids){
        ids.addAll(nxyFuncUsecaseDao.findByItemId(itemId));
        List<NxyFuncItem> items = nxyFuncItemDao.findByParentId(itemId);
        for(NxyFuncItem funcItem : items){
            getUsecaseIds(funcItem.getId(), ids);
        }
        return ids;
    }

    /**
     * 获取执行数据
     * @param id
     * @return
     */
    @RequestMapping(value = "/getUsecaseExec")
    @ResponseBody
    public List<NxyFuncUsecaseExec> getUsecaseExec(@RequestParam("id") Long id) {
        return nxyFuncUsecaseExecDao.findByUsecaseId(id);
    }

    /**
     * 测试数据列表
     * @param model
     * @return
     */
    @RequestMapping(value = "/getUsecaseExecList")
    public String execList(Model model){
        return "/funcexec/usecase/func-exec-list";
    }

    //发送报文列表
    @RequestMapping(value = "/tree")
    @ResponseBody
    public List<Map<String, Object>> tree(@RequestParam("id") Long id) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<NxyFuncUsecaseExecSend> sends = nxyFuncUsecaseExecSendDao.findByExecId(id);
        for(NxyFuncUsecaseExecSend send : sends){
            Map<String, Object> map = new HashMap<>();
            map.put("id", send.getId());
            map.put("messageName", send.getMessageName());
            map.put("messageCode", send.getMessageCode());
            map.put("resultDis", send.getResultDis());
            map.put("resultDescript", send.getResultDescript());
            list.add(map);
        }
        return list;
    }

    @RequestMapping(value = "/getExecIdByUsecaseId")
    @ResponseBody
    public Long getExecIdByUsecaseId(Long id){
        List<NxyFuncUsecaseExec> execs = nxyFuncUsecaseExecDao.findByUsecaseId(id);
        if(execs.isEmpty()) return null;
        return execs.get(0).getId();
    }

    //发送报文数据
    @RequestMapping(value = "/sendMsg")
    @ResponseBody
    public String sendMsg(Long id){
        NxyFuncUsecaseExecSend send = nxyFuncUsecaseExecSendDao.findOne(id);
        String ret = XmlJsonHelper.XmltoJson(send.getMsg());
        return ret;
    }

    //接收报文数据
    @RequestMapping(value = "/recvMsg")
    @ResponseBody
    public String recvMsg(Long id){
        String ret = "{\"total\":0\"rows\":[]}";
        List<NxyFuncUsecaseExecRecv> recvs = nxyFuncUsecaseExecRecvDao.findBySendId(id);
        if(!recvs.isEmpty()){
            ret = XmlJsonHelper.XmltoJson(recvs.get(recvs.size()-1).getMessageMessage());
        }
        return ret;
    }

    /**
     * 测试报告数据
     * @param params
     * @return
     */
    @RequestMapping(value = "/testresult", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> testresult(@RequestParam Map<String, Object> params){
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> result = funcExecService.testresult(params);
        map.put("status", result.get("status"));
        int successCount = Integer.parseInt(result.get("successCount").toString());
        int failCount = Integer.parseInt(result.get("failCount").toString());
        int allCount = Integer.valueOf(result.get("total").toString());
        map.put("allCount", allCount);
        map.put("successCount", successCount);
        map.put("failCount", failCount);
        String successRate = "0";
        if(successCount != 0){
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            successRate = numberFormat.format((float) successCount / (float) allCount * 100) + "%";
        }
        map.put("successRate", successRate);
        map.put("execTime", result.get("execTime"));
        String timeout = sysCodeService.getConfValueByCategoryAndKey("SIMULATOR_SERVER", "NEPS_TIMEOUT");
        map.put("timeOut", formatTime(Long.parseLong(timeout)));
        return map;
    }

    /*
     * 毫秒转化时分秒毫秒
    */
    private String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分钟");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
        if(milliSecond > 0) {
            sb.append(milliSecond+"毫秒");
        }
        return sb.toString();
    }

    /**
     * 根据功能测试项获取其用例列表数据
     * @param params
     * @param page
     * @return
     */
    @RequestMapping(value = "/getUsecaseByItem")
    @ResponseBody
    public Map<String, Object> getUsecaseByItem(@RequestParam Map<String, Object> params, @PageableDefault Pageable page) {
        Pageable p = page;
        if (page != null) {
            p = new PageRequest(page.getPageNumber() < 1 ? 0 : page.getPageNumber() - 1, page.getPageSize(), page.getSort());
        }
        if (params.size() > 3) {
            return funcExecService.getUsecase(params, p);
        }
        return null;
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

    private void getItemName(NxyFuncItem item, StringBuffer sb){
        sb.append(item.getName() + "<-");
        if(item.getParentId() == null){
            sb.append(item.getTestProject().getName());
        } else {
            NxyFuncItem item1 = nxyFuncItemDao.findOne(item.getParentId());
            getItemName(item1, sb);
        }
    }

    @RequestMapping(value = "/export")
    public void exportExcel(Long id, String type, HttpServletResponse response){
        try {
            String[] headers = {"案例编号", "用例标识", "用例目的", "用例步骤", "预期结果", "报文标识号", "发送数据", "接收数据", "执行结果", "失败原因"};
            List<Map<String, Object>> data = new ArrayList<>();
            String title = "";
            Map<String, Object> map = new HashMap<>();
            javax.persistence.Query query;
            List<Long> itemIdList = new ArrayList<>();
            if("project".equals(type)){
                TestProject project = testProjectDao.findOne(id);
                title = project.getName();
                List<NxyFuncItem> itemList = nxyFuncItemDao.findByProjectId(id);
                StringBuffer sb = new StringBuffer("");
                for(NxyFuncItem item : itemList){
                    itemIdList.add(item.getId());
                }
                if(itemIdList.isEmpty()){
                    itemIdList.add(0L);
                }
            } else {
                StringBuffer sb = new StringBuffer("");
                NxyFuncItem item = nxyFuncItemDao.findOne(id);
//                getItemName(item, sb);
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
                query = entityManager.createNativeQuery(String.format("SELECT getChildList(%s)",id));
                Object obj = query.getSingleResult();
                String rt = obj.toString();
                if(!rt.isEmpty()){
                    String[] arr1 = rt.split(",");
                    Pattern pattern = Pattern.compile("[0-9]*");
                    for(String str : arr1){
                        if(!str.isEmpty() && pattern.matcher(str).matches()){
                            itemIdList.add(Long.parseLong(str));
                        }
                    }
                }
                if(itemIdList.isEmpty()){
                    itemIdList.add(id);
                }
            }
            List<NxyFuncUsecaseExcel> usecaseExcels = new ArrayList();
            List<NxyFuncUsecase> usecaseList = nxyFuncUsecaseDao.findByItemIdList(itemIdList);
            for(NxyFuncUsecase usecase : usecaseList){
                NxyFuncUsecaseExcel excel = new NxyFuncUsecaseExcel();
                excel.setCaseNumber(usecase.getCaseNumber() == null ? "" : usecase.getCaseNumber());
                excel.setNo(usecase.getNo());
                excel.setPurpose(usecase.getPurpose() == null ? "" : usecase.getPurpose());
                excel.setStep(usecase.getStep() == null ? "" : usecase.getStep());
                excel.setExpected(usecase.getExpected() == null ? "" : usecase.getExpected());
                List<NxyFuncUsecaseExec> execList = nxyFuncUsecaseExecDao.findByUsecaseId(usecase.getId());
                if(execList != null && execList.size()>0){
                    List<NxyFuncUsecaseExecSend> sendList = nxyFuncUsecaseExecSendDao.findByExecId(execList.get(0).getId());
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
                    String result = execList.get(0).getResult();
                    if("expected".equals(result)){
                        excel.setResult("通过");
                    } else {
                        SysCode code = sysCodeDao.findByCategoryAndKey("UC_RESULT",result);
                        excel.setResult(code.getValue());
                        excel.setRemark(send.getResultDescript());
                    }
                } else {
                    excel.setSendMsg("");
                    excel.setMsgId("");
                    excel.setRecvMsg("");
                    excel.setResult("未执行");
                    excel.setRemark("");
                }
                usecaseExcels.add(excel);
            }
            map.put("title", title);
            map.put("dataset", usecaseExcels);
            data.add(map);

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
}
