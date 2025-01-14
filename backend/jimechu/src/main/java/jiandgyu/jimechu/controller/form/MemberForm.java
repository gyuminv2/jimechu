package jiandgyu.jimechu.controller.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "닉네임은 필수입니다.")
    private String nickname;

//    private String password;
}
