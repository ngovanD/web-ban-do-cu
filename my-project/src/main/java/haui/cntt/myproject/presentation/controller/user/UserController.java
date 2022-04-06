package haui.cntt.myproject.presentation.controller.user;

import haui.cntt.myproject.presentation.mapper.UserMapper;
import haui.cntt.myproject.presentation.response.UserResponse;
import haui.cntt.myproject.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @GetMapping("/get-info")
    public String getInfo(Model model) throws Throwable {
        UserResponse userResponse = UserMapper.convertToUserResponse(userService.getCurrentUser());
        model.addAttribute("userResponse", userResponse);
        return "info_user";
    }

    @GetMapping("/get-full-name")
    public ResponseEntity getFullName(Model model) throws Throwable {
        UserResponse userResponse = UserMapper.convertToUserResponse(userService.getCurrentUser());
        return ResponseEntity.ok().body(userResponse.getFullName());
    }
}
