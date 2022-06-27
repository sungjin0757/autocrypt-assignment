package autocrypt.boardinfoapi.service;

import autocrypt.boardinfoapi.common.exception.PostException;
import autocrypt.boardinfoapi.common.exception.UserException;
import autocrypt.boardinfoapi.domain.Post;
import autocrypt.boardinfoapi.domain.User;
import autocrypt.boardinfoapi.domain.enumerations.Locking;
import autocrypt.boardinfoapi.dto.PostDto;
import autocrypt.boardinfoapi.dto.UserDto;
import autocrypt.boardinfoapi.init.TestSets;
import autocrypt.boardinfoapi.repository.PostRepository;
import autocrypt.boardinfoapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {
    @Autowired
    PostService postService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    User user1;
    PostDto postDto1;
    PostDto postDto2;
    PostDto postDto3;
    PostDto postDto4;

    @BeforeEach
    void setUp(){
        user1 = userRepository.save(User.newInstance("email", "1234", "name"));
        postDto1 = TestSets.POST_DTO1;
        postDto2 = TestSets.POST_DTO2;
        postDto3 = TestSets.POST_DTO3;
        postDto4 = TestSets.POST_DTO4;
    }

    @Test
    @DisplayName("write 테스트")
    void write_테스트(){
        User user = userRepository.save(user1);
        Long postId = postService.write(user.getUserId(), postDto1);
        Long postId2 = postService.write(user.getUserId(), postDto2);
        Assertions.assertAll(()->{
            Assertions.assertThrows(UserException.class, ()->{
               postService.write(-99l, postDto1);
            });
            Assertions.assertEquals(postRepository.findById(postId).get().getUser().getUserId(), user.getUserId());
            Assertions.assertEquals(postRepository.findById(postId2).get().getUser().getUserId(), user.getUserId());
        });
    }

    @Test
    @DisplayName("findPost 테스트")
    void findPost_테스트(){
        User saveUser = userRepository.save(user1);
        Post post = Post.newInstance("title", "content", user1);
        Post post2 = Post.newInstance("title2", "content", user1);
        post.updateLocking(Locking.DISABLED);
        post2.updateLocking(Locking.DISABLED);
        Post savePost = postRepository.save(post);
        Post savePost2 = postRepository.save(post2);

        Assertions.assertAll(()->{
            Assertions.assertThrows(PostException.class,()->{
                postService.findPost(saveUser.getUserId(), -99l);
            });
            Assertions.assertThrows(PostException.class, ()->{
                postService.findPost(-99l, savePost.getPostId());
            });
            Assertions.assertThrows(PostException.class,()->{
                postService.findPost(saveUser.getUserId(), -99l);
            });
            Assertions.assertThrows(PostException.class, ()->{
                postService.findPost(-99l, savePost2.getPostId());
            });
            Assertions.assertDoesNotThrow(()->{
                postService.findPost(saveUser.getUserId(), savePost.getPostId());
            });
            Assertions.assertDoesNotThrow(()->{
                postService.findPost(saveUser.getUserId(), savePost2.getPostId());
            });
        });
    }

    @Test
    @DisplayName("removePosting 테스트")
    void removePosting_테스트(){
        User saveUser = userRepository.save(user1);
        Long postId = postService.write(saveUser.getUserId(), postDto1);
        Long postId2 = postService.write(saveUser.getUserId(), postDto2);
        postService.removePosting(saveUser.getUserId(), postId2);

        Assertions.assertAll(()->{
            Assertions.assertThrows(PostException.class, ()->{
               postService.removePosting(-99l, postId);
            });
            Assertions.assertThrows(PostException.class, ()->{
                postService.removePosting(saveUser.getUserId(), -99l);
            });
            Assertions.assertDoesNotThrow(()->{
                postService.removePosting(saveUser.getUserId(), postId);
            });
            Assertions.assertEquals(postRepository.findById(postId2), Optional.empty());
        });
    }

    @Test
    @DisplayName("updatePosting 테스트")
    void updatePosting_테스트(){
        User saveUser = userRepository.save(user1);
        Long postId = postService.write(saveUser.getUserId(), postDto1);

        Assertions.assertAll(()->{
            Assertions.assertThrows(PostException.class, ()->{
                postService.updatePosting(-99l, postId, postDto2);
            });
            Assertions.assertThrows(PostException.class, ()->{
                postService.updatePosting(saveUser.getUserId(), -99l, postDto2);
            });

            postService.updatePosting(saveUser.getUserId(), postId, postDto3);
            Post findPost = postRepository.findById(postId).get();
            Assertions.assertEquals(findPost.getTitle(),postDto1.getTitle());
            Assertions.assertEquals(findPost.getContent(),postDto3.getContent());

            postService.updatePosting(saveUser.getUserId(), postId, postDto4);
            Post findPost2 = postRepository.findById(postId).get();
            Assertions.assertEquals(findPost2.getTitle(),postDto4.getTitle());
            Assertions.assertEquals(findPost2.getContent(),postDto3.getContent());

            postService.updatePosting(saveUser.getUserId(), postId, postDto1);
            Post findPost3 = postRepository.findById(postId).get();
            Assertions.assertEquals(findPost3.getTitle(),postDto1.getTitle());
            Assertions.assertEquals(findPost3.getContent(),postDto1.getContent());
        });
    }

    @Test
    @DisplayName("updateLocking 테스트")
    void updateLocking_테스트(){
        User saveUser = userRepository.save(user1);
        Long postId = postService.write(saveUser.getUserId(), postDto1);

        Assertions.assertAll(()->{
            Assertions.assertThrows(PostException.class, ()->{
                postService.updateLocking(-99l, postId, Locking.ENABLED);
            });
            Assertions.assertThrows(PostException.class, ()->{
                postService.updateLocking(saveUser.getUserId(), -99l, Locking.DISABLED);
            });
            postService.updateLocking(saveUser.getUserId(), postId, Locking.DISABLED);
            Post findPost = postRepository.findById(postId).get();
            Assertions.assertEquals(findPost.getLocking(), Locking.DISABLED);

            postService.updateLocking(saveUser.getUserId(), postId, Locking.ENABLED);
            Post findPost2 = postRepository.findById(postId).get();
            Assertions.assertEquals(findPost2.getLocking(), Locking.ENABLED);
        });
    }
}