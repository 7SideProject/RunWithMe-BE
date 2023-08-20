package com.runwithme.runwithme;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
@DisplayName("API 테스트")
public class ApisTest {

	private MockMvc mockMvc;

	@Autowired
	public void setMockMvc(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	private String toJson(HashMap<String, Object> stringObjectHashMap) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(stringObjectHashMap);
	}

	@Test
	@DisplayName("[인증 토큰 테스트] 인증 토큰이 없다면 API 호출을 실패한다.")
	void _01_test() throws Exception {
		ResultActions result = mockMvc.perform(
				get("/")
						.accept(MediaType.APPLICATION_JSON)
		);
		result.andDo(print())
				.andExpect(status().isUnauthorized())
		;
	}

	@Test
	@DisplayName("[인증 토큰 테스트] 인증 토큰이 올바르지 않다면 API 호출을 실패한다.")
	void _02_test() throws Exception {
		ResultActions result = mockMvc.perform(
				get("/")
						.header("Authorization", "Bearer AABBCC")
						.accept(MediaType.APPLICATION_JSON)
		);
		result.andDo(print())
				.andExpect(status().isUnauthorized())
		;
	}

	@Test
	@DisplayName("[인증 토큰 테스트] 인증 토큰이 만료되었다면 API 호출을 실패한다.")
	void _03_test() throws Exception {
		ResultActions result = mockMvc.perform(
				get("/")
						.header("Authorization", "Bearer ewogICJpZCI6IDEsCiAgImV4cGlyZSI6IDE1NTAwMDAwMDAwMDAKfQ==")
						.accept(MediaType.APPLICATION_JSON)
		);
		result.andDo(print())
				.andExpect(status().isUnauthorized())
		;
	}

	@Test
	@DisplayName("[인증 토큰 테스트] 인증 토큰 스킴(Bearer)이 올바르지 않다면 API 호출을 실패한다.")
	void _04_test() throws Exception {
		ResultActions result = mockMvc.perform(
				get("/")
						.header("Authorization", "ewogICJpZCI6IDEsCiAgImV4cGlyZSI6IDE2NzI0ODgwMDAwMDAKfQ==")
						.accept(MediaType.APPLICATION_JSON)
		);
		result.andDo(print())
				.andExpect(status().isUnauthorized())
		;
	}

	@Test
	@DisplayName("[계좌 생성 API 테스트] name 파라미터가 누락되면 API 호출을 실패한다.")
	void _05_test() throws Exception {
		ResultActions result = mockMvc.perform(
				post("/")
						.header("Authorization", "Bearer ewogICJpZCI6IDEsCiAgImV4cGlyZSI6IDE2NzI0ODgwMDAwMDAKfQ==")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(
								toJson(
										new HashMap<String, Object>() {{
											put("name", "");
										}}
								)
						)
		);
		result.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.error").exists())
		;
	}
}
