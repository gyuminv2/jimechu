package jiandgyu.jimechu.dto;

import jiandgyu.jimechu.domain.Menu;
import lombok.Data;

@Data
public class MenuDTO {

    private Long id;
    private String name;

    public MenuDTO() {
        this.id = null;
        this.name = "null";
    }

    public MenuDTO(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
    }
}