package jiandgyu.jimechu.repository;

import jakarta.persistence.EntityManager;
import jiandgyu.jimechu.domain.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuRepository {

    private final EntityManager em;

    public void save(Menu menu) {
        if (menu.getId() == null) {
            em.persist(menu);
        } else {
            em.merge(menu); // update.
        }
    }

    public Menu findOne(Long id) {
        return em.find(Menu.class, id);
    }

    public List<Menu> findAll() {
        return em.createQuery("select m from Menu m", Menu.class)
                .getResultList();
    }

    /**
     * 특정 Topic에 속한 Menu 조회
     */
    public List<Menu> findAllByTopicId(Long topicId) {
        return em.createQuery("select m from Menu m where m.topic.id = :topicId", Menu.class)
                .setParameter("topicId", topicId)
                .getResultList();
    }

    public void delete(Long menuId) {
        // 삭제할 메뉴 조회
        Menu menu = em.find(Menu.class, menuId);
        if (menu != null) {
            em.remove(menu);
        }
    }
}
