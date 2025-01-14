package jiandgyu.jimechu.controller;

import jakarta.validation.Valid;
import jiandgyu.jimechu.controller.form.MemberForm;
import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 가입 클릭
     * @param model
     * @return String
     */
    @GetMapping(value = "/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    /**
     * 회원 가입 완료
     * @param form
     * @param result
     * @return String
     */
    @PostMapping(value = "/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Member member = new Member();
        member.setNickname(form.getNickname());
        memberService.join(member);

        return "redirect:/";
    }

    /**
     * 전체 회원 조회
     * @param model
     * @return String
     */
    @GetMapping(value = "/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
