package jiandgyu.jimechu.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class LoginResponseDTO {

    private String accessToken;

    private String refreshToken;

    private String nickname;
}
