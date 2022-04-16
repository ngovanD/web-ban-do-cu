package haui.cntt.myproject.presentation.controller.basic;

import haui.cntt.myproject.common.otp.RandomOtpUtil;
import haui.cntt.myproject.presentation.mapper.CategoryMapper;
import haui.cntt.myproject.presentation.mapper.ProductMapper;
import haui.cntt.myproject.presentation.mapper.UserMapper;
import haui.cntt.myproject.presentation.request.OtpRequest;
import haui.cntt.myproject.presentation.request.UserRequest;
import haui.cntt.myproject.presentation.response.CategoryResponse;
import haui.cntt.myproject.presentation.response.ProductResponse;
import haui.cntt.myproject.service.Impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BasicController {

    @Autowired
    private OtpService otpService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("/home")
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/generate-otp")
    public String generateOtp(@ModelAttribute(value = "cellphone", binding = false) String cellphone
            , @ModelAttribute(value = "status", binding = false) String status
            , Model model) {
        model.addAttribute("cellphone", cellphone);
        model.addAttribute("status", status);
        return "form_verify_otp";
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest otpRequest) {
        String otp = RandomOtpUtil.createOtp();
        try {
            otpService.addOtp(otp, otpRequest.getCellphone());
            //SmsSender.sendOtp(otpRequest.getCellphone(), otp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Gửi thành công !!!");
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@Param(value = "cellphone") String cellphone, @Param(value = "otp") String otp
            , RedirectAttributes redirectAttributes
            , Model model) {
        String status = otpService.checkOtp(otp, cellphone);
        if (status.equals("true")) {
            model.addAttribute("userRequest", UserRequest.builder().cellphone(cellphone).build());
            return "form_register";
        } else {
            redirectAttributes.addAttribute("cellphone", cellphone);
            redirectAttributes.addAttribute("status", status);
            return "redirect:/generate-otp";
        }
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserRequest userRequest) {
        userService.createUser(UserMapper.convertToUser(userRequest));
        return "redirect:/login";
    }

    @PostMapping("/check-username-exist")
    public ResponseEntity<String> checkUsernameExist(@RequestBody UserRequest userRequest) {
        if (userService.existsByUsername(userRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username đã tồn tại !!");
        } else {
            return ResponseEntity.ok().body("Username có thể dùng !!");
        }
    }

    @PostMapping("/check-email-exist")
    public ResponseEntity<String> checkEmailExist(@RequestBody UserRequest userRequest
            , @RequestParam(value = "id", required = false, defaultValue = "0") long userId) throws Throwable {
        if (userService.existsByEmail(userRequest.getEmail(), userId)) {
            return ResponseEntity.badRequest().body("Email đã tồn tại !!");
        } else {
            return ResponseEntity.ok().body("Email có thể dùng !!");
        }
    }

    @PostMapping("/check-cellphone-exist")
    public ResponseEntity<String> checkCellphoneExist(@RequestBody UserRequest userRequest
            , @RequestParam(value = "id", required = false, defaultValue = "0") long userId) throws Throwable {
        if (userService.existsByCellphone(userRequest.getCellphone(), userId)) {
            return ResponseEntity.badRequest().body("Số điện thoại đã tồn tại !!");
        } else {
            return ResponseEntity.ok().body("Số điện thoại có thể dùng !!");
        }
    }

    @GetMapping("/get-menu")
    public String getMenu(Model model) {
        HashMap<CategoryResponse, List<CategoryResponse>> menuResponse = new HashMap<>();

        categoryService.getTreeCategory().forEach((k, v) ->
                menuResponse.put(
                        CategoryMapper.convertToCategoryResponse(k)
                        , v.stream().map(CategoryMapper::convertToCategoryResponse).collect(Collectors.toList())
                )
        );
        model.addAttribute("menuResponse", menuResponse);
        return "fragment_menu";
    }

    @GetMapping("/get-menu2")
    public String getMenu2(Model model) {
        HashMap<CategoryResponse, List<CategoryResponse>> menuResponse = new HashMap<>();
        categoryService.getTreeCategory().forEach((k, v) ->
                menuResponse.put(
                        CategoryMapper.convertToCategoryResponse(k)
                        , v.stream().map(CategoryMapper::convertToCategoryResponse).collect(Collectors.toList())
                )
        );
        model.addAttribute("menuResponse", menuResponse);
        return "fragment_menu2";
    }

    @GetMapping("/forget-password")
    public String getPageForgetPassword() {
        return "form_forget_password";
    }

    @PostMapping("/send-new_password-by-email")
    public ResponseEntity<String> forgetPassword(@RequestBody HashMap<String, String> map) throws Throwable {
        String email = map.get("email");
        String newPassword = userService.changPasswordForUserForgetPassword(email);
        emailService.send(email, "Lấy lại mật khẩu - Chợ cũ", "Mật khẩu mới của bạn là: " + newPassword);
        return ResponseEntity.ok().body("Đổi mật khẩu và gửi mail thành công !!!");
    }


    @GetMapping("/product/detail/{id}")
    public String getDetailProduct(Model model, @PathVariable(value = "id") long productId) throws Throwable{
        ProductResponse productResponse = ProductMapper.convertToProductResponse(productService.getProductResponse(productId)) ;
        model.addAttribute("productResponse", productResponse);
        return "product_detail";
    }

    @GetMapping("/blog-detail")
    public String blogDetail(Model model) {
        return "blog_detail";
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        return "blog";
    }

    @GetMapping("/faq")
    public String faq(Model model) {
        return "faq";
    }

    @GetMapping("/search")
    public String search(Model model) {
        return "search";
    }

    @GetMapping("/term-and-condition")
    public String termAndCondition(Model model) {
        return "term_and_condition";
    }
}
