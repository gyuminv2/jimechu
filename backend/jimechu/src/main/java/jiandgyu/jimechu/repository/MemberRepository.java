package jiandgyu.jimechu.repository;

import jakarta.persistence.EntityManager;
import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    /**
     * 멤버 저장
     * @param member
     */
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    /**
     * 멤버 id 조회
     * @param id
     * @return Member
     */
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    /**
     * 전체 멤버 찾기
     * @return List
     */
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    /**
     * String으로 멤버 찾기
     * @param Nickname
     * @return List
     */
    public List<Member> findByNickname(String Nickname) {
        return em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .setParameter("nickname", Nickname)
                .getResultList();
    }

    /**
     * Topic 조회
     */
    public List<Topic> findTopicsByMemberId(Long memberId) {
        return em.createQuery("select t from Topic t where t.member.id = :memberId", Topic.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
