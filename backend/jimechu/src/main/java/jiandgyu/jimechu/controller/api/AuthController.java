package jiandgyu.jimechu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jiandgyu.jimechu.config.security.jwt.TokenInfo;
import jiandgyu.jimechu.dto.auth.LoginRequestDTO;
import jiandgyu.jimechu.dto.auth.LoginResponseDTO;
import jiandgyu.jimechu.dto.member.MemberCreateDTO;
import jiandgyu.jimechu.service.AuthService;
import jiandgyu.jimechu.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "A | Auth API", description = "권한 API")
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;
    private final AuthService authService;

    /**
     * 회원 가입 폼 (JSON 요청 처리)
     */
    @GetMapping(value = "news", produces = "application/json")
    @Operation(summary = "회원 가입 DTO", description = "회원 가입 DTO를 반환합니다.")
    public MemberCreateDTO createDTO() {
        return new MemberCreateDTO();
    }

    /**
     * Member 생성 (JSON 요청 처리)
     */
    @PostMapping(value = "new", consumes = "application/json", produces = "application/json")
    @Operation(summary = "회원 생성", description = "새로운 회원을 생성합니다.")
    public ResponseEntity<Map<String, String>> createMember(@RequestBody MemberCreateDTO memberCreateDto) {
        memberService.join(memberCreateDto.toMember());
        return ResponseEntity.ok(Map.of("message", "회원 생성 성공!"));
    }

    /**
     * Member 로그인 (JSON 요청 처리)
     */
    @PostMapping(value = "login", consumes = "application/json", produces = "application/json")
    @Operation(summary = "회원 로그인", description = "로그인 및 JWT를 발급합니다.")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO requestDTO) {
        TokenInfo tokenInfo = authService.login(requestDTO);
        return new LoginResponseDTO(tokenInfo.getAccessToken(), tokenInfo.getRefreshToken(), requestDTO.getNickname());
    }

    /**
     * Member 로그아웃 (JSON 요청 처리)
     */
    @PostMapping(value = "logout", produces = "application/json")
    @Operation(summary = "회원 로그아웃", description = "로그아웃 및 Redis에서 Refresh Token을 삭제합니다.")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String accessToken,
                                                      Principal principal) {
        if (principal == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "로그인한 사용자만 로그아웃할 수 있습니다."));
        }
        String token = accessToken.replace("Bearer ", "");
        authService.logout(token, principal.getName());
        return ResponseEntity.ok(Map.of("message", "로그아웃 성공!"));
    }

    /**
     * Refresh Token 재발급 (JSON 요청 처리)
     * 클라이언트는 header에 access token, body에 refresh token을 전달합니다.
     */
    @PostMapping(value = "refresh", produces = "application/json")
    @Operation(summary = "Access Token 재발급", description = "Refresh Token을 이용하여 새로운 Access Token을 발급하고, 기존 Access Token을 블랙리스트에 등록합니다.")
    public ResponseEntity<?> refreshToken(HttpServletRequest request,
                                          @RequestBody RefreshTokenRequestDTO refreshRequest) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Access token이 header에 존재하지 않습니다."));
        }
        String accessToken = authHeader.substring(7);
        try {
            TokenInfo tokenInfo = authService.refreshToken(accessToken, refreshRequest.getRefreshToken());
            return ResponseEntity.ok(Map.of(
                    "accessToken", tokenInfo.getAccessToken(),
                    "refreshToken", tokenInfo.getRefreshToken()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @Data
    public static class RefreshTokenRequestDTO {
        private String refreshToken;
    }
}
