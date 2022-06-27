package autocrypt.boardinfoapi.repository;

import autocrypt.boardinfoapi.domain.Post;
import autocrypt.boardinfoapi.domain.User;
import org.junit.jupiter.api.Assertions;
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

    @Test
    @DisplayName("생성일자 자동 생성 테스트")
    void 생성일자_자동생성(){
        User user = User.newInstance("email", "1234", "name");
        Post post = postRepository.save(Post.newInstance("title", "content", user));
        Assertions.assertAll(()->{
            Assertions.assertNotNull(post.getCreatedDate());
            Assertions.assertNotNull(post.getUpdatedDate());
        });
    }
}