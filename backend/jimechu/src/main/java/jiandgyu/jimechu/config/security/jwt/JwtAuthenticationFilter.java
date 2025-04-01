package jiandgyu.jimechu.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jiandgyu.jimechu.config.security.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        // refresh 엔드포인트는 토큰 검증 로직을 건너뛰도록 처리
        if ("/api/auth/refresh".equals(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = resolveToken(httpRequest);
            if (token == null) {
                chain.doFilter(request, response);
                return;
            }

            if (refreshTokenService.isBlacklisted(token)) {
                throw new JwtException("해당 Access Token은 로그아웃 되었습니다.");
            }

            jwtTokenProvider.validateToken(token); // 만료된 토큰 감지 시 ExpiredJwtException 발생

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            if (e.getClaims() != null && e.getClaims().get("auth") == null) {
                sendErrorResponse((HttpServletResponse) response, HttpServletResponse.SC_FORBIDDEN,
                        "Refresh Token이 만료되었습니다. 재로그인 해주세요.");
            }
            sendErrorResponse((HttpServletResponse) response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Access Token이 만료되었습니다.");
        } catch (JwtException | IllegalArgumentException e) {
            sendErrorResponse((HttpServletResponse) response, HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, String> errors = new HashMap<>();
        errors.put("error", message);

        response.getWriter().write(new ObjectMapper().writeValueAsString(errors));
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
