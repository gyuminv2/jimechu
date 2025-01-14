package jiandgyu.jimechu.repository;

import jakarta.persistence.EntityManager;
import jiandgyu.jimechu.domain.Menu;
import jiandgyu.jimechu.domain.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TopicRepository {

    private final EntityManager em;

    public void save(Topic topic) {
        em.persist(topic);
    }

    public Topic findOne(Long id) {
        return em.find(Topic.class, id);
    }

    public List<Topic> findAll() {
        return em.createQuery("select t from Topic t", Topic.class)
                .getResultList();
    }

}
