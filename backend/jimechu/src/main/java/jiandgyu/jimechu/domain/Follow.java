package jiandgyu.jimechu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Follow {

    @Id @GeneratedValue
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private Member follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private Member following;

    public Follow(Member follower, Member following) {
        this.follower = follower;
        this.following = following;
    }

    //==생성 메서드==//
    public static Follow createFollow(Member follower, Member following) {
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        return follow;
    }
}
