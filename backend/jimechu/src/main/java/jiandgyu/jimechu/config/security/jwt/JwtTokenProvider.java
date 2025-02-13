package jiandgyu.jimechu.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jiandgyu.jimechu.config.security.service.CustomMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final String key;
    private final long accessExpiration;
    private final long refreshExpiration;


    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey,
                            @Value("${jwt.access-expiration-time}") Long accessExpiration,
                            @Value("${jwt.refresh-expiration-time}") Long refreshExpiration) {
        this.key = secretKey;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    /**
     * JWT 토큰 생성
     */
    public TokenInfo createToken(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication.getName());

        return new TokenInfo("Bearer", accessToken, refreshToken);
    }

    /**
     * Access Token 생성
     */
    public String generateAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();

        return Jwts.builder()
                .subject(authentication.getName())
                .claim("auth", authorities)
                .claim("memberId", ((CustomMember) authentication.getPrincipal()).getMemberId())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + accessExpiration))
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Refresh Token 생성
     */
    public String generateRefreshToken(String username) {
        Date now = new Date();

        return Jwts.builder()
                .subject(username)
                .expiration(new Date(now.getTime() + refreshExpiration))
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * JWT 토큰 정보 추출
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String auth = Optional.ofNullable(claims.get("auth", String.class))
                .orElseThrow(() -> new RuntimeException("잘못된 토큰입니다."));
        Long memberId = Optional.ofNullable(claims.get("memberId", Long.class))
                .orElseThrow(() -> new RuntimeException("잘못된 토큰입니다."));

        Collection<GrantedAuthority> authorities = Arrays.stream(auth.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 보안상 비밀번호는 비워둠
        UserDetails principal = new CustomMember(memberId, claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * 토큰 검증
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("[만료된 토큰] " + e.getMessage());
            throw e;  // 예외를 그대로 던져서 filter 에서 처리 !
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("[잘못된 토큰] " + e.getMessage());
            throw new JwtException("[잘못된 토큰] " + e.getMessage());
        }
    }

    public Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    // JWT 토큰 검증
    public boolean isValidToken(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Date getExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }
}
