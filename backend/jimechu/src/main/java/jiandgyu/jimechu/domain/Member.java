package jiandgyu.jimechu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Topic> topics = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberRole> memberRoles = new ArrayList<>();
}