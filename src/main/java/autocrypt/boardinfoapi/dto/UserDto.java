package autocrypt.boardinfoapi.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long userId;
    private String email;
    private String password;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private UserDto(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserDto(Long userId, String email, String password,
                   String name, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public static UserDto newInstance(String email, String password, String name){
        return new UserDto(email, password, name);
    }

    public static UserDto newInstance(Long userId, String email, String password, String name,
                                      LocalDateTime createdDate, LocalDateTime updatedDate){
        return new UserDto(userId, email, password, name, createdDate, updatedDate);
    }
}
