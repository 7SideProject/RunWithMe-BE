package com.runwithme.runwithme.global.security.provider;

import java.util.Map;

public class ProviderUserFactory {
    public static ProviderUser build(ProviderType providerType, Map<String, Object> attributes) {
        switch (providerType) {
            case NAVER: return new NaverOAuth2User((Map<String, Object>) attributes.get("response"));
//            case KAKAO: return new KaKaoOAuth2User((Map<String, Object>) attributes.get("kakao_account"));
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
