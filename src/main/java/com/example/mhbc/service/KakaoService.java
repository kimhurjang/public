package com.example.mhbc.service;

import com.example.mhbc.dto.SocialUserInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@AllArgsConstructor
public class KakaoService {


    public String getKakaoAccessToken(String code) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        String response = webClient.post()
                .uri("/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", "c9bb56960e98eceddc4418dc3243c916")
                        .with("redirect_uri", "http://localhost:8090/member/sociallogin")
                        .with("code", code)
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("📦 카카오 토큰 응답: " + response);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /*사용자 정보 요청*/
    public SocialUserInfo getUserNickname(String accessToken) {
        String response = WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("👤 사용자 정보 응답: " + response);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            Long id = jsonNode.get("id").asLong();
            String nickname = jsonNode.path("properties").path("nickname").asText();
            String email = jsonNode.path("kakao_account").path("email").asText();

            SocialUserInfo userInfo = new SocialUserInfo();
            userInfo.setId(String.valueOf(id));
            userInfo.setNickname(nickname);
            userInfo.setEmail(email);
            return userInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
