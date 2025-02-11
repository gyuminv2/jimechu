package jiandgyu.jimechu.dto.topic;

import lombok.Data;

import java.util.ArrayList;

@Data
public class TopicAndMenuCreateDTO {

    private String title;

    private boolean isPublic;

    private ArrayList<String> menus_name;
}
