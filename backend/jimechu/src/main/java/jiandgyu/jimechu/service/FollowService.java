package jiandgyu.jimechu.service;

import jiandgyu.jimechu.domain.Follow;
import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.repository.FollowRepository;
import jiandgyu.jimechu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    // 팔로우
    @Transactional
    public void follow(Long myId, Long targetMemberId) {

        // 자기 자신 팔로우 불가
        if (myId.equals(targetMemberId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        Member member = memberRepository.findOne(myId);
        Member targetMember = memberRepository.findOne(targetMemberId);

        if (member == null || targetMember == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        // 이미 팔로우한 회원인지 확인
        if (followRepository.existsByFollowerAndFollowing(member, targetMember)) {
            throw new IllegalArgumentException("이미 팔로우한 회원입니다.");
        }

        Follow follow = Follow.createFollow(member, targetMember);
        followRepository.save(follow);
    }

    // 언팔로우
    @Transactional
    public void unfollow(Long myId, Long targetMemberId) {
        Member member = memberRepository.findOne(myId);
        Member targetMember = memberRepository.findOne(targetMemberId);

        if (member == null || targetMember == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        Follow follow = followRepository.findByFollowerAndFollowing(myId, targetMemberId);
        if (follow == null) {
            throw new IllegalArgumentException("팔로우하지 않은 회원입니다.");
        }

        followRepository.delete(follow);
    }

    // 내가 팔로우한 회원 조회
    public List<Member> getFollowings(Long myId) {
        Member member = memberRepository.findOne(myId);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        return followRepository.findFollowingMembers(member);
    }

    // 나를 팔로우한 회원 조회
    public List<Member> getFollowers(Long myId) {
        Member member = memberRepository.findOne(myId);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        return followRepository.findFollowerMembers(member);
    }
}
