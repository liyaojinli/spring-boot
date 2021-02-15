package org.liyaojin.springboot.web;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/web", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WebController {


    @GetMapping(value = "/test")
    public List<String> test() {
        List<String> list = new ArrayList();
        list.add("这是测试");
        return list;
    }
}
