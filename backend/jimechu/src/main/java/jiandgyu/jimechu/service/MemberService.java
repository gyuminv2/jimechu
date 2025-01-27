package jiandgyu.jimechu.service;

import jiandgyu.jimechu.config.security.JwtTokenProvider;
import jiandgyu.jimechu.config.security.TokenInfo;
import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.MemberRole;
import jiandgyu.jimechu.domain.Role;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.dto.LoginRequestDTO;
import jiandgyu.jimechu.repository.MemberRepository;
import jiandgyu.jimechu.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원 가입
     * @param member
     * @return id
     */
    @Transactional // readOnly 적용 X
    public Long join(Member member) {
        verifyDuplicateMember(member);
        String encodePassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodePassword);
        memberRepository.save(member);
        // Role 설정
        MemberRole memberRole = MemberRole.builder()
                .role(Role.MEMBER)
                .member(member)
                .build();
        memberRoleRepository.save(memberRole);
        return member.getId();
    }

    /**
     * 중복 nickname 검증
     */
    private void verifyDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByNickname(member.getNickname());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("존재하는 닉네임입니다.");
        }
    }

    /**
     * 로그인
     */
    @Transactional
    public TokenInfo login(LoginRequestDTO requestDTO) {
        log.debug("login() called with nickname: {}, password: {}",
                requestDTO.getNickname(), requestDTO.getPassword());

        // 2) Create the authentication token
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        requestDTO.getNickname(),
                        requestDTO.getPassword()
                );
        log.debug("Created UsernamePasswordAuthenticationToken: {}", authenticationToken);

        // 3) Perform authentication
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.debug("Authentication successful. Principal: {}", authentication.getPrincipal());

        // 4) Generate JWT token
        TokenInfo tokenInfo = jwtTokenProvider.createToken(authentication);
        log.debug("Generated JWT token: {}", tokenInfo);

        // 5) Return the token
        return tokenInfo;
    }


    /**
     * 전체 회원 조회
     * @return List
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 1명 조회
     * @param memberId
     * @return Member
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /**
     * Topics 조회
     */
    public List<Topic> getTopicsByMember(Long memberId) {
        return memberRepository.findTopicsByMemberId(memberId);
    }
}
