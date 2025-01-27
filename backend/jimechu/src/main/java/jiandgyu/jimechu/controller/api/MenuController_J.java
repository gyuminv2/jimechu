package jiandgyu.jimechu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jiandgyu.jimechu.domain.Menu;
import jiandgyu.jimechu.dto.MenuCreateDTO;
import jiandgyu.jimechu.dto.MenuDTO;
import jiandgyu.jimechu.dto.MenuUpdateDTO;
import jiandgyu.jimechu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
@Tag(name = "Menu API", description = "메뉴 API")
public class MenuController_J {

    private final MenuService menuService;

    /**
     * Menu 생성 (JSON 요청 처리)
     */
    @PostMapping(value = "new", consumes = "application/json", produces = "application/json")
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
    @GetMapping(produces = "application/json")
    @Operation(summary = "전체 Menu 조회", description = "전체 Menu를 조회합니다.")
    public List<MenuDTO> getAllMenus() {
        List<Menu> menus = menuService.findMenus();

        return menus.stream()
                .map(MenuDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Menu Name 수정 (JSON 요청 처리)
     */
    @PatchMapping(value = "{menuId}", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Menu Name 수정", description = "특정 Menu의 Name을 수정합니다.")
    public Map<String, String> updateMenuName(@PathVariable("menuId") Long menuId, @RequestBody MenuUpdateDTO menuUpdateDTO) {
        menuService.updateMenuName(menuId, menuUpdateDTO.getName());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Menu Name 수정 성공!");
        response.put("updatedMenuName", menuUpdateDTO.getName());
        return response;
    }

    /**
     * 특정 Menu 삭제 (JSON 요청 처리)
     */
    @DeleteMapping(value = "{menuId}", produces = "application/json")
    @Operation(summary = "Menu 삭제", description = "특정 Menu를 삭제합니다.")
    public Map<String, String> deleteMenu(@PathVariable("menuId") Long menuId) {
        String deletedMenuName = menuService.getMenuNameById(menuId);
        menuService.deleteMenu(menuId);

        // 응답 메시지 작성
        Map<String, String> response = new HashMap<>();
        response.put("message", "Menu 삭제 성공!");
        response.put("deletedMenuName", deletedMenuName);
        return response;
    }
}
