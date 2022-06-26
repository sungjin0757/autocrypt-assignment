package autocrypt.boardinfoapi.service;

import autocrypt.boardinfoapi.dto.UserDto;
import autocrypt.boardinfoapi.vo.RequestLogin;

public interface UserService {
    Long join(UserDto userDto);
    UserDto login(String email, String password);
}
