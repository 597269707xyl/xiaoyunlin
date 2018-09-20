package com.zdtech.platform.web.controller.funcexec;

import com.zdtech.platform.framework.entity.NxyAsyncNotice;
import com.zdtech.platform.framework.repository.NxyAsyncNoticeDao;
import com.zdtech.platform.framework.utils.XmlDocHelper;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by lyj on 2018/4/10.
 */
@Controller
@RequestMapping("/func/asyncNotice")
public class NxyAsyncNoticeController {

    @Autowired
    private NxyAsyncNoticeDao asyncNoticeDao;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String tolist(Model model) {
        return "/funcexec/asyncnotice/async-notice-list";
    }

    @RequestMapping(value = "/dataPage", method = RequestMethod.GET)
    public String dataPage(Model model, Long id) {
        NxyAsyncNotice nxyAsyncNotice = asyncNoticeDao.findOne(id);
        String msg = nxyAsyncNotice.getMsg();
        try {
            Document document = XmlDocHelper.getXmlFromStr(msg);
            OutputFormat formater=OutputFormat.createPrettyPrint();
            formater.setEncoding("UTF-8");
            Writer out = new StringWriter();
            XMLWriter writer=new XMLWriter(out,formater);
            writer.write(document);
            writer.close();
            msg = out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("msg", msg);
        return "/funcexec/asyncnotice/data";
    }
}
