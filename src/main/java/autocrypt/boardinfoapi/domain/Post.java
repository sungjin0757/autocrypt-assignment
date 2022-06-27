package autocrypt.boardinfoapi.domain;

import autocrypt.boardinfoapi.domain.enumerations.Locking;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Table(name = "posts")
@SequenceGenerator(
        name = "POST_SEQ_GENERATOR",
        sequenceName = "POST_SEQ",
        initialValue = 1, allocationSize = 50
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends AbstractDateTraceEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SEQ_GENERATOR")
    @Column(name = "post_id")
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Locking locking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updateLocking(Locking locking){
        if(locking.equals(Locking.DISABLED)){
            lock();
        }else if(locking.equals(Locking.ENABLED)){
            unLock();
        }
    }

    private void lock(){
        this.locking = Locking.DISABLED;
    }

    private void unLock(){
        this.locking = Locking.ENABLED;
    }

    public void update(String title, String content){
        updateTitle(title);
        updateContent(content);
    }

    private void updateTitle(String title){
        if(!(this.title.equals(title))){
            this.title = title;
        }
    }

    private void updateContent(String content){
        if(!(this.content.equals(content))){
            this.content = content;
        }
    }

    private Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Post newInstance(String title, String content, User user){
        Post post = new Post(title, content);
        post.user = user;
        post.locking = Locking.ENABLED;
        return post;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof Post))
            return false;
        Post p = (Post) o;
        return Objects.equals(postId, p.postId) && Objects.equals(title, p.title)
                && Objects.equals(content, p.content);
    }

    @Override
    public int hashCode() {
        int result = postId == null ? 0 : postId.hashCode();
        result = result * 31 + title.hashCode();
        result = result * 31 + content.hashCode();
        return result;
    }
}
