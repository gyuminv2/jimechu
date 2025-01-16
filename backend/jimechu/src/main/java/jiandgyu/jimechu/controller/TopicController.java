package jiandgyu.jimechu.controller;

import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.Menu;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.service.MemberService;
import jiandgyu.jimechu.service.MenuService;
import jiandgyu.jimechu.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;
    private final MemberService memberService;
    private final MenuService menuService;

    /**
     * Topic 생성 폼
     */
    @GetMapping(value = "/topic/new")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "topics/topicForm";
    }

    /**
     * Topic 생성
     */
    @PostMapping(value = "/topic/new")
    public String createTopic(@RequestParam("memberId") Long memberId,
                              @RequestParam("title") String title) {
        topicService.createTopic(memberId, title, new ArrayList<>());
        return "redirect:/topics";
    }

    /**
     * 전체 Topic 리스트 조회
     *
     * @param model
     * @return String
     */
    @GetMapping(value = "/topics")
    public String list(Model model) {
        List<Topic> topics = topicService.findTopics();
        model.addAttribute("topics", topics);
        return "topics/topicList";
    }
}
