package com.example.productservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class JwtTokenUtil {

    @Value("${jwt.secretKey}")
    private String jwt_secret;

    private SecretKey getSecretKey() {
        log.info("getSecretKey {}", jwt_secret);
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwt_secret));
    }
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody().getSubject();
    }
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody();
    }
}
