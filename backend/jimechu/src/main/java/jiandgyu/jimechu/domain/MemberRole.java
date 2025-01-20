package jiandgyu.jimechu.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MemberRole {

    @Id @GeneratedValue
    @Column(name = "memberRole_id")
    private Long id;

    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private MemberRole(Long id, Role role, Member member) {
        this.id = id;
        this.role = role;
        this.member = member;
    }
}
