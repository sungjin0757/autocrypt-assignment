package autocrypt.boardinfoapi.service;

import autocrypt.boardinfoapi.common.exception.PostException;
import autocrypt.boardinfoapi.common.exception.UserException;
import autocrypt.boardinfoapi.domain.Post;
import autocrypt.boardinfoapi.domain.User;
import autocrypt.boardinfoapi.domain.enumerations.Locking;
import autocrypt.boardinfoapi.dto.PostDto;
import autocrypt.boardinfoapi.repository.PostRepository;
import autocrypt.boardinfoapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService{
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    @Override
    public Long write(Long userId, PostDto postDto) {
        User findUser = findUser(userId);
        Post savePost = postRepository.save(Post.newInstance(postDto.getTitle(), postDto.getContent(), findUser));
        return savePost.getPostId();
    }

    @Override
    public PostDto findPost(Long userId, Long postId) {
        Post findPost = findOne(postId);

        if(findPost.getLocking().equals(Locking.DISABLED)){
            checkIdentification(userId, findPost.getUser().getUserId(),
                    "잠금되어 있는 게시물 입니다. (본인만 확인 가능합니다.)");
        }

        return PostDto.newInstance(findPost.getPostId(), findPost.getTitle(), findPost.getContent(),
                findPost.getUser());
    }

    @Transactional
    @Override
    public void removePosting(Long userId, Long postId) {
        Post findPost = findPostWithCheckIdentification(userId, postId);
        try{
            postRepository.deleteById(postId);
        }catch (Exception e){
            throw new PostException("존재하지 않는 게시물 입니다.");
        }
    }

    @Transactional
    @Override
    public void updatePosting(Long userId, Long postId, PostDto postDto) {
        Post findPost = findPostWithCheckIdentification(userId, postId);
        findPost.update(postDto.getTitle(), postDto.getContent());
    }

    @Transactional
    @Override
    public void updateLocking(Long userId, Long postId, Locking locking) {
        Post findPost = findPostWithCheckIdentification(userId, postId);
        findPost.updateLocking(locking);
    }

    private Post findPostWithCheckIdentification(Long userId, Long postId){
        Post findPost = findOne(postId);
        checkIdentification(userId, findPost.getUser().getUserId(), "본인이 아닙니다.");
        return findPost;
    }

    private Post findOne(Long postId){
        return postRepository.findPostWithUser(postId).orElseThrow(() -> {
            throw new PostException("존재하지 않는 게시물 입니다.");
        });
    }

    private void checkIdentification(Long userId, Long postUserId, String message){
        if(userId != postUserId){
            throw new PostException(message);
        }
    }

    private User findUser(Long userId){
        return userRepository.findById(userId).orElseThrow(()->{
           throw new UserException("존재하지 않는 회원입니다.");
        });
    }
}
