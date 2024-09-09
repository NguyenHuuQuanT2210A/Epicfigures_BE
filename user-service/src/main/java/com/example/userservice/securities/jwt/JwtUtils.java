package com.example.userservice.securities.jwt;

import com.example.userservice.exceptions.CustomException;
import com.example.userservice.securities.services.UserDetailsImpl;
import com.example.userservice.statics.enums.TokenType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.userservice.statics.enums.TokenType.ACCESS_TOKEN;
import static com.example.userservice.statics.enums.TokenType.REFRESH_TOKEN;

@Component
@Slf4j
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Value("${jwt.resetKey}")
    private String resetKey;

    private Key getKey(TokenType type){
        return switch (type) {
            case ACCESS_TOKEN -> Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
            case REFRESH_TOKEN -> Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
            case RESET_TOKEN -> Keys.hmacShaKeyFor(Decoders.BASE64.decode(resetKey));
            default -> throw new CustomException("Token type invalid", HttpStatus.BAD_REQUEST);
        };
    }

    public String generateAccessToken(UserDetailsImpl userDetails) {
        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getKey(ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetailsImpl userDetails) {
        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000 * 7))
                .signWith(getKey(REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateResetToken(UserDetailsImpl userDetails) {

        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(getKey(REFRESH_TOKEN), SignatureAlgorithm.HS256)
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
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getUserNameFromJwtToken(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }

    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimsResolver){
        final Claims claims = extraAllClaim(token, type);
        return claimsResolver.apply(claims);
    }

    private Claims extraAllClaim(String token, TokenType type){
        return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getBody();
    }

    public boolean validateJwtToken(String authToken, TokenType type) {
        try {
            Jwts.parser().setSigningKey(getKey(type)).parseClaimsJws(authToken);
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
