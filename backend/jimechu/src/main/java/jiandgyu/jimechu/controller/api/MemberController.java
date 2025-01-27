package jiandgyu.jimechu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jiandgyu.jimechu.config.security.TokenInfo;
import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.dto.*;
import jiandgyu.jimechu.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "Member API", description = "회원 API")
@RequestMapping("/api/members")
public class MemberController {

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

    /**
     * 전체 Member 조회 (JSON 요청 처리)
     */
    @GetMapping(produces = "application/json")
    @Operation(summary = "전체 회원 조회", description = "전체 회원을 조회합니다.")
    public List<MemberDTO> getAllMembers() {
        List<Member> members = memberService.findMembers();

        return members.stream()
                .map(MemberDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 Member 조회 (JSON 요청 처리)
     */
    @GetMapping(value = "{memberId}", produces = "application/json")
    @Operation(summary = "특정 회원 조회", description = "회원을 조회합니다.")
    public MemberDTO getMember(@PathVariable("memberId") Long memberId) {
        Member member = memberService.findOne(memberId);
        return new MemberDTO(member);
    }

    /**
     * 특정 Member의 Topics 조회 (JSON 요청 처리)
     */
    @GetMapping(value = "/{memberId}/topics", produces = "application/json")
    @Operation(summary = "특정 멤버의 Topic 조회", description = "특정 맴버가 생성한 Topic 목록을 반환합니다.")
    public List<TopicDTO> getTopicsByMember(@PathVariable("memberId") Long memberId) {
        List<Topic> topics = memberService.getTopicsByMember(memberId);
        List<TopicDTO> topicDTOs = new ArrayList<>();

        for (Topic topic : topics) {
            TopicDTO topicDTO = new TopicDTO(topic);
            topicDTOs.add(topicDTO);
        }
        return topicDTOs;
    }
}
