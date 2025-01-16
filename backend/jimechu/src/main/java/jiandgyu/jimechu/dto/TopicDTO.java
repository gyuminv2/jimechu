package jiandgyu.jimechu.dto;

import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.Topic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class TopicDTO {

    private Long id;
    private String title;

    private Long memberId;

    private List<Long> menuIds;
//    private List<Menu> menus;

    public TopicDTO() {
        this.id = null;
        this.title = "null";
        this.memberId = null;
    }

    // DTO 생성
    public TopicDTO(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.memberId = topic.getMember().getId();
    }
}
