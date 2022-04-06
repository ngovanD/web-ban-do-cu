package haui.cntt.myproject.presentation.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    private long Id;

    private String username;

    private String fullName;

    private String email;

    private String cellphone;

    private String password;

    private String status;

    private boolean resetPassword;
}
