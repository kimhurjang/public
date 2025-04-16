package com.example.mhbc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

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

  @RequestMapping("/findidpw")
  public String findidpw(){
    System.out.println(">>>>>>>>>>findidpw page<<<<<<<<<<");
    return  "member/findidpw";
  }

}
