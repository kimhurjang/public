package com.example.mhbc.dto;

public class SocialUserInfoDTO {
    private String userid;
    private String email;
    private String nickname;

    public SocialUserInfoDTO(String userid, String email, String nickname) {
        this.userid = userid;
        this.email = email;
        this.nickname = nickname;
    }

    public SocialUserInfoDTO() {
    }

    // getter들 추가
    public String getUserid() {
        return userid;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    // Setter
    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
