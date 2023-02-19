package com.runwithme.runwithme.global.security.service;

import com.runwithme.runwithme.global.security.provider.ProviderType;
import com.runwithme.runwithme.global.security.provider.ProviderUser;
import com.runwithme.runwithme.global.security.provider.ProviderUserFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private ProviderUser providerUser(OAuth2UserRequest userRequest, OAuth2User user) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        return ProviderUserFactory.build(ProviderType.valueOf(registrationId), user.getAttributes());
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        return providerUser(userRequest, oAuth2User);
    }
}
