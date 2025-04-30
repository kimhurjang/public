package com.example.mhbc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping({"/" , "/home"})
    public String index(){
        System.out.println(">>>>>>>>>>index page<<<<<<<<<<");
        return "index";
    }
    @RequestMapping("/admin")
    public String admin(){
        System.out.println(">>>>>>>>>>admin page<<<<<<<<<<");
        return "admin";
    }


}
