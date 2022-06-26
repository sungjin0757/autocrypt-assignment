package autocrypt.boardinfoapi.service;

import autocrypt.boardinfoapi.common.exception.UserException;
import autocrypt.boardinfoapi.domain.User;
import autocrypt.boardinfoapi.dto.UserDto;
import autocrypt.boardinfoapi.init.TestSets;
import autocrypt.boardinfoapi.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    UserDto userDto1;
    UserDto userDto2;

    @BeforeEach
    void init(){
        userDto1 = TestSets.USER_DTO1;
        userDto2 = TestSets.USER_DTO2;
    }

    @Test
    @DisplayName("회원가입 테스트")
    void 회원가입(){
        Long userId = userService.join(userDto1);
        User findUser = userRepository.findById(userId).get();
        Assertions.assertAll(()->{
            Assertions.assertThrows(UserException.class, ()->{
                userService.join(userDto2);
            });
            Assertions.assertNotNull(findUser);
            Assertions.assertEquals(userDto1.getEmail(), findUser.getEmail());
        });
    }

    @Test
    @DisplayName("로그인 테스트")
    void 로그인(){
        userService.join(userDto1);
        UserDto loginUser = userService.login(userDto1.getEmail(), userDto1.getPassword());
        Assertions.assertAll(()->{
            Assertions.assertThrows(UserException.class, ()->{
                userService.login(UUID.randomUUID().toString(),"1234");
            });
            Assertions.assertThrows(UserException.class, ()->{
                userService.login(userDto1.getEmail(), UUID.randomUUID().toString());
            });
            Assertions.assertEquals(userDto1.getEmail(), loginUser.getEmail());
            Assertions.assertEquals(userDto1.getName(), loginUser.getName());
        });
    }
}