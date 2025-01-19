package jiandgyu.config.authority;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomMember extends User {

    private Long memberId;
    private String memberName;
    private String password;
    private Collection<GrantedAuthority> authorities;

    public CustomMember(Long memberId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.memberId = memberId;
    }
}
