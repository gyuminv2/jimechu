package jiandgyu.jimechu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jiandgyu.jimechu.config.security.JwtTokenProvider;
import jiandgyu.jimechu.config.security.RefreshTokenService;
import jiandgyu.jimechu.config.security.TokenInfo;
import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.dto.LoginRequestDTO;
import jiandgyu.jimechu.dto.LoginResponseDTO;
import jiandgyu.jimechu.dto.MemberCreateDTO;
import jiandgyu.jimechu.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "A | Auth API", description = "권한 API")
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    /**
     * 회원 가입 폼 (JSON 요청 처리)
     */
    @GetMapping(value = "news", produces = "application/json")
    @Operation(summary = "회원 가입 DTO", description = "회원 가입 DTO을 반환합니다.")
    public MemberCreateDTO createDTO() {
        return new MemberCreateDTO();
    }

    /**
     * Member 생성 (JSON 요청 처리)
     */
    @PostMapping(value = "new", consumes = "application/json", produces = "application/json")
    @Operation(summary = "회원 생성", description = "새로운 회원을 생성합니다.")
    public Map<String, String> createMember(@RequestBody MemberCreateDTO memberCreateDto) {
        Member member = new Member();
        member.setNickname(memberCreateDto.getNickname());
        member.setPassword(memberCreateDto.getPassword());
        memberService.join(member);

        Map<String, String> response = new HashMap<>();
        response.put("message", "회원 생성 성공!");
        return response;
    }

    /**
     * Member 로그인 (JSON 요청 처리)
     */
    @PostMapping(value = "login", consumes = "application/json", produces = "application/json")
    @Operation(summary = "회원 로그인", description = "로그인 및 JWT를 발급합니다..")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO requestDTO) {
        TokenInfo tokenInfo = memberService.login(requestDTO);

        refreshTokenService.saveRefreshToken(requestDTO.getNickname(), tokenInfo.getRefreshToken());

        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setAccessToken(tokenInfo.getAccessToken());
        responseDTO.setRefreshToken(tokenInfo.getRefreshToken());
        responseDTO.setNickname(requestDTO.getNickname());

        return responseDTO;
    }

    /**
     * Member 로그아웃 (JSON 요청 처리)
     */
    @PostMapping(value = "logout", produces = "application/json")
    @Operation(summary = "회원 로그아웃", description = "로그아웃 및 Redis에서 Refresh Token을 삭제합니다.")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String accessToken, Principal principal) {
        if (principal == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "로그인한 사용자만 로그아웃할 수 있습니다."));
        }

        String token = accessToken.replace("Bearer ", "");
        String nickname = principal.getName(); // 현재 로그인한 사용자의 닉네임

        // 1. Refresh Token 삭제
        refreshTokenService.deleteRefreshToken(nickname);

        // 2. Access Token 블랙리스트에 추가
        Date expiration = jwtTokenProvider.getExpiration(token);
        refreshTokenService.blacklistAccessToken(token, expiration);

        Map<String, String> response = new HashMap<>();
        response.put("message", "로그아웃 성공!");
        return ResponseEntity.ok(response);
    }
}
