package autocrypt.boardinfoapi.controller;

import autocrypt.boardinfoapi.common.constants.UrlConstants;
import autocrypt.boardinfoapi.common.exception.advice.ExceptionControllerAdvice;
import autocrypt.boardinfoapi.init.TestSets;
import autocrypt.boardinfoapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class UserApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserApiController userApiController;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserService userService;

    Map<String, String> params1;
    Map<String, String> params2;
    Map<String, String> params3;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(userApiController)
                .setControllerAdvice(ExceptionControllerAdvice.class)
                .build();

        params1 = new HashMap<>();
        params1.put("email","sungjin@naver.com");
        params1.put("password", "1234");
        params1.put("name", "hong");

        params2 = new HashMap<>();
        params3 = new HashMap<>();
        params3.put("email","3");
        params3.put("password", "1234");
        params3.put("name", "hong");
    }

    @Test
    @DisplayName("save 테스트")
    void save_테스트() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.post(UrlConstants.USER_BASE+UrlConstants.SAVE)
                        .content(objectMapper.writeValueAsString(params1))
                        .contentType(new MediaType(
                                MediaType.APPLICATION_JSON.getType(),
                                MediaType.APPLICATION_JSON.getSubtype(),
                                Charset.forName("utf8")
                        ))
        )
                .andExpect(status().isCreated())
                .andDo(print());
        mockMvc.perform(
                        MockMvcRequestBuilders.post(UrlConstants.USER_BASE+UrlConstants.SAVE)
                                .content(objectMapper.writeValueAsString(params2))
                                .contentType(new MediaType(
                                        MediaType.APPLICATION_JSON.getType(),
                                        MediaType.APPLICATION_JSON.getSubtype(),
                                        Charset.forName("utf8")
                                ))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
        mockMvc.perform(
                        MockMvcRequestBuilders.post(UrlConstants.USER_BASE+UrlConstants.SAVE)
                                .content(objectMapper.writeValueAsString(params3))
                                .contentType(new MediaType(
                                        MediaType.APPLICATION_JSON.getType(),
                                        MediaType.APPLICATION_JSON.getSubtype(),
                                        Charset.forName("utf8")
                                ))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("login 테스트")
    void login_테스트() throws Exception{
        mockMvc.perform(
                        MockMvcRequestBuilders.post(UrlConstants.USER_BASE+UrlConstants.SAVE)
                                .content(objectMapper.writeValueAsString(params1))
                                .contentType(new MediaType(
                                        MediaType.APPLICATION_JSON.getType(),
                                        MediaType.APPLICATION_JSON.getSubtype(),
                                        Charset.forName("utf8")
                                ))
                )
                .andExpect(status().isCreated())
                .andDo(print());
        Map<String, String> loginParams1 = new HashMap<>();
        loginParams1.put("email", params1.get("email"));
        loginParams1.put("password", params1.get("password"));

        Map<String, String> loginParams2 = new HashMap<>();
        Map<String, String> loginParams3 = new HashMap<>();
        loginParams3.put("email",params1.get("email"));
        loginParams3.put("password", UUID.randomUUID().toString());

        mockMvc.perform(
                MockMvcRequestBuilders.post(UrlConstants.USER_BASE + UrlConstants.LOGIN)
                        .content(objectMapper.writeValueAsString(loginParams1))
                        .contentType(new MediaType(
                                MediaType.APPLICATION_JSON.getType(),
                                MediaType.APPLICATION_JSON.getSubtype(),
                                Charset.forName("utf8")
                        ))
        )
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(
                        MockMvcRequestBuilders.post(UrlConstants.USER_BASE + UrlConstants.LOGIN)
                                .content(objectMapper.writeValueAsString(loginParams2))
                                .contentType(new MediaType(
                                        MediaType.APPLICATION_JSON.getType(),
                                        MediaType.APPLICATION_JSON.getSubtype(),
                                        Charset.forName("utf8")
                                ))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());

        mockMvc.perform(
                        MockMvcRequestBuilders.post(UrlConstants.USER_BASE + UrlConstants.LOGIN)
                                .content(objectMapper.writeValueAsString(loginParams3))
                                .contentType(new MediaType(
                                        MediaType.APPLICATION_JSON.getType(),
                                        MediaType.APPLICATION_JSON.getSubtype(),
                                        Charset.forName("utf8")
                                ))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}