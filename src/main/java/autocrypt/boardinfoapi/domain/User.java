package autocrypt.boardinfoapi.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Table(name = "users")
@SequenceGenerator(
        name = "USER_SEQ_GENERATOR",
        sequenceName = "USER_SEQ",
        initialValue = 1, allocationSize = 50
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AbstractDateTraceEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_GENERATOR")
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;

    private User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User newInstance(String email, String password, String name){
        return new User(email, password, name);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof User))
            return false;
        User u = (User) o;
        return Objects.equals(userId, u.userId) && Objects.equals(email, u.email) &&
                Objects.equals(password, u.password) && Objects.equals(name, u.name);
    }

    @Override
    public int hashCode() {
        int result = userId == null ? 0 : userId.hashCode();
        result = result * 31 + email.hashCode();
        result = result * 31 + password.hashCode();
        result = result * 31 + name.hashCode();
        return result;
    }
}
