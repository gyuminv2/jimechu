package jiandgyu.jimechu.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.Topic;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class MemberDTO {

    private Long id;
    private String nickname;
    private String password;

    private List<Map<Long, String>> topic;

    public MemberDTO() {
        id = null;
        nickname = "null";
        password = null;
    }

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.password = member.getPassword();
        this.topic = member.getTopics().stream()
                .map(topic -> Map.of(topic.getId(), topic.getTitle()))
                .collect(Collectors.toList());
    }
}
