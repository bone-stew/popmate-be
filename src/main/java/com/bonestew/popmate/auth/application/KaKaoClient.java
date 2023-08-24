package com.bonestew.popmate.auth.application;

import com.bonestew.popmate.auth.domain.KakaoUserResponse;
import com.bonestew.popmate.auth.domain.OauthKakaoUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KaKaoClient {

    private final RestTemplate restTemplate;
    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
    public KaKaoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OauthKakaoUser getUserInfo(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(code);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
            USER_INFO_URI,
            HttpMethod.GET,
            entity,
            KakaoUserResponse.class
        );

        if(response.getStatusCode() == HttpStatus.OK){
            System.out.println(response.getBody());
            KakaoUserResponse kakaoUserResponse = response.getBody();
            if(kakaoUserResponse != null){
                return kakaoUserResponse.toUser();
            }
        }
        return null;
    }
}
