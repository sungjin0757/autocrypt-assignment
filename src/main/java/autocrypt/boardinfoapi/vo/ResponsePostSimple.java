package autocrypt.boardinfoapi.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponsePostSimple {
    private Long postId;
    private String title;
    private String content;
    private String email;

    public static ResponsePostSimple newInstance(Long postId, String title, String content,
                                                 String email){
        return new ResponsePostSimple(postId, title, content, email);
    }
}
