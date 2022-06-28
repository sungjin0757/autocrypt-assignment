package autocrypt.boardinfoapi.security.property;

import autocrypt.boardinfoapi.dto.UserDto;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtPrincipal {
    private Long userId;
    private String email;
    private String password;
    private String name;

    public static JwtPrincipal fromClaims(Claims claims){
        return new JwtPrincipal((Long) claims.get("userId"), (String) claims.get("email"),
                (String) claims.get("password"), (String) claims.get("name"));
    }

    public static JwtPrincipal fromDto(UserDto userDto){
        return new JwtPrincipal(userDto.getUserId(), userDto.getEmail(), userDto.getPassword(),
                userDto.getName());
    }
}
