package jiandgyu.jimechu.unit.service;

import jiandgyu.jimechu.config.security.JwtTokenProvider;
import jiandgyu.jimechu.config.security.TokenInfo;
import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.MemberRole;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.dto.LoginRequestDTO;
import jiandgyu.jimechu.repository.MemberRepository;
import jiandgyu.jimechu.repository.MemberRoleRepository;
import jiandgyu.jimechu.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@Transactional
class MemberServiceTest {

    @Mock private MemberRepository memberRepository;
    @Mock private MemberRoleRepository memberRoleRepository;
    @Mock private BCryptPasswordEncoder passwordEncoder;
    @Mock private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock private Authentication mockAuthentication;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @InjectMocks private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void 회원가입() {
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

    @Test
    public void 로그인_성공() {
        // given
        String nickname = "지나";
        String password = "지나123";
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setNickname(nickname);
        requestDTO.setPassword(password);

        TokenInfo mockTokenInfo = new TokenInfo("Bearer", "mockAccessToken");

        // Mock 설정
        when(authenticationManagerBuilder.getObject()).thenReturn(authentication -> mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(nickname);
        when(jwtTokenProvider.createToken(mockAuthentication)).thenReturn(mockTokenInfo);

        // when
        TokenInfo tokenInfo = memberService.login(requestDTO);

        // then
        assertNotNull(tokenInfo, "TokenInfo should not be null");
        assertEquals("Bearer", tokenInfo.getGrantType());
        assertEquals("mockAccessToken", tokenInfo.getAccessToken());
    }

    @Test
    public void 로그인_실패() {
        // given
        String nickname = "invalid지나";
        String password = "invalid지나123";
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setNickname(nickname);
        requestDTO.setPassword(password);

        // Mock : Authentication 실패
        when(authenticationManagerBuilder.getObject())
                .thenReturn(authentication -> {
                    throw new RuntimeException("Authentication failed");
                });

        // when
        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class, () -> {
                    memberService.login(requestDTO);
                });

        // then
        assertEquals("Authentication failed", exception.getMessage());
        verify(authenticationManagerBuilder, times(1)).getObject();
        verify(jwtTokenProvider, never()).createToken(any(Authentication.class));
    }

    @Test
    public void 저장된_토픽_조회() {
        // given
        Member member = getMember();

        Topic topic = new Topic();
        topic.setMember(member);
        topic.setTitle("지메추");
        when(memberRepository.findTopicsByMemberId(1L)).thenReturn(List.of(topic));

        // when
        List<Topic> topics = memberService.getTopicsByMember(1L);

        // then
        assertNotNull(topics);
        assertEquals(1, topics.size());
        assertEquals("지메추", topics.get(0).getTitle());
    }




    private static Member getMember() {
        Member member = new Member();
        member.setId(1L);
        member.setNickname("지나");
        member.setPassword("지나123");
        return member;
    }


}