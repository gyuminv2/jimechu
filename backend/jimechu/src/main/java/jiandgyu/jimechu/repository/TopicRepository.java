package jiandgyu.jimechu.repository;

import jakarta.persistence.EntityManager;
import jiandgyu.jimechu.domain.Menu;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.domain.Visibility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TopicRepository {

    private final EntityManager em;

    public Topic save(Topic topic) {
        em.persist(topic);
        return topic;
    }

    public Topic findOne(Long id) {
        return em.find(Topic.class, id);
    }

    public List<Topic> findAll() {
        return em.createQuery("select t from Topic t", Topic.class)
                .getResultList();
    }

    /**
     * 특정 Member에 속한 Topic 조회
     */
    public List<Topic> findAllByMemberId(Long memberId) {
        return em.createQuery("select t from Topic t where t.member.id = :memberId", Topic.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    /**
     * 특정 Topic에 속한 Menus 조회
     */
    public List<Menu> findMenusByTopicId(Long topicId) {
        return em.createQuery("select m from Menu m where m.topic.id = :topicId", Menu.class)
                .setParameter("topicId", topicId)
                .getResultList();
    }

    /**
     * Topic update (title 바꾸기)
     */
    public void update(Long topicId, String title) {
        Topic topic = findOne(topicId);
        if (topic != null) {
            topic.setTitle(title);
        }
    }

    /**
     * Topic 삭제, topic이 삭제되면 얘와 연관된 menu들도 전부 삭제
     */
    public void delete(Long topicId) {
        Topic topic = findOne(topicId);
        if (topic != null) {
            em.remove(topic);
        }
    }

    /**
     * 공개(PUBLIC) 주제만 조회
     */
    public List<Topic> findPublicTopics() {
        return em.createQuery("SELECT t FROM Topic t WHERE t.visibility = :visibility", Topic.class)
                .setParameter("visibility", Visibility.PUBLIC)
                .getResultList();
    }

    /**
     * 로그인 사용자의 개인 + 공개 주제 조회
     */
    public List<Topic> findPublicAndPrivateTopicsByMemberId(Long memberId) {
        return em.createQuery(
                        "SELECT t FROM Topic t WHERE t.visibility = 'PUBLIC' OR t.member.id = :memberId",
                        Topic.class
                ).setParameter("memberId", memberId)
                .getResultList();
    }
}
