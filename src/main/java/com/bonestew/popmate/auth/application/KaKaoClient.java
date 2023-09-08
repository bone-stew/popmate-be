package com.bonestew.popmate.auth.application;

import com.bonestew.popmate.auth.application.dto.UserInformationDto;
import com.bonestew.popmate.auth.domain.KakaoUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KaKaoClient {

    private final RestTemplate restTemplate;
    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    public UserInformationDto getUserInformation(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
            USER_INFO_URI,
            HttpMethod.GET,
            entity,
            KakaoUserResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            KakaoUserResponse kakaoUserResponse = response.getBody();
            if (kakaoUserResponse != null) {
                return new UserInformationDto(
                    kakaoUserResponse.kakao_account().profile().nickname(),
                    kakaoUserResponse.kakao_account().email());
            }
        }
        return null;
    }
}
