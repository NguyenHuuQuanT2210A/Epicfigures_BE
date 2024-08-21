package com.example.paymentService.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class JwtTokenUtil {

    private static final String jwt_secret =
            "enRmc3R5eW4zc3BhdW5tMjg3cHY5cWQyb2pxanoxdnJ4bjk0bmFkZXhqcjJ4eGpldGlscmZnenJyY2llZjNxN2Q5N3dwNmltdnA1YXFlN2JxMnIwdnU2dm51c2p4Ynk3Ym15emQxZnZmc3M4ZzE1cnZubG80d2N4Z3VqN2cxNjg=";
//            "ZHhx9KAoMauq823k72mVjMhhmypx5QQ8eso8Ocwlsm8pQPDGdhtergbjKfTOVEMa2PR9SrFXbCrSLK8jSmWJTxS3oRd2KEZVzwO0YumtYftcNWNK8E1e3SpJvRH0KnW4X6sMoaPbWjf3EA8CiagAQ1IgTxDqOtAd14fvndtA87e9DjhRrluPj6t13M8Qp3pkHKXJVw6FfW3xj4j0YxSQXVMQkdTfgwjZfCvoS0YrnAW3plSfrfVueqTammoeX0NTiMd9Ua0fMjcUypyMM2aGe9MRMF4LZiEvsThvTt4UlGOS9Iozv4BnkEjU8yARJ582JnqoUUSWkDGfLA9m1WlTGCoavlcsehlTPCZQ9IenZcw4HlrmQGml08NQGTqN1u2YIzfV1Xb6pDnQtIGWxi2fasccPJZOFdo0QKy9bJVV3Q8dIorwX41dlFZWLh1y8haaPe5FBHAIIayd30S3jgQFpuiVmTUJEjQO9FM2mU0UagZnbBmZRHx78K1hbGjwtzGn";  // Tạo khóa ký an toàn
    private SecretKey getSecretKey() {
        log.info("getSecretKey {}", jwt_secret);
        return Keys.hmacShaKeyFor(jwt_secret.getBytes());
    }
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody().getSubject();
    }
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody();
    }
}
