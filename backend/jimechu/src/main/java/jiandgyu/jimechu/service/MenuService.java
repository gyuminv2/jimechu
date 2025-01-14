package jiandgyu.jimechu.service;


import jiandgyu.jimechu.domain.Menu;
import jiandgyu.jimechu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    /**
     * 메뉴 추가
     * @param menu
     */
    @Transactional
    public void saveMenu(Menu menu) {
        menuRepository.save(menu);
    }

    /**
     * 전체 메뉴 조회
     * @return List
     */
    public List<Menu> findMenus() {
        return menuRepository.findAll();
    }

    /**
     * 메뉴 1개 조회
     * @param menuId
     * @return Menu
     */
    public Menu findOne(Long menuId) {
        return menuRepository.findOne(menuId);
    }
}
