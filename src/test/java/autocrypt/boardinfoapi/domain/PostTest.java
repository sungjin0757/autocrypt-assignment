package autocrypt.boardinfoapi.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {
    @Test
    @DisplayName("update 테스트")
    void update_테스트(){
        User user = User.newInstance("email", "1234", "name");
        Post post1 = Post.newInstance("1", "2", user);
        Post post2 = Post.newInstance("1", "2", user);
        post1.update("1","3");
        post2.update("3","2");

        Assertions.assertAll(()->{
            Assertions.assertEquals(post1.getTitle(),"1");
            Assertions.assertEquals(post1.getContent(),"3");
            Assertions.assertEquals(post2.getTitle(),"3");
            Assertions.assertEquals(post2.getContent(),"2");
        });
    }
}