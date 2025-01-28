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

    @Enumerated(EnumType.STRING)
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

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getMemberRoles().add(this);
    }
}
