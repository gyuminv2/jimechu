package jiandgyu.jimechu.dto;

import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.Topic;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberDTO {

    private Long id;
    private String nickname;
    private List<Topic> topics;

    //==연관 메서드==//
    public MemberDTO() {
        id = null;
        nickname = "null";
    }

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.topics = member.getTopics();
    }
}
