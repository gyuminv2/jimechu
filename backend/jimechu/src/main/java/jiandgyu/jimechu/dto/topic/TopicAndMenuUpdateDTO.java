package jiandgyu.jimechu.dto.topic;

import jiandgyu.jimechu.domain.Visibility;
import lombok.Data;

import java.util.ArrayList;

@Data
public class TopicAndMenuUpdateDTO {

    private String title;

    private Visibility visibility;

    private ArrayList<String> menus_name;
}