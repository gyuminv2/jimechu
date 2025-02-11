package jiandgyu.jimechu.dto.auth;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private String accessToken;

    private String refreshToken;

    private String nickname;

    public LoginResponseDTO(String accessToken, String refreshToken, String nickname) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.nickname = nickname;
    }
}
