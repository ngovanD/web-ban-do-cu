package haui.cntt.myproject.presentation.controller.user;

import haui.cntt.myproject.common.file.FileUploadUtil;
import haui.cntt.myproject.presentation.mapper.UserMapper;
import haui.cntt.myproject.presentation.request.LoginRequest;
import haui.cntt.myproject.presentation.request.UserRequest;
import haui.cntt.myproject.presentation.response.UserResponse;
import haui.cntt.myproject.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    String UPLOAD_DIR_USER = "src/main/resources/static/user/";

    @GetMapping("/get-info")
    public String getInfo(Model model) throws Throwable {
        UserResponse userResponse = UserMapper.convertToUserResponse(userService.getCurrentUser());
        model.addAttribute("userResponse", userResponse);
        return "info_user";
    }

    @PostMapping("/edit")
    public ResponseEntity editInfo(@RequestBody UserRequest userRequest) throws Throwable {
        userService.changeInfo(UserMapper.convertToUser(userRequest));
        return ResponseEntity.ok().body("Sửa thông tin thành công !!!");
    }

    @GetMapping("/get-full-name")
    public ResponseEntity getFullName(Model model) throws Throwable {
        UserResponse userResponse = UserMapper.convertToUserResponse(userService.getCurrentUser());
        return ResponseEntity.ok().body(userResponse.getFullName());
    }

    @PostMapping("/edit/avatar/{id}")
    public ResponseEntity editAvatar(@PathVariable(value = "id") long userId
            , @RequestPart(value = "image") MultipartFile multipartFile) throws Throwable {

        String uploadDir = UPLOAD_DIR_USER + userId;
        String fileName = userService.getUserById(userId).getAvatar();
        FileUploadUtil.deleteFile(uploadDir, fileName);

        String newFileName = userService.updateAvatar(userId, multipartFile.getOriginalFilename());

        FileUploadUtil.saveFile(uploadDir, newFileName, multipartFile);

        UserResponse userResponse = UserMapper.convertToUserResponse(userService.getUserById(userId));

        return ResponseEntity.ok().body(userResponse.getApiGetAvatar());
    }

    @GetMapping("/change-password")
    public String getPageChangePassword(Model model) throws Throwable {
        UserResponse userResponse = UserMapper.convertToUserResponse(userService.getCurrentUser());
        model.addAttribute("userResponse", userResponse);
        return "form_change_password";
    }

    @PostMapping("/check-password")
    public ResponseEntity checkPassword(@RequestBody LoginRequest loginRequest){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Sai mật khẩu !");
        }
        return ResponseEntity.ok().body("Mật khẩu đúng !!!");
    }

    @PostMapping("/change-password")
    public ResponseEntity changePassword(@RequestBody LoginRequest loginRequest) throws Throwable {
        userService.changPassword(loginRequest.getPassword());
        return ResponseEntity.ok().body("Đổi mật khẩu thành công !!!");
    }
}
