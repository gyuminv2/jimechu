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

    private String title;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "topic")
    private List<Menu> menus = new ArrayList<>();

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getTopics().add(this);
    }

    //==생성 메서드==//
    public static Topic createTopic(String title, Member member, Visibility visibility) {
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setVisibility(visibility);
        topic.setMember(member);
        return topic;
    }

    public boolean isPublicVisibility() {
        return this.visibility == Visibility.PUBLIC;
    }
}
