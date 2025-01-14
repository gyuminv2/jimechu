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

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;
    private final MemberService memberService;
    private final MenuService menuService;

    @GetMapping(value = "/topic")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
//        List<Menu> menus = menuService.findMenus();

        model.addAttribute("members", members);
//        model.addAttribute("menus", menus);

        return "topics/topicForm";
    }

    @PostMapping(value = "/topic")
    public String topic(@RequestParam("memberId") Long memberId) {
//        topicService.topic(memberId, menuId);
        topicService.topic(memberId);
        return "redirect:/topics";
    }

    /**
     * 전체 주제 리스트
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
