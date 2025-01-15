package jiandgyu.jimechu.controller.JsonController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.dto.MemberDTO;
import jiandgyu.jimechu.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Tag(name = "Member API", description = "회원 API")
public class MemberController_J {

    private final MemberService memberService;

    /**
     * 회원 가입 폼 (JSON 요청 처리)
     */
    @GetMapping(value = "/members/new", produces = "application/json")
    @ResponseBody
    @Operation(summary = "회원 가입 DTO", description = "회원 가입 DTO을 반환합니다.")
    public MemberDTO createDTO() {
        return new MemberDTO();
    }

    /**
     * 회원 생성 (JSON 요청 처리)
     */
    @PostMapping(value = "/members/new", consumes = "application/json", produces = "application/json")
    @ResponseBody
    @Operation(summary = "회원 생성", description = "새로운 회원을 생성합니다. 'id'와 'topic'을 제거 후 생성하세요.")
    public Map<String, String> createMember(@RequestBody MemberDTO memberDto) {
        Member member = new Member();
        member.setNickname(memberDto.getNickname());
        memberService.join(member);

        // 이렇게 내 맘대로 response를 작성해도 되나?
        Map<String, String> response = new HashMap<>();
        response.put("message", "회원 생성 성공!");
        return response;
    }

    /**
     * 전체 회원 조회 (JSON 요청 처리)
     */
    @GetMapping(value = "/members", produces = "application/json")
    @ResponseBody
    @Operation(summary = "전체 회원 조회", description = "전체 회원을 리스트로 조회합니다.")
    public List<Member> getAllMembers() {
        return memberService.findMembers();
    }

    /**
     * 특정 회원 조회 (JSON 요청 처리)
     */
    @GetMapping(value = "/members/{memberId}", produces = "application/json")
    @ResponseBody
    @Operation(summary = "특정 회원 조회", description = "회원을 조회합니다.")
    public MemberDTO getMember(@PathVariable Long memberId) {
        Member member = memberService.findOne(memberId);
        return new MemberDTO(member);
    }
}
