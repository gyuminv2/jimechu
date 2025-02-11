package jiandgyu.jimechu.service;

import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.MemberRole;
import jiandgyu.jimechu.domain.Role;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.repository.MemberRepository;
import jiandgyu.jimechu.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        verifyDuplicateMember(member);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);

        // Role 설정
        MemberRole memberRole = MemberRole.builder()
                .role(Role.MEMBER)
                .member(member)
                .build();
        memberRoleRepository.save(memberRole);
        return member.getId();
    }

    private void verifyDuplicateMember(Member member) {
        List<Member> existing = memberRepository.findByNickname(member.getNickname());
        if (!existing.isEmpty()) {
            throw new IllegalStateException("존재하는 닉네임입니다.");
        }
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    public List<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public List<Topic> getTopicsByMember(Long memberId) {
        return memberRepository.findTopicsByMemberId(memberId);
    }
}