package jiandgyu.jimechu.dto.member;

import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.dto.topic.TopicDTO;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberDTO {

    private Long id;
    private String nickname;

    private List<TopicDTO> topics;

    public MemberDTO() {
        id = null;
        nickname = null;
    }

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.topics = member.getTopics().stream()
                .map(TopicDTO::new) // TopicDTO로 변환
                .collect(Collectors.toList());
    }
}
