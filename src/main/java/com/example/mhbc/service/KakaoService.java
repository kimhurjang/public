package com.example.mhbc.service;

import com.example.mhbc.dto.SocialUserInfoDTO;
import com.example.mhbc.entity.SnsEntity;
import com.example.mhbc.repository.SnsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;

@Service
@AllArgsConstructor
public class KakaoService {

    private final SnsRepository snsRepository; // 🔥 SNS 저장용 Repository 주입

    public String getKakaoAccessToken(String code) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        String response = webClient.post()
                .uri("/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", "3a729b684852129622871e6b959a97e6")
                        .with("redirect_uri", "http://localhost:8090/api/member/kakao")
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

    // 사용자 정보 요청
    public SocialUserInfoDTO getUserNickname(String accessToken) {
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

            SocialUserInfoDTO userInfo = new SocialUserInfoDTO();
            userInfo.setUserid("k" + id);
            userInfo.setNickname(nickname);
            userInfo.setEmail(email);
            return userInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 🔥 SNS 테이블에 저장하는 메서드 추가
    public void saveUserInfoToSns(String accessToken) {
        SocialUserInfoDTO userInfo = getUserNickname(accessToken);
        if (userInfo != null && userInfo.getUserid() != null) {
            SnsEntity sns = SnsEntity.builder()
                    .snsType("kakao")
                    .snsId(userInfo.getUserid())
                    .snsEmail(userInfo.getEmail())
                    .snsName(userInfo.getNickname())
                    //.snsProfileImg(userInfo.getProfileImage())
                    //.snsMobile(userInfo.getMobile())
                    .connectedAt(new Date())
                    .build();

            snsRepository.save(sns);
            System.out.println("✅ SNS 테이블에 저장 완료: " + userInfo.getNickname());
        } else {
            System.out.println("❌ 사용자 정보가 없어서 저장 실패");
        }
    }
}
