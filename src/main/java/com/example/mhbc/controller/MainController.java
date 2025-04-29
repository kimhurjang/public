package com.example.mhbc.controller;

import com.example.mhbc.dto.CommentsDTO;
import com.example.mhbc.entity.BoardEntity;
import com.example.mhbc.entity.BoardGroupEntity;
import com.example.mhbc.repository.AttachmentRepository;
import com.example.mhbc.repository.BoardGroupRepository;
import com.example.mhbc.repository.BoardRepository;
import com.example.mhbc.repository.CommentsRepository;
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

    @RequestMapping({"/" , "/home"})
    public String index(Model model){
        System.out.println(">>>>>>>>>>index page<<<<<<<<<<");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
            model.addAttribute("userid", user.getUsername());
        } else {
            model.addAttribute("userid", null);
        }

        model.addAttribute("title", "만화방초");//페이지별 타이틀 설정.(디폴트값==기본 제목)
        return "index";
    }

    @RequestMapping("/admin")
    public String admin(Model model){
        System.out.println(">>>>>>>>>>admin page<<<<<<<<<<");

        /*권한 체크*/
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String userid = auth.getName(); // 또는 ((User) auth.getPrincipal()).getUsername()
            boolean isAdmin = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals("ROLE_ADMIN"));

            model.addAttribute("userid", userid);
            model.addAttribute("isAdmin", isAdmin);
        } else {
            model.addAttribute("userid", null);
            model.addAttribute("isAdmin", false);
        }

/*
 *
 *                 관리자 전용 메뉴 html
 *                 <li th:if="${isAdmin}">
 *                 <a href="/admin" class="nav_item admin">
 *                 <span class="xi-cog"></span><br>
 *                 <span>관리자 페이지</span>
 *                 </a>
 *                 </li>
 *
 */

        return "admin";
    }
    @RequestMapping("/gallery")
    public String gallery(){
        System.out.println(">>>>>>>>>>gallery page<<<<<<<<<<");
        return "board/gallery";
    }



}
