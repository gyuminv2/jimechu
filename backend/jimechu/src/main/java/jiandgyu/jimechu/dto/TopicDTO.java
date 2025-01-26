package jiandgyu.jimechu.dto;

import jiandgyu.jimechu.domain.Menu;
import jiandgyu.jimechu.domain.Topic;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class TopicDTO {

    private Long id;
    private String title;

    private List<MenuDTO> menus;

    public TopicDTO() {
        this.id = null;
        this.title = null;
        this.menus = null;
    }

    // DTO 생성
    public TopicDTO(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.menus = topic.getMenus().stream()
                .map(MenuDTO::new)
                .collect(Collectors.toList());
    }
}
