package com.runwithme.runwithme.global.security.provider;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface ProviderUser extends OAuth2User {

    String getEmail();

    String getName();

    String getProvider();
}
