package com.runwithme.runwithme.global.security.provider;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

public class NaverOAuth2User implements ProviderUser {

    private final Map<String, Object> attributes;

    public NaverOAuth2User(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getProvider() {
        return ProviderType.NAVER.toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
