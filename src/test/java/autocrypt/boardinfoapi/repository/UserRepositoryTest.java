package autocrypt.boardinfoapi.repository;

import autocrypt.boardinfoapi.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("생성일자 자동생성 테스트")
    void 생성일자_테스트(){
        User user = userRepository.save(User.newInstance("email", "1234", "name"));

        Assertions.assertAll(()->{
            Assertions.assertNotNull(user.getCreatedDate());
            Assertions.assertNotNull(user.getUpdatedDate());
        });
    }

}