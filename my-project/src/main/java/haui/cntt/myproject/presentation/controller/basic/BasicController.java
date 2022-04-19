package haui.cntt.myproject.presentation.controller.basic;

import haui.cntt.myproject.common.otp.RandomOtpUtil;
import haui.cntt.myproject.presentation.mapper.CategoryMapper;
import haui.cntt.myproject.presentation.mapper.ProductMapper;
import haui.cntt.myproject.presentation.mapper.SlideMapper;
import haui.cntt.myproject.presentation.mapper.UserMapper;
import haui.cntt.myproject.presentation.request.OtpRequest;
import haui.cntt.myproject.presentation.request.UserRequest;
import haui.cntt.myproject.presentation.response.CategoryResponse;
import haui.cntt.myproject.presentation.response.ProductResponse;
import haui.cntt.myproject.presentation.response.SlideResponse;
import haui.cntt.myproject.service.Impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    @Autowired
    private SlideServiceImpl slideService;

    @GetMapping("/home")
    public String home(Model model) {
        HashMap<CategoryResponse, List<CategoryResponse>> menuResponse = new HashMap<>();
        categoryService.getTreeCategory().forEach((k, v) ->
                menuResponse.put(
                        CategoryMapper.convertToCategoryResponse(k)
                        , v.stream().map(CategoryMapper::convertToCategoryResponse).collect(Collectors.toList())
                )
        );
        model.addAttribute("menuResponse", menuResponse);

        List<ProductResponse> randomProductList = productService.getRandomProduct(9)
                .stream()
                .map(ProductMapper::convertToProductResponse)
                .collect(Collectors.toList());
        model.addAttribute("randomProductList", randomProductList);

        List<ProductResponse> newProductList = productService.getNewProduct(6)
                .stream()
                .map(ProductMapper::convertToProductResponse)
                .collect(Collectors.toList());
        model.addAttribute("newProductList", newProductList);

        List<ProductResponse> hotProductList = productService.getHotProduct(3)
                .stream()
                .map(ProductMapper::convertToProductResponse)
                .collect(Collectors.toList());
        model.addAttribute("hotProductList", hotProductList);

        List<SlideResponse> slideResponseList = slideService.getAllActive()
                .stream()
                .map(SlideMapper::convertToSlideResponse)
                .collect(Collectors.toList());
        model.addAttribute("slideResponseList", slideResponseList);

//        List<ProductResponse> randomAllProductList = productService.getRandomAllProduct(12)
//                .stream()
//                .map(ProductMapper::convertToProductResponse)
//                .collect(Collectors.toList());
//        model.addAttribute("randomAllProductList", randomAllProductList);
//
//        List<ProductResponse> randomProductList = productService.getRandomAllProduct(12)
//                .stream()
//                .map(ProductMapper::convertToProductResponse)
//                .collect(Collectors.toList());
//        model.addAttribute("randomAllProductList", randomAllProductList);
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


    @GetMapping("/product/{id}")
    public String getDetailProduct(Model model, @PathVariable(value = "id") long productId) throws Throwable {
        ProductResponse productResponse = ProductMapper.convertToProductResponse(productService.getDetailProduct(productId));
        model.addAttribute("productResponse", productResponse);
        return "product_detail";
    }

    @GetMapping("/category/{slug}")
    public String getListProductByCategory(Model model, @PathVariable(value = "slug") String slug
            , @RequestParam(value = "page", required = false, defaultValue = "1") int page
            , @RequestParam(value = "min", required = false, defaultValue = "0") int min
            , @RequestParam(value = "max", required = false, defaultValue = "30000000") int max
            , @RequestParam(value = "sort", required = false, defaultValue = "created_date") String sort
            , @RequestParam(value = "location", required = false, defaultValue = "0") int codeProvince) throws Throwable {

        HashMap<CategoryResponse, List<CategoryResponse>> menuResponse = new HashMap<>();
        categoryService.getTreeCategory().forEach((k, v) ->
                menuResponse.put(
                        CategoryMapper.convertToCategoryResponse(k)
                        , v.stream().map(CategoryMapper::convertToCategoryResponse).collect(Collectors.toList())
                )
        );
        model.addAttribute("menuResponse", menuResponse);

        Page<ProductResponse> productResponsePage = categoryService.getProductByCategory(slug, page - 1, min, max, sort, codeProvince)
                .map(ProductMapper::convertToProductResponse);
        model.addAttribute("list_product", productResponsePage.getContent());
        model.addAttribute("current_page", page);
        model.addAttribute("total_page", productResponsePage.getTotalPages());
        model.addAttribute("slug", slug);
        model.addAttribute("min", min);
        model.addAttribute("max", max);
        model.addAttribute("sort", sort);
        model.addAttribute("location", codeProvince);

        return "list_product_category";
    }

    @GetMapping("/search")
    public String search(Model model
            , @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword
            , @RequestParam(value = "category_slug", required = false, defaultValue = "") String slugCategory
            , @RequestParam(value = "page", required = false, defaultValue = "1") int page
            , @RequestParam(value = "min", required = false, defaultValue = "0") int min
            , @RequestParam(value = "max", required = false, defaultValue = "30000000") int max
            , @RequestParam(value = "sort", required = false, defaultValue = "created_date") String sort
            , @RequestParam(value = "location", required = false, defaultValue = "0") int codeProvince) throws Throwable {

        HashMap<CategoryResponse, List<CategoryResponse>> menuResponse = new HashMap<>();
        categoryService.getTreeCategory().forEach((k, v) ->
                menuResponse.put(
                        CategoryMapper.convertToCategoryResponse(k)
                        , v.stream().map(CategoryMapper::convertToCategoryResponse).collect(Collectors.toList())
                )
        );
        model.addAttribute("menuResponse", menuResponse);

        Page<ProductResponse> productResponsePage = productService.searchProduct(keyword, page - 1, slugCategory, min, max, sort, codeProvince)
                .map(ProductMapper::convertToProductResponse);

        model.addAttribute("list_product", productResponsePage.getContent());
        model.addAttribute("current_page", page);
        model.addAttribute("total_page", productResponsePage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("category_slug", slugCategory);
        model.addAttribute("min", min);
        model.addAttribute("max", max);
        model.addAttribute("sort", sort);
        model.addAttribute("location", codeProvince);

        return "search_product_page";
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

    @GetMapping("/term-and-condition")
    public String termAndCondition(Model model) {
        return "term_and_condition";
    }
}
