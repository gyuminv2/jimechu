package jiandgyu.jimechu.dto.topic;

import jiandgyu.jimechu.domain.Visibility;
import lombok.Data;

@Data
public class TopicUpdateDTO {

    private String title;

    private Visibility visibility;
}
