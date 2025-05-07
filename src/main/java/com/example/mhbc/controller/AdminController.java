package com.example.mhbc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @RequestMapping("/admin_board")
    public String admin_board(){


        return "/admin_board";
    }
}
