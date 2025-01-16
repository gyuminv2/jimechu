package jiandgyu.jimechu.service;

import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     * @param member
     * @return id
     */
    @Transactional // readOnly 적용 X
    public Long join(Member member) {
//        verifyDuplicateMember(member); // 로그인 기능 임시로... 대기.
        memberRepository.save(member);
        return member.getId();
    }

    private void verifyDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByNickname(member.getNickname());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("존재하는 닉네임입니다.");
        }
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
        return memberRepository.findAllByMemberId(memberId);
    }
}
