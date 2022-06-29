package autocrypt.boardinfoapi.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponsePost {
    private Long postId;
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedDate;
    private ResponseUser responseUser;

    public static ResponsePost newInstance(Long postId, String title, String content,
                                           LocalDateTime createdDate,
                                           LocalDateTime updatedDate, ResponseUser responseUser){
        return new ResponsePost(postId, title, content, createdDate, updatedDate, responseUser);
    }
}
