package jiandgyu.jimechu.controller.web;

import jiandgyu.jimechu.controller.form.MenuForm;
import jiandgyu.jimechu.domain.Menu;
import jiandgyu.jimechu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * 메뉴 생성 폼
     * @param model
     * @return String
     */
    @GetMapping(value = "/menus/new")
    public String createForm(Model model) {
        model.addAttribute("form", new MenuForm());
        return "menus/createMenuForm";
    }

    /**
     * 메뉴 생성
     * @param form
     * @return String
     */
    @PostMapping(value = "menus/new")
    public String create(MenuForm form) {
        Menu menu = new Menu();
        menu.setName(form.getName());

//        menuService.saveMenu(menu);
        return "redirect:/menus";
    }

    /**
     * 전체 메뉴 리스트
     * @param model
     * @return String
     */
    @GetMapping(value = "/menus")
    public String list(Model model) {
        List<Menu> menus = menuService.findMenus();
        model.addAttribute("menus", menus);
        return "menus/menuList";
    }

    /**
     * 메뉴 수정 폼
     * @param menuId
     * @param model
     * @return
     */
    @GetMapping(value = "/menus/{menuId}/edit")
    public String updateMenuForm(@PathVariable("menuId") Long menuId, Model model) {
        Menu menu = menuService.findOne(menuId);

        MenuForm form = new MenuForm();
        form.setId(menu.getId());
        form.setName(menu.getName());

        model.addAttribute("form", form);
        return "menus/updateMenuForm";
    }

    /**
     * 메뉴 수정
     * @param menuId
     * @param form
     * @return String
     */
    @PostMapping(value = "/menus/{menuId}/edit")
    public String updateMenu(@PathVariable Long menuId, @ModelAttribute("form")MenuForm form) {
        menuService.updateMenu(menuId, form.getName());
        return "redirect:/menus";
    }

    /**
     * 메뉴 삭제
     * @param menuId
     * @return String
     */
    @PostMapping(value = "/menus/{menuId}/delete")
    public String deleteMenu(@PathVariable("menuId") Long menuId) {
        menuService.deleteMenu(menuId);
        return "redirect:/menus";
    }

//    @PostMapping(value = "/menus/{menuId}/edit")
//    public String updateMenu(@ModelAttribute("form") MenuForm form) {
//        Menu menu = new Menu();
//        menu.setId(form.getId());
//        menu.setName(form.getName());
//
//        menuService.saveMenu(menu);
//        return "redirect:/menus";
//    }
}
