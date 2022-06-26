package autocrypt.boardinfoapi.init;

import autocrypt.boardinfoapi.dto.UserDto;

public abstract class TestSets {
    public static UserDto USER_DTO1 = UserDto.newInstance("sungjin@naver.com","1234", "hong");
    // 중복 데이터 셋
    public static UserDto USER_DTO2 = UserDto.newInstance("sungjin@naver.com","12345", "hongg");
}
