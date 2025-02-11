package jiandgyu.jimechu.dto.auth;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String nickname;

    private String password;
}
