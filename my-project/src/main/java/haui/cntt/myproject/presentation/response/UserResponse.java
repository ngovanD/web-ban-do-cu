package haui.cntt.myproject.presentation.response;

import haui.cntt.myproject.common.enum_.GenderEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private long id;

    private String username;

    private String email;

    private String cellphone;

    private String fullName;

    private String apiGetAvatar;

    private GenderEnum gender;

    private String status;
}
