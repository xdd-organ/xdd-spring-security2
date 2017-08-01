package com.hp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "demo")
public class DemoController {
    @RequestMapping(value = "test1")
    @ResponseBody
    public String test1() {
        return "hello1";
    }

    @RequestMapping(value = "login")
    public String test2() {
        return "login";
    }
}
