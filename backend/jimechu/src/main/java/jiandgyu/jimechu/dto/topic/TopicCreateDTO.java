package jiandgyu.jimechu.dto.topic;

import jiandgyu.jimechu.domain.Visibility;
import lombok.Data;

@Data
public class TopicCreateDTO {

    private String title;

    private Visibility visibility;
}
