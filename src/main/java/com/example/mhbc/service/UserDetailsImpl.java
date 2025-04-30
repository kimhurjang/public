package com.example.mhbc.service;

// Lombok의 @Data 어노테이션을 사용하여 자동으로 getter, setter, toString 등을 생성
import com.example.mhbc.entity.MemberEntity;
import lombok.Data;

// Spring Security의 GrantedAuthority 및 UserDetails 인터페이스를 사용
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// @Data 어노테이션을 통해 기본적인 getter, setter, equals, hashCode, toString 자동 생성
@Data
public class UserDetailsImpl implements UserDetails {  // Spring Security의 UserDetails 인터페이스 구현

    private MemberEntity member;

    public UserDetailsImpl(MemberEntity member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 권한을 반환하는 메서드
        // 여기서는 "ROLE_USER" 권한을 가진 SimpleGrantedAuthority 객체를 리스트로 반환
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        //일반 유저 권한
//        if(member.getRole().equals("user")){
//            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//        }
//
//        //관리자 권한
//        if(member.getRole().equals("admin")){
//            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        }
        return authorities;
    }

    @Override
    public String getPassword() {
        // 사용자의 비밀번호를 반환하는 메서드
        System.out.println("----getPassword----");
        return member.getPwd();
    }

    @Override
    public String getUsername() {
        // 사용자의 아이디(Username)를 반환하는 메서드
        System.out.println("----getUsername----");
        return member.getUserid();
    }
}
