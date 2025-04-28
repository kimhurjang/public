package com.example.mhbc.controller;

import com.example.mhbc.entity.MemberEntity;
import com.example.mhbc.repository.MemberRepository;
import com.example.mhbc.service.KakaoService;
import com.example.mhbc.dto.SocialUserInfo;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/member")
@AllArgsConstructor
public class MemberController {

  private KakaoService kakaoService;
  private MemberRepository memberRepository;

  @RequestMapping("/login")
  public String login() {
    System.out.println(">>>>>>>>>>login page<<<<<<<<<<");
    return "member/login";
  }


  @RequestMapping("/join")
  public String join() {
    return "/member/join";
  }

  @PostMapping("/joinproc")
  public String joinproc(@RequestParam("name") String name,
                         @RequestParam("pwd") String pwd,
                         @RequestParam("userid") String userid,
                         Model model) {


    return "member/login";
  }

  @PostMapping("/sociallogin")
  public String kakaoCallback(@RequestParam String code, HttpSession session) {
    // 1. 액세스 토큰 받기
    String accessToken = kakaoService.getKakaoAccessToken(code);

    // 2. 사용자 정보 가져오기
    SocialUserInfo userInfo = kakaoService.getUserNickname(accessToken);

    // 3. DB에 유저 저장 or 조회
    MemberEntity member = memberRepository.findByEmail(userInfo.getEmail())
            .orElseGet(() -> {
              MemberEntity newMember = new MemberEntity();
              newMember.setEmail(userInfo.getEmail());
              newMember.setName(userInfo.getNickname());
              return memberRepository.save(newMember);
            });

    // ✅ 4. 세션에 memberIdx 저장
    session.setAttribute("memberIdx", member.getIdx());

    // 필요하면 nickname도 저장 가능
    session.setAttribute("nickname", member.getName());

    // 5. 리다이렉트
    return "redirect:/"; // 원하는 곳으로 보내면 됨
  }
}

