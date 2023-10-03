package com.runwithme.runwithme.global.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {

    public String secret;

    public int accessTokenExpiry;

    public int refreshTokenExpiry;
}
