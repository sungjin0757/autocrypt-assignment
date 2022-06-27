package autocrypt.boardinfoapi.repository;

import autocrypt.boardinfoapi.domain.Post;
import autocrypt.boardinfoapi.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryTest {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp(){
        user = User.newInstance("email", "1234", "name");
    }

    @Test
    @DisplayName("생성일자 자동 생성 테스트")
    void 생성일자_자동생성(){
        userRepository.save(user);
        Post savePost = postRepository.save(Post.newInstance("title", "content", user));
        Assertions.assertAll(()->{
            Assertions.assertNotNull(savePost.getCreatedDate());
            Assertions.assertNotNull(savePost.getUpdatedDate());
        });
    }

    @Test
    @DisplayName("findPost 테스트")
    void findPost_테스트(){
        userRepository.save(user);
        Post savePost = postRepository.save(Post.newInstance("title", "content", user));
        Long postId = savePost.getPostId();
        Post findPost = postRepository.findPostWithUser(postId).get();
        Assertions.assertAll(()->{
            Assertions.assertEquals(user.getUserId(), findPost.getUser().getUserId());
        });
    }
}