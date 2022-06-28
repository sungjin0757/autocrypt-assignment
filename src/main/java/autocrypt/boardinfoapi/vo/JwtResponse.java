package autocrypt.boardinfoapi.vo;

import lombok.Data;

@Data
public class JwtResponse {
    private String message;
    private String token;

    public JwtResponse(String token) {
        this.message = "Login Success";
        this.token = token;
    }
}
