package com.example.mhbc.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/board")
@RequiredArgsConstructor
public class AdminBoardController {

    @RequestMapping("/admin_now_board")
    public String admin_now_board(){


        return "/admin/admin_now_board";
    }
    @RequestMapping("/admin_manage_board")
    public String admin_manage_board(){


        return "/admin/admin_manage_board";
    }

}
