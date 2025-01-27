package jiandgyu.jimechu.unit.service;

import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.MemberRole;
import jiandgyu.jimechu.dto.MemberCreateDTO;
import jiandgyu.jimechu.repository.MemberRepository;
import jiandgyu.jimechu.repository.MemberRoleRepository;
import jiandgyu.jimechu.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

//@SpringBootTest
@Transactional
class MemberServiceTest {

    @Mock private MemberRepository memberRepository;
    @Mock private MemberRoleRepository memberRoleRepository;
    @Mock private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks private MemberService memberService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void 회원가입() throws Exception {
        // given
        Member member = getMember();

        // Mock : 중복 닉네임 검증
        when(memberRepository.findByNickname("지나")).thenReturn(Collections.emptyList());
        // Mock : 비밀번호 암호화
        when(passwordEncoder.encode("지나123")).thenReturn("암호화된비밀번호");
        // Mock : 회원 저장
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
            Member savedMember = invocation.getArgument(0);
            savedMember.setId(1L);
            return savedMember;
        });

        // Mock : Role 저장
        when(memberRoleRepository.save(any(MemberRole.class))).thenReturn(null);

        // when
        Long saveId = memberService.join(member);

        // then
        assertNotNull(saveId);
        assertEquals(1L, saveId);
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(memberRoleRepository, times(1)).save(any(MemberRole.class));
    }

    @Test
    public void 중복_닉네임_예외() {
        // given
        Member member = getMember();

        // Mock : 중복 닉네임 검증
        when(memberRepository.findByNickname("지나")).thenReturn(List.of(new Member()));

        // when
        IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class, () -> {
                    memberService.join(member);
                });

        // then
        assertEquals("존재하는 닉네임입니다.", exception.getMessage());
        verify(memberRepository, never()).save(any(Member.class));
    }

    private static Member getMember() {
        Member member = new Member();
        member.setNickname("지나");
        member.setPassword("지나123");
        return member;
    }

}