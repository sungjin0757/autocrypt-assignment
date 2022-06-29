package autocrypt.boardinfoapi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestPost {
    @NotNull(message = "Title Cannot Be Null")
    private String title;
    @NotNull(message = "Content Cannot Be Null")
    private String content;
}
