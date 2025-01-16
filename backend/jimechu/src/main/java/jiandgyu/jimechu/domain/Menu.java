package jiandgyu.jimechu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Menu {

    @Id @GeneratedValue
    @Column(name = "menu_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    //==연관관계 메서드==//
    public void setTopic(Topic topic) {
        this.topic = topic;
        topic.getMenus().add(this);
    }

    //==생성 메서드==//
    public static Menu createMenu(String name, Topic topic) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setTopic(topic);
        return menu;
    }
}
