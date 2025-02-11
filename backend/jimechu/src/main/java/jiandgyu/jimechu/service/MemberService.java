package jiandgyu.jimechu.service;

import jiandgyu.jimechu.config.security.jwt.JwtTokenProvider;
import jiandgyu.jimechu.config.security.service.RefreshTokenService;
import jiandgyu.jimechu.config.security.jwt.TokenInfo;
import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.MemberRole;
import jiandgyu.jimechu.domain.Role;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.dto.auth.LoginRequestDTO;
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
    private final RefreshTokenService refreshTokenService;

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

        // 1) 사용자 입력 기반 UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        requestDTO.getNickname(),
                        requestDTO.getPassword()
                );
        log.debug("Created UsernamePasswordAuthenticationToken: {}", authenticationToken);

        /**
         * 2) AuthenticationManager로 인증 수행
         * authenticate() 메서드 호출 시, AuthenticationProvider의 authenticate() 메서드가 호출됨
         * -> DaoAuthenticationProvider에서 CustomUserDetailService의 loadUserByUsername() 메서드 호출
         * -> CustomUserDetailService에서 사용자 정보 조회
         */
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.debug("Authentication successful. Principal: {}", authentication.getPrincipal());

        // 3) SecurityContext에 Authentication 객체 저장
        TokenInfo tokenInfo = jwtTokenProvider.createToken(authentication);
        log.debug("Generated JWT token: {}", tokenInfo);

        // 4) RefreshToken 저장
        refreshTokenService.saveRefreshToken(requestDTO.getNickname(), tokenInfo.getRefreshToken());

        // 5) TokenInfo 반환
        return tokenInfo;
    }

//    /**
//     * 로그아웃
//     */
//    @Transactional
//    public void logout(String accessToken) {
//        if (accessToken == null || accessToken.isEmpty()) {
//            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
//        }
//    }

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
     * 닉네임으로 회원 조회
     * @param nickname
     * @return Member
     */
    public List<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    /**
     * Topics 조회
     */
    public List<Topic> getTopicsByMember(Long memberId) {
        return memberRepository.findTopicsByMemberId(memberId);
    }
}
