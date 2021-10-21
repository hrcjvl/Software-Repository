package com.bootdo.importdata.controller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: liyh
 * @Date: 2020/10/23 17:33
 */

@Controller
public class IndexController {

    @RequestMapping("/upload")
    public String index()
    {
        return "index";
    }
}
