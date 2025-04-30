//package com.example.mhbc.service;
//
//import com.example.mhbc.entity.MemberEntity;
//import com.example.mhbc.repository.MemberRepository;
//import com.example.mhbc.service.UserDetailsImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@RequiredArgsConstructor//자동 생성자
//@Service
//public class UserDetailServiceImpl implements UserDetailsService {
//
//    private final MemberRepository memberRepository;//final == 생성자 주입법. Required랑 세트member;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println("----UserDetailService----");
//        System.out.println("----username : " + username);
//
//        Optional<MemberEntity> member = memberRepository.findByUserid(username);//DB에서 username과 일치하는 userid찾기
//
//        if(member == null){
//            System.out.println("없는 아이디");
//            return null;
//        }
//
//        System.out.println(member.toString());
//
//        return new UserDetailsImpl(member);
//    }
//}
