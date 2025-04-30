package com.example.mhbc.controller;

import com.example.mhbc.dto.CommentsDTO;
import com.example.mhbc.entity.BoardEntity;
import com.example.mhbc.entity.BoardGroupEntity;
import com.example.mhbc.entity.MemberEntity;
import com.example.mhbc.repository.*;
import com.example.mhbc.service.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
    private MemberRepository memberRepository;

    @RequestMapping({"/" , "/home"})
    public String index(Model model){
        System.out.println(">>>>>>>>>>index page<<<<<<<<<<");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userid = auth.getName();

        MemberEntity member = memberRepository.findByUserid(userid);

        model.addAttribute("member", member);
        model.addAttribute("title", "만화방초");//페이지별 타이틀 설정.(디폴트값==기본 제목)
        return "index";
    }

    @RequestMapping("/admin")
    public String admin(Model model,
                        @RequestParam("grade") int grade){
        System.out.println(">>>>>>>>>>admin page<<<<<<<<<<");

        /*권한 체크*/
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if(grade == 10 && isAdmin == true){
            return "/admin/admin";
        }else{
            return "/";
        }

    }




}
