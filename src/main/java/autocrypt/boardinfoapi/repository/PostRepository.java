package autocrypt.boardinfoapi.repository;

import autocrypt.boardinfoapi.domain.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends CrudRepository<Post, Long> {
    @Query("select distinct p from Post p join fetch p.user u where p.postId = :postId")
    Optional<Post> findPostWithUser(@Param("postId")Long postId);
}
