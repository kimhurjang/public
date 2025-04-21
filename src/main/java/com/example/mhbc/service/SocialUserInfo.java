package com.example.mhbc.service;

public class SocialUserInfo {
    private String id;
    private String email;
    private String nickname;

    public SocialUserInfo(String id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    // getter들 추가
    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getNickname() { return nickname; }

}
