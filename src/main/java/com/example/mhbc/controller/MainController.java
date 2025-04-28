package com.example.mhbc.controller;

import com.example.mhbc.dto.CommentsDTO;
import com.example.mhbc.entity.BoardEntity;
import com.example.mhbc.entity.BoardGroupEntity;
import com.example.mhbc.repository.AttachmentRepository;
import com.example.mhbc.repository.BoardGroupRepository;
import com.example.mhbc.repository.BoardRepository;
import com.example.mhbc.repository.CommentsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainController {

    private AttachmentRepository attachmentRepository;
    private BoardGroupRepository boardGroupRepository;
    private BoardRepository boardRepository;
    private CommentsRepository commentsRepository;

    @RequestMapping({"/" , "/home"})
    public String index(Model model){
        System.out.println(">>>>>>>>>>index page<<<<<<<<<<");
        model.addAttribute("title", "만화방초");//페이지별 타이틀 설정.(디폴트값==기본 제목)
        return "index";
    }

    @RequestMapping("/admin")
    public String admin(){
        System.out.println(">>>>>>>>>>admin page<<<<<<<<<<");
        return "admin";
    }
    @RequestMapping("/gallery")
    public String gallery(){
        System.out.println(">>>>>>>>>>gallery page<<<<<<<<<<");
        return "board/gallery";
    }



}
