package autocrypt.boardinfoapi.security.property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
@Getter
public class JwtProperty {
    private final String secret;
    private final Long expiration;

    public JwtProperty(String secret, Long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }
}
