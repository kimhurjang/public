package com.example.mhbc.controller;

import com.example.mhbc.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.mhbc.repository.MemberRepository;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/member")
public class MemberController {

  private final MemberRepository memberRepository;

  @RequestMapping("/login")
  public String login(){
    System.out.println(">>>>>>>>>>login page<<<<<<<<<<");
    return "member/login";
  }

  @RequestMapping("/join")
  public String join(){
    System.out.println(">>>>>>>>>>join page<<<<<<<<<<");
    return  "member/join";
  }
  
  @PostMapping("/join")
  public String joinProc(@RequestParam ("userid") String userid,
                         @RequestParam ("pwd") String pwd,
                         @RequestParam ("name") String name,
                         @RequestParam ("telecom") String telecom,
                         @RequestParam ("email") String email,
                         @RequestParam ("mobile1") String mobile1,
                         @RequestParam ("mobile2") String mobile2,
                         @RequestParam ("mobile3") String mobile3
                         ){
    String mobile = mobile1 + "-" + mobile2 + "-" + mobile3;

    MemberDTO memberDTO = MemberDTO.builder()
            .userid(userid)
            .pwd(pwd)
            .name(name)
            .telecom(telecom)
            .email(email)
            .mobile(mobile)
            .grade(1)
            .status("정상")
            .build();

    memberRepository.save(memberDTO.toEntity());

    return "redirect:/api/member/login";

  }
  

  @RequestMapping("/findidpw")
  public String findidpw(){
    System.out.println(">>>>>>>>>>findidpw page<<<<<<<<<<");
    return  "member/findidpw";
  }

  @RequestMapping("/adminuser")
  public String adminuser(){
    System.out.println(">>>>>>>>>>adminuser page<<<<<<<<<<");
    return  "member/adminuser";
  }

  @RequestMapping("/adminuserinfo")
  public String adminuserinfo(){
    System.out.println(">>>>>>>>>>adminuserinfo page<<<<<<<<<<");
    return  "member/adminuserinfo";
  }



}
