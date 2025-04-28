package com.example.mhbc.controller;
import com.example.mhbc.dto.MemberDTO;
import com.example.mhbc.dto.SocialUserInfoDTO;
import com.example.mhbc.entity.MemberEntity;
import com.example.mhbc.entity.SnsEntity;
import com.example.mhbc.repository.MemberRepository;
import com.example.mhbc.repository.SnsRepository;
import com.example.mhbc.service.KakaoService;
import com.example.mhbc.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/member")
public class MemberController {

  private final MemberRepository memberRepository;
  private final KakaoService kakaoService;
  private final SnsRepository snsRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;  // 추가!


  @Autowired
  private LoginService loginService; // 로그인 서비스 주입

  // 일반 로그인 페이지

  @GetMapping("/login")
  public String loginForm() {
    return "member/login"; // member 폴더에 있는 login.html을 반환
  }

  @PostMapping("/loginProc")
  public String loginPost(@RequestParam("userid") String userid,
                          @RequestParam("pwd") String pwd,
                          RedirectAttributes redirectAttributes,
                          HttpSession session) {

    // 로그인 서비스 호출
    MemberEntity member = loginService.login(userid, pwd);

    if (member != null) {
      // 로그인 성공 시 세션에 사용자 정보를 저장
      session.setAttribute("loginMember", member);  // 세션에 로그인 정보 저장

      // 로그인 후 메인 페이지로 리다이렉트
      return "redirect:/";
    } else {
      // 비밀번호가 틀리거나 회원이 존재하지 않는 경우
      redirectAttributes.addFlashAttribute("error", "아이디나 비밀번호가 틀립니다.");
      return "redirect:/api/member/login";  // 로그인 페이지로 리디렉션
    }
  }



  @GetMapping("/")
  public String homePage(HttpSession session) {
    Object loginMember = session.getAttribute("loginMember");
    if (loginMember == null) {
      System.out.println("로그인되지 않음");
      return "redirect:/login";  // 로그인하지 않으면 로그인 페이지로 리디렉션
    }
    System.out.println("로그인된 사용자: " + loginMember);
    return "index";  // 로그인된 상태에서 메인 페이지로 이동
  }



  // 카카오 로그인 처리 (인가 코드 받는 엔드포인트)
  @GetMapping("/kakao")
  public String sociallogin(@RequestParam("code") String code, Model model) {
    System.out.println("받은 인가 코드: " + code);
    String accessToken = kakaoService.getKakaoAccessToken(code);
    SocialUserInfoDTO userInfo = kakaoService.getUserInfoFromKakao(accessToken);  // 카카오에서 받은 사용자 정보

    if (userInfo == null || userInfo.getSnsEmail() == null) {
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
      snsUser.setSnsEmail(userInfo.getSnsEmail());  // 이메일
      snsUser.setSnsName(userInfo.getSnsName());  // 이름

      // LocalDateTime으로 연결 시간 설정
      snsUser.setConnectedAt(LocalDateTime.now());  // LocalDateTime 사용

      snsRepository.save(snsUser);  // SNS 정보 저장
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

    // 비밀번호 암호화
    String encodedPwd = bCryptPasswordEncoder.encode(pwd);

    String mobile = mobile1 + "-" + mobile2 + "-" + mobile3;

    MemberDTO memberDTO = MemberDTO.builder()
            .userid(userid)
            .pwd(encodedPwd)  // 암호화된 비밀번호 저장
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

  // 아이디/비밀번호 찾기 페이지
  @RequestMapping("/findidpw")
  public String findidpw() {
    System.out.println(">>>>>>>>>>findidpw page<<<<<<<<<<");
    return "member/findidpw";
  }

  // 관리자 페이지 (사용자 관리)
  @RequestMapping("/adminuser")
  public String adminuser() {
    System.out.println(">>>>>>>>>>adminuser page<<<<<<<<<<");
    return "member/adminuser";
  }

  // 관리자 사용자 정보 보기
  @RequestMapping("/adminuserinfo")
  public String adminuserinfo() {
    System.out.println(">>>>>>>>>>adminuserinfo page<<<<<<<<<<");
    return "member/adminuserinfo";

  }
}