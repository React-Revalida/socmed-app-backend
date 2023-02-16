package org.ssglobal.revalida.codes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/api")
public class HomeController {
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("hello")
    @ResponseBody
    public String index() {
        LOG.warn("Hello World!");
        return "Hello World!";
    }

}
