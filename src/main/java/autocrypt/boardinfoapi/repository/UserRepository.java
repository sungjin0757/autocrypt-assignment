package autocrypt.boardinfoapi.repository;

import autocrypt.boardinfoapi.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
