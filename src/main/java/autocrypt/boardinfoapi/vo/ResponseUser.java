package autocrypt.boardinfoapi.vo;

import lombok.Data;

@Data
public class ResponseUser {
    private Long userId;
    private String email;
    private String name;

    private ResponseUser(Long userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }

    public static ResponseUser newInstance(Long userId, String email, String name){
        return new ResponseUser(userId, email, name);
    }
}
