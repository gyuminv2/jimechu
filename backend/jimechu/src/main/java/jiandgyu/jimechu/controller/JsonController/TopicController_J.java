package jiandgyu.jimechu.controller.JsonController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.Menu;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.dto.MemberDTO;
import jiandgyu.jimechu.dto.MenuDTO;
import jiandgyu.jimechu.dto.TopicCreateDTO;
import jiandgyu.jimechu.dto.TopicDTO;
import jiandgyu.jimechu.service.MenuService;
import jiandgyu.jimechu.service.TopicService;
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
@Tag(name = "Topic API", description = "주제 API")
public class TopicController_J {

    private final TopicService topicService;
    private final MenuService menuService;

    /**
     * Topic 생성 (JSON 요청 처리)
     */
    @PostMapping(value = "/topic/new", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Topic 생성", description = "특정 Member의 새로운 Topic을 생성합니다.")
    public Map<String, String> createTopic(@RequestBody TopicCreateDTO topicCreateDTO) {
        Long topicId = topicService.createTopic(topicCreateDTO.getMemberId(), topicCreateDTO.getTitle(), null);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Topic 생성 성공!");
        response.put("topicId", String.valueOf(topicId));
        return response;
    }

    /**
     * 전체 Topic 조회 (JSON 요청 처리)
     */
    @GetMapping(value = "/topics", produces = "application/json")
    @Operation(summary = "전체 Topic 조회", description = "전체 Topic을 조회합니다.")
    public List<TopicDTO> getAllTopics() {
        List<Topic> topics = topicService.findTopics();

        return topics.stream()
                .map(TopicDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 Topic의 menus 조회 (JSON 요청 처리)
     */
    @GetMapping(value = "/topics/{topicId}/menus", produces = "application/json")
    @Operation(summary = "특정 Topic의 Menus 조회", description = "특정 Topic의 Menu 목록을 반환합니다.")
    public List<MenuDTO> getMenusByTopic(@PathVariable Long topicId) {
        List<Menu> menus = menuService.getMenusByTopic(topicId);

        return menus.stream()
                .map(MenuDTO::new)
                .collect(Collectors.toList());
    }


//    /**
//     * 특정 Member의 Topics 조회 (JSON 요청 처리)
//     */
//    @GetMapping(value = "/members/{memberId}/topics", produces = "application/json")
//    @Operation(summary = "특정 Member의 Topics 조회", description = "특정 Member의 Topic 목록을 반환합니다.")
//    public List<TopicDTO> getTopicsByMember(@PathVariable Long memberId) {
//        List<Topic> topics = topicService.findTopicsByMemberId(memberId);
//
//        return topics.stream()
//                .map(TopicDTO::new)
//                .collect(Collectors.toList());
//    }

    /**
     * 특정 Topic 삭제 (JSON 요청 처리)
     */
    @DeleteMapping(value = "/{topicId}", produces = "application/json")
    @Operation(summary = "Topic 삭제", description = "특정 Topic을 삭제합니다.")
    public Map<String, String> deleteTopic(@PathVariable Long topicId) {
        topicService.deleteTopic(topicId);

        // 응답 메시지 작성
        Map<String, String> response = new HashMap<>();
        response.put("message", "Topic 삭제 성공!");
        return response;
    }

}
