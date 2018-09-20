package com.zdtech.platform.web.controller.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangbo on 2018/7/11.
 */
@Controller
@RequestMapping(value = "/test/file")
public class TestProjectFileController {
    @RequestMapping(value = "/list")
    public String showList(){
        return "test/file/file-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "test/file/file-add";
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> query(){
        Map<String,Object> map = new HashMap<>();
        return map;
    }
}
