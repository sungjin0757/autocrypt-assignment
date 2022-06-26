package autocrypt.boardinfoapi.service;

import autocrypt.boardinfoapi.common.exception.UserException;
import autocrypt.boardinfoapi.domain.User;
import autocrypt.boardinfoapi.dto.UserDto;
import autocrypt.boardinfoapi.repository.UserRepository;
import autocrypt.boardinfoapi.vo.RequestLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Long join(UserDto userDto) {
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        String name = userDto.getName();

        checkDuplicateUser(email);
        User joinUser = userRepository.save(User.newInstance(email, passwordEncoder.encode(password), name));
        return joinUser.getUserId();
    }

    @Override
    public UserDto login(String email, String password) {
        User findUser = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new UserException("이메일을 다시 확인해주세요.");
        });
        String findUserEmail = findUser.getEmail();
        String findUserPassword = findUser.getPassword();
        String findUserName = findUser.getName();
        LocalDateTime createdDate = findUser.getCreatedDate();
        LocalDateTime updatedDate = findUser.getUpdatedDate();

        matchPassword(findUserPassword, password);

        return UserDto.newInstance(findUserEmail, findUserPassword, findUserName,
                createdDate, updatedDate);
    }

    private void matchPassword(String userPassword, String loginPassword){
        if(!passwordEncoder.matches(loginPassword, userPassword)){
            throw new UserException("비밀번호를 다시 확인해주세요.");
        }
    }

    private void checkDuplicateUser(String email){
        if(userRepository.findByEmail(email).isPresent()){
            throw new UserException("중복된 회원 입니다.");
        }
    }
}
