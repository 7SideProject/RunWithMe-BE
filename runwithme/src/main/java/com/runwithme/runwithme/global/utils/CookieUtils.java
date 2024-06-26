package com.runwithme.runwithme.global.utils;


import java.util.Base64;
import java.util.Optional;

import org.springframework.util.SerializationUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtils {

	public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return Optional.of(cookie);
				}
			}
		}
		return Optional.empty();
	}

	public static void addCookie(HttpServletResponse response, String name, String value) {
		Cookie cookie = new Cookie(name, value);
		response.addCookie(cookie);
	}

	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);

		response.addCookie(cookie);
	}

	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					cookie.setValue("");
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
	}

	public static String serialize(Object obj) {
		return Base64.getUrlEncoder()
			.encodeToString(SerializationUtils.serialize(obj));
	}

	public static <T> T deserialize(Cookie cookie, Class<T> cls) {
		return cls.cast(
			SerializationUtils.deserialize(
				Base64.getUrlDecoder().decode(cookie.getValue())
			)
		);
	}
}
