package jiandgyu.jimechu.controller.JsonController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jiandgyu.jimechu.domain.Menu;
import jiandgyu.jimechu.dto.MenuCreateDTO;
import jiandgyu.jimechu.dto.MenuDTO;
import jiandgyu.jimechu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "Menu API", description = "메뉴 API")
public class MenuController_J {

    private final MenuService menuService;

    /**
     * Menu 생성 (JSON 요청 처리)
     */
    @PostMapping(value = "/menu/new", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Menu 생성", description = "특정 Topic의 새로운 Menu를 생성합니다.")
    public Map<String, String> createMenu(@RequestBody MenuCreateDTO menuCreateDTO) {
        Long menuId = menuService.createMenu(menuCreateDTO.getTopicId(), menuCreateDTO.getName());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Menu 생성 성공!");
        response.put("menuId", String.valueOf(menuId));
        return response;
    }

    /**
     * 전체 Menu 조회 (JSON 요청 처리)
     */
    @GetMapping(value = "/menus", produces = "application/json")
    @Operation(summary = "전체 Menu 조회", description = "전체 Menu를 조회합니다.")
    public List<MenuDTO> getAllMenus() {
        List<Menu> menus = menuService.findMenus();

        return menus.stream()
                .map(MenuDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 Topic의 menus 조회 (JSON 요청 처리)
     */
    @GetMapping(value = "/topics/{topicId}/menus", produces = "application/json")
    @Operation(summary = "특정 Topic의 Menus 조회", description = "특정 Topic의 Menu 목록을 반환합니다.")
    public List<MenuDTO> getMenusByTopic(@PathVariable Long topicId) {
        List<Menu> menus = menuService.findMenusByTopic(topicId);

        return menus.stream()
                .map(MenuDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 Menu 삭제 (JSON 요청 처리)
     */
    @DeleteMapping(value = "/{menuId}", produces = "application/json")
    @Operation(summary = "Menu 삭제", description = "특정 Menu를 삭제합니다.")
    public Map<String, String> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);

        // 응답 메시지 작성
        Map<String, String> response = new HashMap<>();
        response.put("message", "Menu 삭제 성공!");
//        response.put("menuId", String.valueOf(menuId));
        return response;
    }

}
