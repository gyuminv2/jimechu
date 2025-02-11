package jiandgyu.jimechu.dto.member;

import jiandgyu.jimechu.domain.Member;
import lombok.Data;

@Data
public class MemberCreateDTO {

    private String nickname;

    private String password;

    public Member toMember() {
        Member member = new Member();
        member.setNickname(this.nickname);
        member.setPassword(this.password);
        return member;
    }
}
