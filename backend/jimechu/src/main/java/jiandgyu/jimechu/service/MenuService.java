package jiandgyu.jimechu.service;


import jiandgyu.jimechu.domain.Menu;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.repository.MenuRepository;
import jiandgyu.jimechu.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final TopicRepository topicRepository;

    /**
     * Menu 생성
     */
    @Transactional
    public Long createMenu(Long topicId, String name) {
        Topic topic = topicRepository.findOne(topicId);
        if (topic == null) {
            throw new IllegalArgumentException("존재하지 않는 topicId : " + topicId);
        }
        Menu menu = Menu.createMenu(name, topic);

        menuRepository.save(menu);
        return menu.getId();
    }

    /**
     * 전체 Menu 조회
     */
    public List<Menu> findMenus() {
        return menuRepository.findAll();
    }

    /**
     * 메뉴 1개 조회
     */
    public Menu findOne(Long menuId) {
        return menuRepository.findOne(menuId);
    }

    /**
     * 특정 Topic에 속한 Menu 조회
     */
    public List<Menu> getMenusByTopic(Long topicId) {
        return menuRepository.findAllByTopicId(topicId);
    }

    /**
     * 영속성 컨텍스트가 자동 변경
     */
    @Transactional
    public void updateMenu(Long id, String name) {
        Menu menu = menuRepository.findOne(id);
        menu.setName(name);
    }

    /**
     * 메뉴 삭제
     */
    @Transactional
    public void deleteMenu(Long menuId) {
        menuRepository.delete(menuId);
    }

    public String getMenuNameById(Long menuId) {
        return menuRepository.findOne(menuId).getName();
    }
}
