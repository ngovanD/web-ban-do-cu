package haui.cntt.myproject.presentation.controller.basic;

import haui.cntt.myproject.common.otp.RandomOtpUtil;
import haui.cntt.myproject.presentation.mapper.UserMapper;
import haui.cntt.myproject.presentation.request.OtpRequest;
import haui.cntt.myproject.presentation.request.UserRequest;
import haui.cntt.myproject.service.Impl.OtpService;
import haui.cntt.myproject.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BasicController {

    @Autowired
    private OtpService otpService;
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/generate-otp")
    public String generateOtp(@ModelAttribute(value = "cellphone", binding = false) String cellphone
                            , @ModelAttribute(value = "status", binding = false) String status
                            , Model model){

        model.addAttribute("cellphone", cellphone);
        model.addAttribute("status", status);
        return "form_verify_otp";
    }

    @PostMapping("/generate-otp")
    public ResponseEntity sendOtp(@RequestBody OtpRequest otpRequest){
        String otp = RandomOtpUtil.createOtp();
        try {
            otpService.addOtp(otp, otpRequest.getCellphone());
            //SmsSender.sendOtp(otpRequest.getCellphone(), otp);
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body("Gửi thành công !!!");
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@Param(value = "cellphone") String cellphone, @Param(value = "otp") String otp
            , RedirectAttributes redirectAttributes
            , Model model){

        String status = otpService.checkOtp(otp, cellphone);
        if(status.equals("true"))
        {
            model.addAttribute("userRequest", UserRequest.builder().cellphone(cellphone).build());
            return "form_register";
        }
        else
        {
            redirectAttributes.addAttribute("cellphone", cellphone);
            redirectAttributes.addAttribute("status", status);
            return "redirect:/generate-otp";
        }
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserRequest userRequest){

        userService.createUser(UserMapper.convertToUser(userRequest));
        return "redirect:/login";
    }

    @PostMapping("/check-username-exist")
    public ResponseEntity checkUsernameExist(@RequestBody UserRequest userRequest){

        if(userService.existsByUsername(userRequest.getUsername()))
        {
            return ResponseEntity.badRequest().body("Username đã tồn tại !!");
        }
        else
        {
            return ResponseEntity.ok().body("Username có thể dùng !!");
        }
    }

    @PostMapping("/check-email-exist")
    public ResponseEntity checkEmailExist(@RequestBody UserRequest userRequest){
        if(userService.existsByEmail(userRequest.getEmail()))
        {
            return ResponseEntity.badRequest().body("Email đã tồn tại !!");
        }
        else
        {
            return ResponseEntity.ok().body("Email có thể dùng !!");
        }
    }

    @PostMapping("/check-cellphone-exist")
    public ResponseEntity checkCellphoneExist(@RequestBody UserRequest userRequest){
        if(userService.existsByCellphone(userRequest.getCellphone()))
        {
            return ResponseEntity.badRequest().body("Số điện thoại đã tồn tại !!");
        }
        else
        {
            return ResponseEntity.ok().body("Số điện thoại có thể dùng !!");
        }
    }

    @GetMapping("/home")
    public String home(Model model){
        return "index";
    }
}
