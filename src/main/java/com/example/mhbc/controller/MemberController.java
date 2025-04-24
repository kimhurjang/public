package com.example.mhbc.controller;

import com.example.mhbc.dto.MemberDTO;
import com.example.mhbc.dto.SocialUserInfoDTO;
import com.example.mhbc.entity.MemberEntity;
import com.example.mhbc.entity.SnsEntity;
import com.example.mhbc.repository.MemberRepository;
import com.example.mhbc.repository.SnsRepository;  // SnsRepository import 추가
import com.example.mhbc.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/member")
public class MemberController {

  private final MemberRepository memberRepository;
  private final KakaoService kakaoService;
  private final SnsRepository snsRepository;  // SnsRepository 주입

  // 일반 로그인 페이지
  @RequestMapping("/login")
  public String login() {
    System.out.println(">>>>>>>>>>login page<<<<<<<<<<");
    return "member/login";
  }

  // 카카오 로그인 처리 (인가 코드 받는 엔드포인트)
  @GetMapping("/kakao")
  public String sociallogin(@RequestParam("code") String code, Model model) {
    System.out.println("받은 인가 코드: " + code);
    String accessToken = kakaoService.getKakaoAccessToken(code);
    SocialUserInfoDTO userInfo = kakaoService.getUserNickname(accessToken);

    if (userInfo == null || userInfo.getEmail() == null) {
      System.out.println("사용자 정보를 가져오지 못했습니다.");
      return "redirect:/api/member/login";  // 로그인 페이지로 리디렉션
    }

    // SNS 정보가 이미 존재하는지 체크 (SNS ID로만 중복 체크)
    Optional<SnsEntity> existingSnsUser = snsRepository.findBySnsId(userInfo.getUserid());

    if (existingSnsUser.isEmpty()) {
      // SNS 테이블에 저장 (중복되지 않으면 저장)
      SnsEntity snsUser = new SnsEntity();
      snsUser.setSnsType("KAKAO");
      snsUser.setSnsId(userInfo.getUserid());
      snsUser.setSnsEmail(userInfo.getEmail());
      snsUser.setSnsName(userInfo.getNickname());
      snsUser.setConnectedAt(new java.sql.Timestamp(System.currentTimeMillis()));
      snsRepository.save(snsUser);
      model.addAttribute("userInfo", userInfo);  // userInfo 모델에 추가 (필요시 사용)
    } else {
      System.out.println("이미 가입된 사용자입니다.");
    }

    // 메인 페이지로 리디렉션
    return "redirect:/";  // 메인 페이지로 리디렉션
  }



  // 회원가입 페이지
  @RequestMapping("/join")
  public String join() {
    System.out.println(">>>>>>>>>>join page<<<<<<<<<<");
    return "member/join";
  }

  // 회원가입 처리
  @PostMapping("/join")
  public String joinProc(@RequestParam("userid") String userid,
                         @RequestParam("pwd") String pwd,
                         @RequestParam("name") String name,
                         @RequestParam("telecom") String telecom,
                         @RequestParam("email") String email,
                         @RequestParam("mobile1") String mobile1,
                         @RequestParam("mobile2") String mobile2,
                         @RequestParam("mobile3") String mobile3) {
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

    // 중복 체크를 추가하여 처리
    if (!memberRepository.existsByUserid(userid)) {
      memberRepository.save(memberDTO.toEntity());
    } else {
      System.out.println("이미 가입된 사용자입니다.");
      return "redirect:/api/member/join";  // 이미 존재하는 경우 회원가입 페이지로 리디렉션
    }

    return "redirect:/api/member/login";
  }

  @RequestMapping("/findidpw")
  public String findidpw() {
    System.out.println(">>>>>>>>>>findidpw page<<<<<<<<<<");
    return "member/findidpw";
  }

  @RequestMapping("/adminuser")
  public String adminuser() {
    System.out.println(">>>>>>>>>>adminuser page<<<<<<<<<<");
    return "member/adminuser";
  }

  @RequestMapping("/adminuserinfo")
  public String adminuserinfo() {
    System.out.println(">>>>>>>>>>adminuserinfo page<<<<<<<<<<");
    return "member/adminuserinfo";
  }
}
