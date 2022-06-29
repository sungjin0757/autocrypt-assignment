package autocrypt.boardinfoapi.dto;

import autocrypt.boardinfoapi.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostDto {
    private Long postId;
    private String title;
    private String content;
    private User user;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public PostDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    private PostDto(Long postId, String title, String content, User user, LocalDateTime createdDate,
                    LocalDateTime updatedDate) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.user = user;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public static PostDto newInstance(Long postId, String title, String content, User user,
                                      LocalDateTime createdDate, LocalDateTime updatedDate){
        return new PostDto(postId, title, content, user, createdDate, updatedDate);
    }

    public static PostDto newInstance(String title, String content){
        return new PostDto(title, content);
    }
}
