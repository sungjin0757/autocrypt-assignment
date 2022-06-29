package autocrypt.boardinfoapi.controller;

import autocrypt.boardinfoapi.common.constants.UrlConstants;
import autocrypt.boardinfoapi.common.exception.PostException;
import autocrypt.boardinfoapi.domain.Post;
import autocrypt.boardinfoapi.domain.enumerations.Locking;
import autocrypt.boardinfoapi.dto.UserDto;
import autocrypt.boardinfoapi.init.TestSets;
import autocrypt.boardinfoapi.repository.PostRepository;
import autocrypt.boardinfoapi.security.JwtUtil;
import autocrypt.boardinfoapi.security.property.JwtPrincipal;
import autocrypt.boardinfoapi.service.PostService;
import autocrypt.boardinfoapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class PostApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    PostRepository postRepository;

    UserDto userDto1 ;
    UserDto userDto2 ;
    Long userId1;
    Long userId2;
    String token1;
    String token2;
    JwtPrincipal jwtPrincipal1;
    JwtPrincipal jwtPrincipal2;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        userDto1 = TestSets.USER_DTO1;
        userDto2 = TestSets.USER_DTO3;
        userId1 = userService.join(userDto1);
        userId2 = userService.join(userDto2);

        jwtPrincipal1 = JwtPrincipal.newInstance(userId1, userDto1.getEmail(), userDto1.getPassword(), userDto1.getName());
        jwtPrincipal2 = JwtPrincipal.newInstance(userId2, userDto2.getEmail(), userDto2.getPassword(), userDto2.getName());
        token1 = jwtUtil.generateToken(jwtPrincipal1);
        token2 = jwtUtil.generateToken(jwtPrincipal2);

    }

    @Test
    @DisplayName("write 테스트")
    void write_테스트() throws Exception{
        Map<String, String> param1 = new HashMap<>();
        param1.put("title", "title");
        param1.put("content", "content");
        Map<String, String> param2 = new HashMap<>();

        mockMvc.perform(
                MockMvcRequestBuilders.post(UrlConstants.POST_BASE + UrlConstants.SAVE)
                        .header("Authorization", "Bearer "+token1)
                        .content(objectMapper.writeValueAsString(param1))
                        .contentType((new MediaType(
                                MediaType.APPLICATION_JSON.getType(),
                                MediaType.APPLICATION_JSON.getSubtype(),
                                Charset.forName("utf8")
                        ))
                        )

        )
                .andExpect(status().isCreated())
                .andDo(print());
        mockMvc.perform(
                        MockMvcRequestBuilders.post(UrlConstants.POST_BASE + UrlConstants.SAVE)
                                .header("Authorization", "Bearer "+token1)
                                .content(objectMapper.writeValueAsString(param2))
                                .contentType((new MediaType(
                                                MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8")
                                        ))
                                )

                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("find post 테스트")
    void findPost_테스트() throws Exception{
        Long postId = savePost();
        postService.updateLocking(userId1, postId, Locking.DISABLED);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(UrlConstants.POST_BASE + "/"+postId)
                                .header("Authorization", "Bearer "+token1)
                                .contentType((new MediaType(
                                                MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8")
                                        ))
                                )

                )
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(
                        MockMvcRequestBuilders.get(UrlConstants.POST_BASE + "/"+postId)
                                .header("Authorization", "Bearer "+token2)
                                .contentType((new MediaType(
                                                MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8")
                                        ))
                                )

                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("remove post 테스트")
    void removePost_테스트() throws Exception{
        Long postId = savePost();
        mockMvc.perform(
                        MockMvcRequestBuilders.delete(UrlConstants.POST_BASE + UrlConstants.DELETE + "/"+postId)
                                .header("Authorization", "Bearer "+token2)
                                .contentType((new MediaType(
                                                MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8")
                                        ))
                                )

                )
                .andExpect(status().isBadRequest())
                .andDo(print());
        mockMvc.perform(
                        MockMvcRequestBuilders.delete(UrlConstants.POST_BASE + UrlConstants.DELETE + "/"+postId)
                                .header("Authorization", "Bearer "+token1)
                                .contentType((new MediaType(
                                                MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8")
                                        ))
                                )

                )
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertThrows(PostException.class, ()->{
            postService.findPost(userId1, postId);
        });
    }

    @Test
    @DisplayName("update post 테스트")
    void updatePost_테스트() throws Exception{
        Long postId = savePost();
        Map<String, String> param1 = new HashMap<>();
        param1.put("title", "title1");
        param1.put("content", "content1");
        Map<String, String> param2 = new HashMap<>();
        mockMvc.perform(
                        MockMvcRequestBuilders.put(UrlConstants.POST_BASE + UrlConstants.UPDATE + "/"+postId)
                                .header("Authorization", "Bearer "+token2)
                                .content(objectMapper.writeValueAsString(param1))
                                .contentType((new MediaType(
                                                MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8")
                                        ))
                                )

                )
                .andExpect(status().isBadRequest())
                .andDo(print());
        mockMvc.perform(
                        MockMvcRequestBuilders.put(UrlConstants.POST_BASE + UrlConstants.UPDATE + "/"+postId)
                                .header("Authorization", "Bearer "+token1)
                                .content(objectMapper.writeValueAsString(param2))
                                .contentType((new MediaType(
                                                MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8")
                                        ))
                                )

                )
                .andExpect(status().isBadRequest())
                .andDo(print());
        mockMvc.perform(
                        MockMvcRequestBuilders.put(UrlConstants.POST_BASE + UrlConstants.UPDATE + "/"+postId)
                                .header("Authorization", "Bearer "+token1)
                                .content(objectMapper.writeValueAsString(param1))
                                .contentType((new MediaType(
                                                MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8")
                                        ))
                                )

                )
                .andExpect(status().isOk())
                .andDo(print());
        Post post = postRepository.findById(postId).get();
        Assertions.assertAll(()->{
            Assertions.assertEquals(post.getTitle(), "title1");
            Assertions.assertEquals(post.getContent(), "content1");
        });
    }

    @Test
    @DisplayName("update lock 테스트")
    void updateLock_테스트() throws Exception{
        Long postId = savePost();
        Map<String, String> param1 = new HashMap<>();
        param1.put("locking", "DISABLED");
        Map<String, String> param2 = new HashMap<>();
        Map<String, String> param3 = new HashMap<>();
        param3.put("locking", "DISABLED1");

        mockMvc.perform(
                        MockMvcRequestBuilders.put(UrlConstants.POST_BASE + UrlConstants.UPDATE
                                        + UrlConstants.LOCK + "/"+postId)
                                .header("Authorization", "Bearer "+token1)
                                .content(objectMapper.writeValueAsString(param2))
                                .contentType((new MediaType(
                                                MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8")
                                        ))
                                )

                )
                .andExpect(status().isBadRequest())
                .andDo(print());
        mockMvc.perform(
                        MockMvcRequestBuilders.put(UrlConstants.POST_BASE + UrlConstants.UPDATE
                                        + UrlConstants.LOCK + "/"+postId)
                                .header("Authorization", "Bearer "+token1)
                                .content(objectMapper.writeValueAsString(param3))
                                .contentType((new MediaType(
                                                MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8")
                                        ))
                                )

                )
                .andExpect(status().isBadRequest())
                .andDo(print());
        mockMvc.perform(
                        MockMvcRequestBuilders.put(UrlConstants.POST_BASE + UrlConstants.UPDATE
                                        + UrlConstants.LOCK + "/"+postId)
                                .header("Authorization", "Bearer "+token2)
                                .content(objectMapper.writeValueAsString(param1))
                                .contentType((new MediaType(
                                                MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8")
                                        ))
                                )

                )
                .andExpect(status().isBadRequest())
                .andDo(print());
        mockMvc.perform(
                        MockMvcRequestBuilders.put(UrlConstants.POST_BASE + UrlConstants.UPDATE
                                        + UrlConstants.LOCK + "/"+postId)
                                .header("Authorization", "Bearer "+token1)
                                .content(objectMapper.writeValueAsString(param1))
                                .contentType((new MediaType(
                                                MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8")
                                        ))
                                )

                )
                .andExpect(status().isOk())
                .andDo(print());
        Post post = postRepository.findById(postId).get();
        Assertions.assertEquals(post.getLocking(), Locking.DISABLED);
    }

    private Long savePost(){
        return postService.write(userId1, TestSets.POST_DTO1);
    }

}