package haui.cntt.myproject.presentation.mapper;

import haui.cntt.myproject.persistance.entity.User;
import haui.cntt.myproject.presentation.controller.basic.ImageController;
import haui.cntt.myproject.presentation.request.UserRequest;
import haui.cntt.myproject.presentation.response.UserResponse;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

public class UserMapper {
    private UserMapper(){super();}

    public static User convertToUser(UserRequest userRequest)
    {
        return User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .cellphone(userRequest.getCellphone())
                .fullName(userRequest.getFullName())
                .build();
    }

    public static UserResponse convertToUserResponse(User user)
    {
        String apiGetAvatar = MvcUriComponentsBuilder.fromMethodName(ImageController.class,"readDetailFile"
                , user.getClass().getSimpleName().toLowerCase(), user.getId().toString(), user.getAvatar()).toUriString();
        if(user.getAvatar() == null || user.getAvatar().equals("avatar_default.png"))
        {
            apiGetAvatar = MvcUriComponentsBuilder.fromMethodName(ImageController.class,"readDetailFile"
                    , user.getClass().getSimpleName().toLowerCase(), "0", "avatar_default.png").toUriString();
        }

        String status = user.getHiddenFlag() == true ? "Tạm khóa": "Hoạt động bình thường";

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .cellphone(user.getCellphone())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .apiGetAvatar(apiGetAvatar)
                .status(status)
                .build();
    }
}
