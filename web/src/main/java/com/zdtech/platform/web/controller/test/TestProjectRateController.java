package com.zdtech.platform.web.controller.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lyj on 2018/7/11.
 */
@Controller
@RequestMapping(value = "/test/rate")
public class TestProjectRateController {

    @RequestMapping(value = "/list")
    public String showList(){
        return "test/rate/rate-query";
    }
}
