package haui.cntt.myproject.presentation.controller.admin;

import haui.cntt.myproject.common.file.FileUploadUtil;
import haui.cntt.myproject.presentation.mapper.UserMapper;
import haui.cntt.myproject.presentation.request.UserRequest;
import haui.cntt.myproject.presentation.response.UserResponse;
import haui.cntt.myproject.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/user")
public class UserAdminController {
    @Autowired
    UserServiceImpl userService;

    String UPLOAD_DIR_USER = "src/main/resources/static/user/";

    @GetMapping("/get-all")
    public String getAllUser(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page
                                        , @RequestParam(value = "size", required = false, defaultValue = "10") int size){

        Page<UserResponse> userResponsePage = userService.getAllUser(page-1, size)
                .map(UserMapper::convertToUserResponse);
        model.addAttribute("list_user", userResponsePage.getContent());
        model.addAttribute("current_page", page);
        model.addAttribute("total_page", userResponsePage.getTotalPages());

        return "admin_list_user";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(Model model, @PathVariable(value = "id") long userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok().body("Đã xóa user có id: " + userId);
    }

    @GetMapping("/view/{id}")
    public String view(Model model, @PathVariable(value = "id") long userId) throws Throwable {
        UserResponse userResponse = UserMapper.convertToUserResponse(userService.getUserById(userId));
        model.addAttribute("userResponse", userResponse);
        return "admin_user_detail";
    }

    @GetMapping("/edit/{id}")
    public String editView(Model model, @PathVariable(value = "id") long userId) throws Throwable {
        UserResponse userResponse = UserMapper.convertToUserResponse(userService.getUserById(userId));
        model.addAttribute("userResponse", userResponse);
        model.addAttribute("userRequest", new UserRequest());
        return "admin_user_edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(Model model, @PathVariable(value = "id") long userId
                                  , @ModelAttribute(value = "userRequest") UserRequest userRequest) throws Throwable {
        userService.editUser(UserMapper.convertToUser(userRequest), userRequest.isResetPassword());
        return "redirect:/admin/user/view/" + userId;
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
}
