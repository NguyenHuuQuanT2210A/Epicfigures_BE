package com.example.userservice.securities.jwt;

import com.example.userservice.securities.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${application.jwtExpirationMs}")
    private int jwtExpirationMs;
    private static final String SECRET_KEY =
            "enRmc3R5eW4zc3BhdW5tMjg3cHY5cWQyb2pxanoxdnJ4bjk0bmFkZXhqcjJ4eGpldGlscmZnenJyY2llZjNxN2Q5N3dwNmltdnA1YXFlN2JxMnIwdnU2dm51c2p4Ynk3Ym15emQxZnZmc3M4ZzE1cnZubG80d2N4Z3VqN2cxNjg=";
//            "ZHhx9KAoMauq823k72mVjMhhmypx5QQ8eso8Ocwlsm8pQPDGdhtergbjKfTOVEMa2PR9SrFXbCrSLK8jSmWJTxS3oRd2KEZVzwO0YumtYftcNWNK8E1e3SpJvRH0KnW4X6sMoaPbWjf3EA8CiagAQ1IgTxDqOtAd14fvndtA87e9DjhRrluPj6t13M8Qp3pkHKXJVw6FfW3xj4j0YxSQXVMQkdTfgwjZfCvoS0YrnAW3plSfrfVueqTammoeX0NTiMd9Ua0fMjcUypyMM2aGe9MRMF4LZiEvsThvTt4UlGOS9Iozv4BnkEjU8yARJ582JnqoUUSWkDGfLA9m1WlTGCoavlcsehlTPCZQ9IenZcw4HlrmQGml08NQGTqN1u2YIzfV1Xb6pDnQtIGWxi2fasccPJZOFdo0QKy9bJVV3Q8dIorwX41dlFZWLh1y8haaPe5FBHAIIayd30S3jgQFpuiVmTUJEjQO9FM2mU0UagZnbBmZRHx78K1hbGjwtzGn";  // Tạo khóa ký an toàn

    private SecretKey getSecretKey() {
        log.info("getSecretKey {}", SECRET_KEY);
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .claim("roles", userPrincipal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateJwtOAuth2Token(Authentication authentication) {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String username = oauth2User.getAttribute("email");

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(username)
                .claim("name", oauth2User.getAttribute("name"))
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
