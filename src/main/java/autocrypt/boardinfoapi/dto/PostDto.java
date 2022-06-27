package autocrypt.boardinfoapi.dto;

import autocrypt.boardinfoapi.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDto {
    private Long postId;
    private String title;
    private String content;
    private User user;

    private PostDto(Long postId, String title, String content, User user) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static PostDto newInstance(Long postId, String title, String content, User user){
        return new PostDto(postId, title, content, user);
    }
}
