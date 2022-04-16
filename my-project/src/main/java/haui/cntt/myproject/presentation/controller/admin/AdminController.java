package haui.cntt.myproject.presentation.controller.admin;

import haui.cntt.myproject.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class AdminController {
    @Autowired
    UserServiceImpl userService;

    @GetMapping("/home")
    public String home(Model model){

        return "admin_home";
    }

    @GetMapping("/get-info/get-name")
    public ResponseEntity<String> getName() throws Throwable {
        return ResponseEntity.ok().body(userService.getCurrentUser().getFullName());
    }
}