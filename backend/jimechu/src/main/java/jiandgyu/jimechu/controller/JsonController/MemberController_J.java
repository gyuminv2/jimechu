package jiandgyu.jimechu.controller.JsonController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.dto.MemberCreateDTO;
import jiandgyu.jimechu.dto.MemberDTO;
import jiandgyu.jimechu.dto.TopicDTO;
import jiandgyu.jimechu.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "Member API", description = "회원 API")
public class MemberController_J {

    private final MemberService memberService;

    /**
     * 회원 가입 폼 (JSON 요청 처리)
     */
    @GetMapping(value = "/members/new", produces = "application/json")
    @Operation(summary = "회원 가입 DTO", description = "회원 가입 DTO을 반환합니다.")
    public MemberDTO createDTO() {
        return new MemberDTO();
    }

    /**
     * Member 생성 (JSON 요청 처리)
     */
    @PostMapping(value = "/members/new", consumes = "application/json", produces = "application/json")
    @Operation(summary = "회원 생성", description = "새로운 회원을 생성합니다.")
    public Map<String, String> createMember(@RequestBody MemberCreateDTO memberCreateDto) {
        Member member = new Member();
        member.setNickname(memberCreateDto.getNickname());
        memberService.join(member);

        Map<String, String> response = new HashMap<>();
        response.put("message", "회원 생성 성공!");
        return response;
    }

    /**
     * 전체 Member 조회 (JSON 요청 처리)
     */
    @GetMapping(value = "/members", produces = "application/json")
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
    @GetMapping(value = "/members/{memberId}", produces = "application/json")
    @Operation(summary = "특정 회원 조회", description = "회원을 조회합니다.")
    public MemberDTO getMember(@PathVariable Long memberId) {
        Member member = memberService.findOne(memberId);
        return new MemberDTO(member);
    }

    /**
     * 특정 Member의 Topics 조회 (JSON 요청 처리)
     */
    @GetMapping(value = "/members/{memberId}/topics", produces = "application/json")
    @ResponseBody
    @Operation(summary = "특정 멤버의 Topic 조회", description = "특정 맴버가 생성한 Topic 목록을 반환합니다.")
    public List<TopicDTO> getTopicsByMember(@PathVariable Long memberId) {
        List<Topic> topics = memberService.getTopicsByMember(memberId);
        List<TopicDTO> topicDTOs = new ArrayList<>();

        for (Topic topic : topics) {
            TopicDTO topicDTO = new TopicDTO(topic);
            topicDTOs.add(topicDTO);
        }
        return topicDTOs;
    }
}
