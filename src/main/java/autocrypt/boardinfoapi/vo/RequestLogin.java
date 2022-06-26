package autocrypt.boardinfoapi.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestLogin {
    @NotNull(message = "Email Cannot Be Null")
    @Email
    private String email;
    @NotNull(message = "Password Cannot Be Null")
    @Size(min = 2, message = "Password Required More Than Two Characters")
    private String password;
}
