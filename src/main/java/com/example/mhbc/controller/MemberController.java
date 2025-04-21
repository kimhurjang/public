package com.example.mhbc.controller;

import com.example.mhbc.entity.MemberEntity;
import com.example.mhbc.repository.MemberRepository;
import com.example.mhbc.service.KakaoService;
import com.example.mhbc.service.SocialUserInfo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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

  @RequestMapping("/sociallogin")
  public String sociallogin(@RequestParam("code") String code, Model model) {
    System.out.println("🔍 받은 인가 코드: " + code);
    String accessToken = kakaoService.getKakaoAccessToken(code);
    SocialUserInfo userInfo = kakaoService.getUserNickname(accessToken);


    if (userInfo == null || userInfo.getEmail() == null) {
      System.out.println("사용자 정보를 가져오지 못했습니다.");
      return "redirect:/member/login";  // 또는 에러페이지로 리디렉션
    }

    // 예시: 사용자 이메일로 회원가입 또는 로그인 처리
    Optional<MemberEntity> existingMember = memberRepository.findByEmail(userInfo.getEmail());
    if (existingMember.isEmpty()) {
      // 새로운 사용자 등록
      MemberEntity newMember = new MemberEntity();
      newMember.setName(userInfo.getNickname());
      newMember.setUserid(userInfo.getId()); // 또는 카카오 ID로 아이디 설정
      newMember.setPwd("default_password");  // 기본 비밀번호 설정 (필요시)
      memberRepository.save(newMember);


      // 모델에 사용자 정보 추가
      model.addAttribute("userInfo", userInfo);
    } else {
      System.out.println("사용자 정보를 가져오는 데 실패했습니다.");
    }
    return "redirect:/";
  }
}

