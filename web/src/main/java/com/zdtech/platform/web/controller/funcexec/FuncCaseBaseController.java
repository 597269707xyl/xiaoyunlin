package com.zdtech.platform.web.controller.funcexec;

import com.zdtech.platform.framework.entity.NxyFuncUsecase;
import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.framework.entity.SysConf;
import com.zdtech.platform.framework.repository.NxyFuncUsecaseDao;
import com.zdtech.platform.framework.repository.SysConfDao;
import com.zdtech.platform.service.funcexec.FuncCasebaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by lyj on 2018/7/11.
 */
@Controller
@RequestMapping("/casebase")
public class FuncCaseBaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private NxyFuncUsecaseDao nxyFuncUsecaseDao;
    @Autowired
    private FuncCasebaseService funcCasebaseService;
    @Autowired
    private SysConfDao confDao;

    private String getToolType(){
        SysConf conf = confDao.findByCategoryAndKey("SIMULATOR_SERVER", "TOOL_TYPE");
        if(conf != null){
            return conf.getKeyVal();
        }
        return "atsp";
    }

    @RequestMapping(value = "/list")
    public String list(Model model){
        model.addAttribute("toolType", getToolType());
        return "/funcexec/casebase/func-case-base-list";
    }

    @RequestMapping(value = "/marklist")
    public String marklist(Model model){
        model.addAttribute("toolType", getToolType());
        return "/funcexec/casebase/func-case-mark-list";
    }

    @RequestMapping(value = "/backup")
    public String backup(){
        return "/funcexec/casebase/backup";
    }

    /**
     * 添加或修改功能测试项页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/addItemPage", method = RequestMethod.GET)
    public String addItemPage(Model model, String type, String mark){
        model.addAttribute("type", type);
        model.addAttribute("mark", mark);
        return "/funcexec/casebase/func-item-add";
    }

    /**
     * 添加用例页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/addUsecasePage/{itemId}", method = RequestMethod.GET)
    public String addUsecasePage(Model model, @PathVariable("itemId") Long itemId){
        model.addAttribute("itemId", itemId);
        model.addAttribute("toolType", getToolType());
        return "/funcexec/casebase/func-usecase-add";
    }

    @RequestMapping(value = "/addUsecase", method = RequestMethod.POST)
    @ResponseBody
    public Result addUsecase(NxyFuncUsecase usecase,  Long itemId) {
        Result result = null;
        try {
            if(usecase.getId() != null){
                NxyFuncUsecase nxyFuncUsecase = nxyFuncUsecaseDao.findOne(usecase.getId());
                nxyFuncUsecase.setSeqNo(usecase.getSeqNo());
                nxyFuncUsecase.setStep(usecase.getStep());
                nxyFuncUsecase.setPurpose(usecase.getPurpose());
                nxyFuncUsecase.setExpected(usecase.getExpected());
                nxyFuncUsecase.setCaseNumber(usecase.getCaseNumber());
                nxyFuncUsecaseDao.save(nxyFuncUsecase);
                result = new Result(true, "");
            } else {
                result = funcCasebaseService.addUsecase(usecase, itemId);
            }
            logger.info("@A|true|添加功能测试用例成功!");
        } catch (Exception e) {
            logger.error("添加功能测试用例失败！", e);
            result = new Result(false, "");
        }

        return result;
    }

    @RequestMapping(value = "/copyPage", method = RequestMethod.GET)
    public String copyUsecasePage() {
        return "/funcexec/casebase/copy-select";
    }

    /**
     * 复制测试用例
     * @param usecaseIds 复制数据
     * @param id  目标Id
     * @return
     */
    @RequestMapping(value = "/usecase/copy", method = RequestMethod.POST)
    @ResponseBody
    public Result usecasecopy(@RequestParam("usecaseIds[]") Long[] usecaseIds, @RequestParam("id") Long id, Long instanceId) {
        Result ret = new Result();
        try{
            ret = funcCasebaseService.copyUsecase(usecaseIds, id, instanceId);
        } catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg(e.getMessage());
        }
        return ret;
    }

    /**
     * 复制测试项
     * @param itemIds 复制数据
     * @param id  目标Id
     * @return
     */
    @RequestMapping(value = "/item/copy", method = RequestMethod.POST)
    @ResponseBody
    public Result copy(@RequestParam("itemIds[]") Long[] itemIds, @RequestParam("id") Long id, @RequestParam("type") String type, Long instanceId) {
        Result ret = new Result();
        try{
            ret = funcCasebaseService.copyItem(itemIds, id, type, instanceId);
        } catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg(e.getMessage());
        }
        return ret;
    }

    @RequestMapping(value = "/synchrodata", method = RequestMethod.POST)
    @ResponseBody
    public Result synchrodata(@RequestParam("ids[]") Long[] usecaseIds, String mark) {
        Result ret = new Result();
        try{
            ret = funcCasebaseService.synchrodata(usecaseIds, mark);
        } catch (Exception e){
            e.printStackTrace();
            ret.setSuccess(false);
            ret.setMsg("同步数据失败!");
        }
        return ret;
    }
}
