package com.runwithme.runwithme.global.security.config;

import com.runwithme.runwithme.domain.user.service.UserService;
import com.runwithme.runwithme.global.security.filter.CustomAuthenticationFilter;
import com.runwithme.runwithme.global.security.filter.TokenAuthorizationFilter;
import com.runwithme.runwithme.global.security.handler.OAuth2AuthenticationSuccessHandler;
import com.runwithme.runwithme.global.security.jwt.AuthTokenFactory;
import com.runwithme.runwithme.global.security.properties.JwtProperties;
import com.runwithme.runwithme.global.security.provider.CustomAuthenticationProvider;
import com.runwithme.runwithme.global.security.repository.CustomAuthorizationRequestRepository;
import com.runwithme.runwithme.global.security.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final String[] PERMIT_ALL_SWAGGER = {
        /* swagger v2 */
        "/v2/api-docs",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui.html",
        "/webjars/**",
        /* swagger v3 */
        "/v3/api-docs/**",
        "/swagger-ui/**"
    };

    private final String[] PERMIT_ALL_ACTUATOR = {
        "/management/**"
    };

    private final String[] PERMIT_ALL_USER = {
            "/user/login",
            "/user/join"
    };

    private final AuthTokenFactory authTokenFactory;

    private final JwtProperties properties;

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.cors().configurationSource(corsConfigurationSource());

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.formLogin().disable();
        http.httpBasic().disable();

        http.oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(authorizationEndpointConfig ->
                        authorizationEndpointConfig.baseUri("/oauth2/authorization")
                                .authorizationRequestRepository(authorizationRequestRepository()))
                .userInfoEndpoint(
                        userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(customOAuth2UserService)
                )
                .successHandler(oAuth2AuthenticationSuccessHandler()));

        http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        http.authorizeHttpRequests()
                .requestMatchers(PERMIT_ALL_SWAGGER).permitAll()
                .requestMatchers(PERMIT_ALL_ACTUATOR).permitAll()
                .requestMatchers(PERMIT_ALL_USER).permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(tokenAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new CustomAuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(authTokenFactory, properties);
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authTokenFactory, properties);

        customAuthenticationFilter.setFilterProcessesUrl("/users/login");
        customAuthenticationFilter.setAuthenticationManager(authenticationManager());

        return customAuthenticationFilter;
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(passwordEncoder());
    }

    @Bean
    public TokenAuthorizationFilter tokenAuthorizationFilter() {
        return new TokenAuthorizationFilter(authTokenFactory);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("HEAD","POST","GET","DELETE","PUT"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
