package com.runwithme.runwithme.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        setResponseBody(response);
    }

    private void setResponseBody(HttpServletResponse response) {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String responseBody = null;

        try {
            ObjectWriter objectWriter = om.writerWithDefaultPrettyPrinter();
            System.out.println("objectWriter = " + objectWriter);
//            responseBody = objectWriter.writeValueAsString(ResultResponseDto.of(ResultCode.UNAUTHORIZED));
            responseBody = objectWriter.toString();
        } catch (Exception e) {
            System.out.println("EEE" + e);
        }
//        String responseBody = om.writerWithDefaultPrettyPrinter().writeValueAsString(ResultResponseDto.of(ResultCode.UNAUTHORIZED));

        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (Exception e) {
            System.out.println(e);
        }
        writer.write(responseBody);
    }
}
