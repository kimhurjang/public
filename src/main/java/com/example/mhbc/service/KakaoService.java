package com.example.mhbc.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@AllArgsConstructor
public class KakaoService {


    public String getKakaoAccessToken(String code) {
        String response = WebClient.create()
                .post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("grant_type=authorization_code" +
                        "&client_id=c9bb56960e98eceddc4418dc3243c916" +
                        "&redirect_uri=http://localhost:8090/member/sociallogin" +
                        "&code=" + code +
                        "&scope=account_email profile_nickname")
                .retrieve()
                .bodyToMono(String.class)
                .block();  // 이 결과가 카카오 서버의 JSON 응답 (문자열)

        System.out.println("📦 카카오 토큰 응답: " + response); // 여기서 응답 출력

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get("access_token").asText(); // access_token 추출
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /*사용자 정보 요청*/
    public String getUserInfo(String accessToken) {
        return WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    public SocialUserInfo getUserNickname(String accessToken) {
        try {
            String userInfoJson = getUserInfo(accessToken);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(userInfoJson);

            String id = jsonNode.get("id").asText();
            JsonNode kakaoAccount = jsonNode.get("kakao_account");
            JsonNode profile = kakaoAccount.get("profile");
            String nickname = profile.has("nickname") ? profile.get("nickname").asText() : null;

            return new SocialUserInfo(id, null, nickname); // 이메일은 null로 처리, 닉네임만 사용
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
