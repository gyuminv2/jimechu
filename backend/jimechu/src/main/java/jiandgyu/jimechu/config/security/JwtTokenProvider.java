package jiandgyu.jimechu.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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

    private final String secretKey;
    private final long jwtExpiration;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey,
                            @Value("${jwt.expiration-time}") Long jwtExpiration) {
        this.secretKey = secretKey;
        this.jwtExpiration = jwtExpiration;
    }

    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    /**
     * JWT 토큰 생성
     */
    public TokenInfo createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        Date now = new Date();
        Date accessExpiration = new Date(now.getTime() + jwtExpiration);

        String jwt = Jwts.builder()
                .subject(authentication.getName())
                .claim("auth", authorities)
                .claim("memberId", ((CustomMember) authentication.getPrincipal()).getMemberId())
                .issuedAt(now)
                .expiration(accessExpiration)
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();

        return new TokenInfo("Bearer", jwt);
    }

    /**
     * JWT 토큰 정보 추출
     */
    public Authentication getAuthentication(String jwt) {
        Claims claims = getClaims(jwt);
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
        } catch (SecurityException | MalformedJwtException |UnsupportedJwtException | IllegalArgumentException e) {
                log.debug("[잘못된 토큰] " + e.getMessage());
                throw new JwtException("[잘못된 토큰] " + e.getMessage());
        } catch (ExpiredJwtException e) {
            log.debug("[만료된 토큰] " + e.getMessage());
            throw new JwtException("[만료된 토큰] " + e.getMessage());
        }
    }

    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
