package com.runwithme.runwithme.domain.record.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runwithme.runwithme.domain.record.dto.CoordinateDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
@DisplayName("Record Service 단위 테스트")
class RecordControllerTest {

    private MockMvc mockMvc;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private String toJson(HashMap<String, Object> stringObjectHashMap) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.writeValueAsString(stringObjectHashMap);
    }

    @Test
    @DisplayName("[challenge] 챌린지 기록을 등록한다.")
    void _01_test() throws Exception {
        ResultActions result;
        result = mockMvc.perform(
                post("/challenge/1/record")
                        .header("Authorization", "Bearer ewogICJpZCI6IDEsCiAgImV4cGlyZSI6IDE2NzI0ODgwMDAwMDAKfQ==")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(
                                toJson(
                                        new HashMap<String, Object>() {{
                                            put("startTime", "2024-03-18");
                                            put("endTime", "2024-03-18");
                                            put("runningDay", "2024-03-18");
                                            put("runningTime", 123);
                                            put("runningDistance", 300);
                                            put("calorie", 100);
                                            put("avgSpeed", 2.0D);
                                            put("coordinates", null);
                                            put("successYn", 'Y');
                                        }}
                                )
                        )
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.ResultResponseDto.code").isNumber())
                .andExpect(jsonPath("$.data.ResultResponseDto.code", is(200)))
                .andExpect(jsonPath("$.data.ResultResponseDto.code").isNumber())
                .andExpect(jsonPath("$.data.ResultResponseDto.code", is(200)))
                .andExpect(jsonPath("$.data.ResultResponseDto.message", is("기록 등록에 성공하였습니다.")))
                .andExpect(jsonPath("$.error").doesNotExist())
        ;
    }
}