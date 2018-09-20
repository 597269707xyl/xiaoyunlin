package com.zdtech.platform.web.controller.system;

import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.*;
import com.zdtech.platform.framework.service.GenericService;
import com.zdtech.platform.service.adapter.AdapterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by huangbo on 2018/7/10.
 */
@Controller
@RequestMapping(value = "/sys/adapter")
public class AdapterController {
    @Autowired
    private SysAdapterDao sysAdapterDao;
    @Autowired
    private GenericService genericService;
    @Autowired
    private SysAdapterConfDao sysAdapterConfDao;
    @Autowired
    private SysAdapterQueueDao sysAdapterQueueDao;
    @Autowired
    private SimSystemInstanceDao instanceDao;
    @Autowired
    private AdapterService adapterService;

    @RequestMapping(value = "/list")
    public String showList() {
        return "system/adapter/adapter-list";
    }

    @RequestMapping(value = "/add/{systemId}", method = RequestMethod.GET)
    public String add(@PathVariable("systemId") String systemId) {
        if (systemId.equals("MQ")) {
            return "system/adapter/adapterMq-add";
        } else if (systemId.equals("TCP")) {
            return "system/adapter/adapterTcp-add";
        } else {
            return "system/adapter/adapterHttp-add";
        }
    }

    @RequestMapping(value = "/add/{adapterType}", method = RequestMethod.POST)
    @ResponseBody
    public Result addAdapter(SysAdapter sysAdapter, SysAdapterHttp sysAdapterHttp, SysAdapterMq sysAdapterMq, SysAdapterTcp sysAdapterTcp, @PathVariable("adapterType") String type,String sendQueue, String recvQueue) {
        Result result =adapterService.addAdapter(sysAdapter,sysAdapterHttp,sysAdapterMq,sysAdapterTcp,type,sendQueue,recvQueue);
        return result;
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> query(@RequestParam Map<String, Object> params,
                                     @PageableDefault Pageable page) {
        Pageable p = page;
        if (page != null) {
            p = new PageRequest(page.getPageNumber() < 1 ? 0 : page.getPageNumber() - 1, page.getPageSize(), page.getSort());
        }
        Map<String, Object> map = genericService.commonQuery("sysAdapter", params, p);
        return map;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result del(@RequestParam("ids[]") Long[] ids) {
        Result result = new Result();
        try {
            for (Long id : ids) {
                sysAdapterDao.delete(id);
                result.setSuccess(true);
            }
        } catch (Exception e) {
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value = "/get/{id}")
    @ResponseBody
    public SysAdapter edit(@PathVariable("id") Long id) {
        SysAdapter sysAdapter = sysAdapterDao.findOne(id);
        String type = sysAdapter.getAdapterType();
        Map<String, Object> map = new HashMap<>();
        Class clazz = sysAdapter.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(sysAdapter));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sysAdapter;
    }

    @RequestMapping(value = "/paramListData/{id}")
    @ResponseBody
    public List<SysAdapterConf> paramListData(@PathVariable("id") Long adapterId) {
        List<SysAdapterConf> list = sysAdapterConfDao.findBySimAdapterId(adapterId);
        return list;
    }

    @RequestMapping(value = "/saveParam", method = RequestMethod.POST)
    @ResponseBody
    public Result saveParam(SysAdapterConf conf) {
        Result ret = new Result();
        try {
            sysAdapterConfDao.save(conf);
        } catch (Exception e) {
            ret.setMsg("操作失败");
            ret.setSuccess(false);
        }
        return ret;
    }

    @RequestMapping(value = "/deleteParam", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteParam(@RequestParam("ids[]") Long[] ids) {
        Result ret = new Result();
        try {
            sysAdapterConfDao.deleteByIds(Arrays.asList(ids));
        } catch (Exception e) {
            ret.setMsg("操作失败");
            ret.setSuccess(false);
        }
        return ret;
    }

    @RequestMapping(value = "/getSendQueue/{id}")
    @ResponseBody
    public List<SysAdapterQueue> getSendQueue(@PathVariable("id") Long id) {
        List<SysAdapterQueue> sendSimMqList = sysAdapterQueueDao.findByAdapterAndType(id, "send");
        return sendSimMqList;
    }

    @RequestMapping(value = "/getRecvQueue")
    @ResponseBody
    public List<SysAdapterQueue> getRecvQueue(@RequestParam(value = "id") Long id) {
        List<SysAdapterQueue> sendSimMqList = sysAdapterQueueDao.findByAdapterAndType(id, "recv");

        return sendSimMqList;
    }

    @RequestMapping(value = "/addQueueList")
    public String showQueue() {
        return "system/adapter/adapterMq-queue-add";
    }

    @RequestMapping(value = "/addQueue", method = RequestMethod.POST)
    @ResponseBody
    public Result addQueue(SysAdapterQueue sysAdapterQueue){
        Result result = new Result();
        sysAdapterQueueDao.save(sysAdapterQueue);
        return result;
    }

    @RequestMapping(value = "/getAdapterList", method = RequestMethod.POST)
    @ResponseBody
    public List<SysAdapter> getAdapterList(String type, Long instanceId){
        List<SysAdapter> list;
        if(StringUtils.isEmpty(type)){
            list = sysAdapterDao.findAll();
        } else {
            list = sysAdapterDao.getListByType(type);
        }
        List<Long> adapterIds;
        if(instanceId == null){
            adapterIds = instanceDao.findAdapterListDistinct();
        } else {
            adapterIds = instanceDao.findAdapterListByInstanceIdDistinct(instanceId);
        }
        if(adapterIds != null && adapterIds.size()>0){
            for(int i=list.size()-1; i>=0; i--){
                if(adapterIds.contains(list.get(i).getId())){
                    list.remove(i);
                }
            }
        }
        return list;
    }

    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    @ResponseBody
    public Result copy(@RequestParam("ids[]") Long[] ids) {
        Result result = adapterService.copyAdapter(ids);
        return result;
    }
}
