package jiandgyu.jimechu.repository;

import jakarta.persistence.EntityManager;
import jiandgyu.jimechu.domain.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRoleRepository {

    private final EntityManager em;

    public MemberRole save(MemberRole memberRole) {
        em.persist(memberRole);
        return memberRole;
    }
}
