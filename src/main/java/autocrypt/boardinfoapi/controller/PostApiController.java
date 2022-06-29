package autocrypt.boardinfoapi.controller;

import autocrypt.boardinfoapi.common.constants.SecurityConstants;
import autocrypt.boardinfoapi.common.constants.UrlConstants;
import autocrypt.boardinfoapi.controller.util.ValidateUtil;
import autocrypt.boardinfoapi.domain.User;
import autocrypt.boardinfoapi.dto.PostDto;
import autocrypt.boardinfoapi.security.property.JwtPrincipal;
import autocrypt.boardinfoapi.service.PostService;
import autocrypt.boardinfoapi.vo.*;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(UrlConstants.POST_BASE)
@RequiredArgsConstructor
@SecurityRequirement(name = SecurityConstants.SWAGGER_KEY)
@Slf4j
public class PostApiController {
    private final PostService postService;

    @Operation(summary = "게시판 등록")
    @PostMapping(UrlConstants.SAVE)
    public ResponseEntity<ResponsePostSimple> save(@Valid @RequestBody RequestPost requestPost,
                                                   BindingResult result,
                                                   @Parameter(hidden = true) @AuthenticationPrincipal JwtPrincipal jwtPrincipal){
        ValidateUtil.checkValidate(result);
        Long userId = jwtPrincipal.getUserId();

        PostDto postDto = makePostDto(requestPost);

        Long postId = postService.write(userId, postDto);
        ResponsePostSimple responsePostSimple = makeResponsePostSimple(postDto, postId, jwtPrincipal.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(responsePostSimple);
    }

    @Operation(summary = "게시판 조회")
    @GetMapping(UrlConstants.ID)
    public ResponseEntity<ResponsePost> getPost(@PathVariable("id")Long postId,
                                                @Parameter(hidden = true) @AuthenticationPrincipal JwtPrincipal jwtPrincipal){
        Long userId = jwtPrincipal.getUserId();
        PostDto postDto = postService.findPost(userId, postId);
        ResponsePost responsePost = makeResponsePost(postDto);

        return ResponseEntity.status(HttpStatus.OK).body(responsePost);
    }

    @Operation(summary = "게시판 삭제")
    @DeleteMapping(UrlConstants.DELETE + UrlConstants.ID)
    public ResponseEntity<String> deletePost(@PathVariable("id")Long postId,
                                             @Parameter(hidden = true) @AuthenticationPrincipal JwtPrincipal jwtPrincipal){
        Long userID = jwtPrincipal.getUserId();
        postService.removePosting(userID, postId);

        return ResponseEntity.status(HttpStatus.OK).body("Delete Completed");
    }

    @Operation(summary = "게시판 업데이트")
    @PutMapping(UrlConstants.UPDATE + UrlConstants.ID)
    public ResponseEntity<ResponsePostSimple> updatePost(@PathVariable("id")Long postId,
                                                         @Parameter(hidden = true) @AuthenticationPrincipal JwtPrincipal jwtPrincipal,
                                             @Valid @RequestBody RequestPost requestPost, BindingResult result){
        ValidateUtil.checkValidate(result);
        Long userId = jwtPrincipal.getUserId();
        PostDto postDto = makePostDto(requestPost);
        postService.updatePosting(userId, postId, postDto);
        ResponsePostSimple responsePostSimple = makeResponsePostSimple(postDto, postId, jwtPrincipal.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(responsePostSimple);
    }

    @Operation(summary = "게시판 잠금 설정")
    @PutMapping(UrlConstants.UPDATE + UrlConstants.LOCK +UrlConstants.ID)
    public ResponseEntity<String> updateLock(@PathVariable("id")Long postId,
                                             @Parameter(hidden = true) @AuthenticationPrincipal JwtPrincipal jwtPrincipal,
                                                         @Valid @RequestBody RequestLock requestLock,
                                                         BindingResult result){
        ValidateUtil.checkValidate(result);
        Long userId = jwtPrincipal.getUserId();
        postService.updateLocking(userId, postId, requestLock.getLocking());

        return ResponseEntity.status(HttpStatus.OK).body("Update Locking Completed");
    }

    private ResponsePostSimple makeResponsePostSimple(PostDto postDto, Long postId, String email){
        return ResponsePostSimple.newInstance(postId,postDto.getTitle(),postDto.getContent(), email);
    }

    private PostDto makePostDto(RequestPost requestPost){
        String title = requestPost.getTitle();
        String content = requestPost.getContent();

        return PostDto.newInstance(title, content);
    }

    private ResponsePost makeResponsePost(PostDto postDto){
        ResponseUser responseUser = makeResponseUser(postDto.getUser());
        return ResponsePost.newInstance(postDto.getPostId(), postDto.getTitle(),
                postDto.getContent(), postDto.getCreatedDate(), postDto.getUpdatedDate(), responseUser);
    }

    private ResponseUser makeResponseUser(User user){
        return ResponseUser.newInstance(user.getUserId(), user.getEmail(), user.getName());
    }
}
