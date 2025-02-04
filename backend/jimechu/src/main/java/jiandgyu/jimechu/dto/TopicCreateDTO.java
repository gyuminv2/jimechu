package jiandgyu.jimechu.dto;

import lombok.Data;

@Data
public class TopicCreateDTO {

    private String title;

    private Long memberId;

    private boolean isPublic;
}
