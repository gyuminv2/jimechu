package jiandgyu.jimechu.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jiandgyu.jimechu.config.security.jwt.JwtTokenProvider;
import jiandgyu.jimechu.config.security.jwt.TokenInfo;
import jiandgyu.jimechu.config.security.service.CustomMember;
import jiandgyu.jimechu.config.security.service.RefreshTokenService;
import jiandgyu.jimechu.dto.auth.LoginRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    /**
     * 로그인: 인증 후 JWT(access, refresh token) 발급
     */
    public TokenInfo login(LoginRequestDTO requestDTO) {
        // 1) 사용자 입력 기반 UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(requestDTO.getNickname(), requestDTO.getPassword());
        log.debug("Created UsernamePasswordAuthenticationToken: {}", authenticationToken);

        // 2) AuthenticationManager를 통한 인증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.debug("로그인 성공: nickname={}, authorities={}", requestDTO.getNickname(), authentication.getAuthorities());

        // 3) JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.createToken(authentication);
        log.debug("생성된 JWT token: {}", tokenInfo);

        // 4) 로그인 시 Redis에 refresh token 저장
        refreshTokenService.saveRefreshToken(requestDTO.getNickname(), tokenInfo.getRefreshToken());
        return tokenInfo;
    }

    /**
     * Refresh token 기반으로 새로운 access token 발급 및 기존 토큰 블랙리스트 등록
     *
     * @param oldAccessToken       기존 access token (만료되었어도 클레임 추출 가능)
     * @param providedRefreshToken 클라이언트가 전달한 refresh token
     * @return TokenInfo (새 access token, 기존 refresh token)
     */
    public TokenInfo refreshToken(String oldAccessToken, String providedRefreshToken) {
        // 1. access token의 클레임 추출 (만료된 토큰도 처리)
        Claims claims;
        try {
            claims = jwtTokenProvider.getClaims(oldAccessToken);
        } catch (ExpiredJwtException ex) {
            // 만료된 경우에도 claims를 얻는다.
            claims = ex.getClaims();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Access token이 유효하지 않습니다.");
        }

        String username = claims.getSubject();
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("Access token에 사용자 정보가 없습니다.");
        }

        // 2. refresh token 전달 여부 확인
        if (providedRefreshToken == null || providedRefreshToken.isEmpty()) {
            throw new RuntimeException("Refresh token이 요청에 포함되어 있지 않습니다.");
        }

        // 3. Redis에 저장된 refresh token과 비교
        String storedRefreshToken = refreshTokenService.getRefreshToken(username);
        if (storedRefreshToken == null) {
            throw new RuntimeException("해당 사용자에 대한 refresh token이 존재하지 않습니다.");
        }
        if (!storedRefreshToken.equals(providedRefreshToken)) {
            throw new RuntimeException("Refresh token이 일치하지 않습니다.");
        }

        // 4. refresh token 유효성 검증
        try {
            jwtTokenProvider.validateToken(providedRefreshToken);
        } catch (JwtException e) {
            throw new RuntimeException("Refresh token이 만료되었거나 유효하지 않습니다.");
        }

        // 5. 새로운 access token 발급
        String authoritiesStr = claims.get("auth", String.class);
        List<SimpleGrantedAuthority> authorities = Arrays.stream(authoritiesStr.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        Long memberId = claims.get("memberId", Long.class);
        if (memberId == null) {
            memberId = 0L;
        }
        CustomMember principal = new CustomMember(memberId, username, "", authorities);
        Authentication newAuth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        String newAccessToken = jwtTokenProvider.generateAccessToken(newAuth);

        // 6. 기존 access token의 만료시간은 claims에서 직접 가져오기
        Date oldExpiration = claims.getExpiration();
        refreshTokenService.blacklistAccessToken(oldAccessToken, oldExpiration);

        return new TokenInfo("Bearer", newAccessToken, providedRefreshToken);
    }

    /**
     * 로그아웃 처리: refresh token 삭제 및 access token 블랙리스트 등록
     *
     * @param accessToken access token (Header에서 추출)
     * @param username    현재 로그인한 사용자 식별자
     */
    public void logout(String accessToken, String username) {
        refreshTokenService.deleteRefreshToken(username);
        Date expiration = jwtTokenProvider.getExpiration(accessToken);
        refreshTokenService.blacklistAccessToken(accessToken, expiration);
    }
}
