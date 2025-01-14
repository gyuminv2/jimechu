package jiandgyu.jimechu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Topic {

    @Id @GeneratedValue
    @Column(name = "topic_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "topic")
    private List<Menu> menus = new ArrayList<>();

    //==생성 메서드==//
    public static Topic createTopic(Member member) {
        Topic topic = new Topic();
        topic.setMember(member);
        // menu는 우짜지??

        return topic;
    }
}
