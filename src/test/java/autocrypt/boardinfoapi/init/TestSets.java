package autocrypt.boardinfoapi.init;

import autocrypt.boardinfoapi.dto.PostDto;
import autocrypt.boardinfoapi.dto.UserDto;

public abstract class TestSets {
    public static UserDto USER_DTO1 = UserDto.newInstance("sungjin@naver.com","1234", "hong");
    // 중복 데이터 셋
    public static UserDto USER_DTO2 = UserDto.newInstance("sungjin@naver.com","12345", "hongg");
    public static UserDto USER_DTO3 =UserDto.newInstance("sungjin@google.com","1234","hong");

    public static PostDto POST_DTO1 = PostDto.newInstance("title", "content");
    public static PostDto POST_DTO2 = PostDto.newInstance("title2", "content2");
    public static PostDto POST_DTO3 = PostDto.newInstance("title", "content2");
    public static PostDto POST_DTO4 = PostDto.newInstance("title3", "content2");

}
