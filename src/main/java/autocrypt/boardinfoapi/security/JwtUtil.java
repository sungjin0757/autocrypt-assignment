package autocrypt.boardinfoapi.security;

import autocrypt.boardinfoapi.common.exception.JwtAuthenticationException;
import autocrypt.boardinfoapi.security.property.JwtPrincipal;
import autocrypt.boardinfoapi.security.property.JwtProperty;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperty jwtProperty;

    public String generateToken(JwtPrincipal jwtPrincipal){
        return Jwts.builder()
                .setSubject(jwtPrincipal.getEmail())
                .setHeader(createHeader())
                .setClaims(createClaims(jwtPrincipal))
                .setIssuedAt(new Date())
                .setExpiration(getExpiration())
                .signWith(createSecretKey())
                .compact();
    }

    public JwtPrincipal parseToken(String token){
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(jwtProperty.getSecret()))
                .build();
        try{
            Claims claims = parser.parseClaimsJws(token).getBody();
            return JwtPrincipal.fromClaims(claims);
        }catch (ExpiredJwtException e){
            throw new JwtAuthenticationException("Token Is Expired");
        }catch (JwtException e){
            throw new JwtAuthenticationException("Token Is Invalid");
        } catch (NullPointerException e){
            throw new JwtAuthenticationException("Token Is Empty");
        }
    }

    private Map<String, Object> createHeader(){
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    private Map<String, String> createClaims(JwtPrincipal jwtPrincipal){
        Map<String, String> claims = new HashMap<>();
        claims.put("userId", String.valueOf(jwtPrincipal.getUserId()));
        claims.put("email", jwtPrincipal.getEmail());
        claims.put("password", jwtPrincipal.getPassword());
        claims.put("name", jwtPrincipal.getName());

        return claims;
    }

    private Date getExpiration(){
        return new Date(new Date().getTime() + (jwtProperty.getExpiration() * 60 * 1000));
    }

    private Key createSecretKey() {
        byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(jwtProperty.getSecret());
        return new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
    }
}
