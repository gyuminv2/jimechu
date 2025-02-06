package jiandgyu.jimechu.repository;

import jakarta.persistence.EntityManager;
import jiandgyu.jimechu.domain.Follow;
import jiandgyu.jimechu.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowRepository {

    private final EntityManager em;

    public void save(Follow follow) {
        em.persist(follow);
    }

    public Follow findByFollowerAndFollowing(Long followerId, Long followingId) {
        return em.createQuery("select f from Follow f where f.follower.id = :followerId and f.following.id = :followingId", Follow.class)
                .setParameter("followerId", followerId)
                .setParameter("followingId", followingId)
                .getSingleResult();
    }

    public boolean existsByFollowerAndFollowing(Member member, Member targetMember) {
        return em.createQuery("select count(f) from Follow f where f.follower = :member and f.following = :targetMember", Long.class)
                .setParameter("member", member)
                .setParameter("targetMember", targetMember)
                .getSingleResult() > 0;
    }

    public List<Member> findFollowingMembers(Member member) {
        return em.createQuery("select f.following from Follow f where f.follower = :member", Member.class)
                .setParameter("member", member)
                .getResultList();
    }

    public List<Member> findFollowerMembers(Member member) {
        return em.createQuery("select f.follower from Follow f where f.following = :member", Member.class)
                .setParameter("member", member)
                .getResultList();
    }

    public void delete(Follow follow) {
        em.remove(follow);
    }
}
