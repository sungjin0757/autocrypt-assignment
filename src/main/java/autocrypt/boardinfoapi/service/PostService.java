package autocrypt.boardinfoapi.service;

import autocrypt.boardinfoapi.domain.enumerations.Locking;
import autocrypt.boardinfoapi.dto.PostDto;

public interface PostService {
    Long write(Long userId, PostDto postDto);
    PostDto findPost(Long userId, Long postId);
    void removePosting(Long userId, Long postId);
    void updatePosting(Long userId, Long postId, PostDto postDto);
    void updateLocking(Long userId, Long postId, Locking locking);
}
