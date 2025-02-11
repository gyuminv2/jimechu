package jiandgyu.jimechu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jiandgyu.jimechu.config.security.service.CustomMember;
import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.dto.member.MemberDTO;
import jiandgyu.jimechu.dto.topic.TopicDTO;
import jiandgyu.jimechu.service.FollowService;
import jiandgyu.jimechu.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "B | Member API", description = "회원 API")
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final FollowService followService;

    /**
     * 내 정보 (JSON 요청 처리)
     */
    @GetMapping(value = "/me", produces = "application/json")
    @Operation(summary = "내 정보", description = "현재 로그인한 나의 정보를 반환합니다.")
    public MemberDTO getCurrentUser(@AuthenticationPrincipal CustomMember customMember) {
        Long memberId = customMember.getMemberId();
        Member member = memberService.findOne(memberId);
        return new MemberDTO(member);
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

    /**
     * 특정 Member Follow (JSON 요청 처리)
     */
    @PostMapping(value = "follow/{targetMemberId}", produces = "application/json")
    @Operation(summary = "특정 멤버 Follow", description = "특정 맴버를 Follow합니다.")
    public Map<String, String> followMember(@AuthenticationPrincipal CustomMember customMember,
                                            @PathVariable("targetMemberId") Long targetMemberId) {
        if (customMember == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        followService.follow(customMember.getMemberId(), targetMemberId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Follow 성공!");
        return response;
    }

    /**
     * 특정 Member Unfollow (JSON 요청 처리)
     */
    @PostMapping(value = "unfollow/{targetMemberId}", produces = "application/json")
    @Operation(summary = "특정 멤버 Unfollow", description = "특정 맴버를 Unfollow합니다.")
    public Map<String, String> unfollowMember(@AuthenticationPrincipal CustomMember customMember,
                                              @PathVariable("targetMemberId") Long targetMemberId) {
        if (customMember == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        followService.unfollow(customMember.getMemberId(), targetMemberId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Unfollow 성공!");
        return response;
    }

    /**
     * 내가 Follow한 Following 조회 (JSON 요청 처리)
     */
    @GetMapping(value = "/followings", produces = "application/json")
    @Operation(summary = "내가 Follow한 Followings 조회", description = "내가 Follow한 Followings를 조회합니다.")
    public List<MemberDTO> getFollowers(@AuthenticationPrincipal CustomMember customMember) {
        if (customMember == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        List<Member> followers = followService.getFollowings(customMember.getMemberId());

        return followers.stream()
                .map(MemberDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 나를 Follow한 Follower 조회 (JSON 요청 처리)
     */
    @GetMapping(value = "/followers", produces = "application/json")
    @Operation(summary = "나를 Follow한 Followers 조회", description = "나를 Follow한 Followers를 조회합니다.")
    public List<MemberDTO> getFollowings(@AuthenticationPrincipal CustomMember customMember) {
        if (customMember == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        List<Member> followings = followService.getFollowers(customMember.getMemberId());

        return followings.stream()
                .map(MemberDTO::new)
                .collect(Collectors.toList());
    }
}
