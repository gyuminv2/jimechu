package jiandgyu.jimechu.dto.topic;

import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.domain.Visibility;
import jiandgyu.jimechu.dto.menu.MenuDTO;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TopicDTO {

    private Long id;
    private String title;
    private Visibility visibility;

    private List<MenuDTO> menus;

    public TopicDTO() {
        this.id = null;
        this.title = null;
        this.visibility = null;
        this.menus = null;
    }

    // DTO 생성
    public TopicDTO(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.visibility = topic.getVisibility();
        this.menus = topic.getMenus().stream()
                .map(MenuDTO::new)
                .collect(Collectors.toList());
    }
}
