package jiandgyu.jimechu.service;

import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        Member member = getMember();

        Long saveId = memberService.join(member);

        assertEquals(member, memberRepository.findOne(saveId));
    }

    private static Member getMember() {
        Member member = new Member();
        member.setNickname("지나");
        member.setPassword("지나123");
        return member;
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        Member member1 = getMember();
        Member member2 = new Member();

        member2.setNickname("지나"); member2.setPassword("지나321");

        memberService.join(member1);
        memberService.join(member2);

        fail("예외 발생 (중복 닉네임 사용)");
    }

    @Test
    public void login() {
    }

    @Test
    public void findMembers() {
    }

    @Test
    public void findOne() {
    }

    @Test
    public void getTopicsByMember() {
    }
}