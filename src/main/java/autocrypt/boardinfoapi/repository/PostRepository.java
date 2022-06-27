package autocrypt.boardinfoapi.repository;

import autocrypt.boardinfoapi.domain.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
}
