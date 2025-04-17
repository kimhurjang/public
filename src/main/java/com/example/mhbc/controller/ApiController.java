package com.example.mhbc.controller;

import com.example.mhbc.repository.MemberRepository;  // MemberRepository import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/member")  // api/member로 기본 경로 설정
public class ApiController {

  @Autowired
  private MemberRepository memberRepository;  // Repository 주입

  @RequestMapping("/idcheck")
  @ResponseBody
  public String idcheck(@RequestParam("userid") String userid) {
    // 아이디 중복 확인

    // System.out.println("입력한 아이디: " + userid);
    // System.out.println("DB에 존재하나? " + memberRepository.existsByUserid(userid));

    boolean exists = memberRepository.existsByUserid(userid);
    if (exists) {
      // 이미 있음 → 중복
      return "Y";
    } else {
      // 없음 → 사용 가능
      return "N";
    }
  }
}
