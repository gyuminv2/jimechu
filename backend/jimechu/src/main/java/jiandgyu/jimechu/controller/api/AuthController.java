package jiandgyu.jimechu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jiandgyu.jimechu.config.security.TokenInfo;
import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.dto.LoginRequestDTO;
import jiandgyu.jimechu.dto.LoginResponseDTO;
import jiandgyu.jimechu.dto.MemberCreateDTO;
import jiandgyu.jimechu.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "A | Auth API", description = "권한 API")
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;

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
    @Operation(summary = "회원 로그인", description = "회원으로 로그인합니다.")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO requestDTO) {
        TokenInfo tokenInfo = memberService.login(requestDTO);

        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setToken(tokenInfo.getAccessToken());
        responseDTO.setNickname(requestDTO.getNickname());

        return responseDTO;
    }
}
