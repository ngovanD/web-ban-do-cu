package haui.cntt.myproject.presentation.controller.basic;

import haui.cntt.myproject.common.otp.RandomOtpUtil;
import haui.cntt.myproject.common.otp.SmsSender;
import haui.cntt.myproject.presentation.mapper.*;
import haui.cntt.myproject.presentation.request.LoginRequest;
import haui.cntt.myproject.presentation.request.OtpRequest;
import haui.cntt.myproject.presentation.request.UserRequest;
import haui.cntt.myproject.presentation.response.BlogResponse;
import haui.cntt.myproject.presentation.response.CategoryResponse;
import haui.cntt.myproject.presentation.response.ProductResponse;
import haui.cntt.myproject.presentation.response.SlideResponse;
import haui.cntt.myproject.service.*;
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
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SlideService slideService;
    @Autowired
    private BlogService blogService;

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

        List<BlogResponse> blogResponseList = blogService.getTop4()
                .stream()
                .map(BlogMapper::convertToBlogResponse)
                .collect(Collectors.toList());
        model.addAttribute("blogResponseList", blogResponseList);

        List<ProductResponse> randomAllProductList = productService.getRandomAllProduct(12)
                .stream()
                .map(ProductMapper::convertToProductResponse)
                .collect(Collectors.toList());
        model.addAttribute("randomAllProductList", randomAllProductList);

        List<ProductResponse> randomQuanAoList = productService.getRandomQuanAo(12)
                .stream()
                .map(ProductMapper::convertToProductResponse)
                .collect(Collectors.toList());
        model.addAttribute("randomQuanAoList", randomQuanAoList);

        List<ProductResponse> randomQuanDoDienTuList = productService.getRandomDoDienTu(12)
                .stream()
                .map(ProductMapper::convertToProductResponse)
                .collect(Collectors.toList());
        model.addAttribute("randomQuanDoDienTuList", randomQuanDoDienTuList);

        List<ProductResponse> randomSachTruyenList = productService.getRandomSachTruyen(12)
                .stream()
                .map(ProductMapper::convertToProductResponse)
                .collect(Collectors.toList());
        model.addAttribute("randomSachTruyenList", randomSachTruyenList);

        return "index";
    }

    @GetMapping("/")
    public String homeDefault() {
        return "redirect:/home";
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
            SmsSender.sendOtp(otpRequest.getCellphone(), otp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("G???i th??nh c??ng !!!");
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
    public String register(Model model, @ModelAttribute UserRequest userRequest) {
        userService.createUser(UserMapper.convertToUser(userRequest));
        model.addAttribute("loginRequest", new LoginRequest());
        model.addAttribute("statusRegister", "????ng k?? th??nh c??ng !!!");
        return "login";
    }

    @PostMapping("/check-username-exist")
    public ResponseEntity<String> checkUsernameExist(@RequestBody UserRequest userRequest) {
        if (userService.existsByUsername(userRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username ???? t???n t???i !!");
        } else {
            return ResponseEntity.ok().body("Username c?? th??? d??ng !!");
        }
    }

    @PostMapping("/check-email-exist")
    public ResponseEntity<String> checkEmailExist(@RequestBody UserRequest userRequest
            , @RequestParam(value = "id", required = false, defaultValue = "0") long userId) throws Throwable {
        if (userService.existsByEmail(userRequest.getEmail(), userId)) {
            return ResponseEntity.badRequest().body("Email ???? t???n t???i !!");
        } else {
            return ResponseEntity.ok().body("Email c?? th??? d??ng !!");
        }
    }

    @PostMapping("/check-cellphone-exist")
    public ResponseEntity<String> checkCellphoneExist(@RequestBody UserRequest userRequest
            , @RequestParam(value = "id", required = false, defaultValue = "0") long userId) throws Throwable {
        if (userService.existsByCellphone(userRequest.getCellphone(), userId)) {
            return ResponseEntity.badRequest().body("S??? ??i???n tho???i ???? t???n t???i !!");
        } else {
            return ResponseEntity.ok().body("S??? ??i???n tho???i c?? th??? d??ng !!");
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
        emailService.send(email, "Ch??? c?? - L???y l???i m???t kh???u", "M???t kh???u m???i c???a b???n l??: " + newPassword);
        return ResponseEntity.ok().body("?????i m???t kh???u v?? g???i mail th??nh c??ng !!!");
    }


    @GetMapping("/product/{slug}")
    public String getDetailProduct(Model model, @PathVariable(value = "slug") String slug) throws Throwable {
        ProductResponse productResponse = ProductMapper.convertToProductResponse(productService.getDetailProductBySlug(slug));
        model.addAttribute("productResponse", productResponse);

        List<ProductResponse> recommendProductList = productService.getRecommendList(productResponse.getId(), 12)
                .stream()
                .map(ProductMapper::convertToProductResponse)
                .collect(Collectors.toList());
        model.addAttribute("recommendProductList", recommendProductList);
        return "product_detail";
    }

    @GetMapping("/category/{slug}")
    public String getListProductByCategory(Model model, @PathVariable(value = "slug") String slug
            , @RequestParam(value = "page", required = false, defaultValue = "1") int page
            , @RequestParam(value = "min", required = false, defaultValue = "0") int min
            , @RequestParam(value = "max", required = false, defaultValue = "30000000") int max
            , @RequestParam(value = "sort", required = false, defaultValue = "created_date") String sort
            , @RequestParam(value = "location", required = false, defaultValue = "0") int codeProvince
            , @RequestParam(value = "status", required = false, defaultValue = "all") String status) throws Throwable {

        HashMap<CategoryResponse, List<CategoryResponse>> menuResponse = new HashMap<>();
        categoryService.getTreeCategory().forEach((k, v) ->
                menuResponse.put(
                        CategoryMapper.convertToCategoryResponse(k)
                        , v.stream().map(CategoryMapper::convertToCategoryResponse).collect(Collectors.toList())
                )
        );
        model.addAttribute("menuResponse", menuResponse);

        Page<ProductResponse> productResponsePage = categoryService.getProductByCategory(slug, page - 1, min, max, sort, codeProvince, status)
                .map(ProductMapper::convertToProductResponse);
        model.addAttribute("list_product", productResponsePage.getContent());
        model.addAttribute("current_page", page);
        model.addAttribute("total_page", productResponsePage.getTotalPages());
        model.addAttribute("slug", slug);
        model.addAttribute("min", min);
        model.addAttribute("max", max);
        model.addAttribute("sort", sort);
        model.addAttribute("location", codeProvince);
        model.addAttribute("status", status);

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
            , @RequestParam(value = "location", required = false, defaultValue = "0") int codeProvince
            , @RequestParam(value = "status", required = false, defaultValue = "all") String status) throws Throwable {

        HashMap<CategoryResponse, List<CategoryResponse>> menuResponse = new HashMap<>();
        categoryService.getTreeCategory().forEach((k, v) ->
                menuResponse.put(
                        CategoryMapper.convertToCategoryResponse(k)
                        , v.stream().map(CategoryMapper::convertToCategoryResponse).collect(Collectors.toList())
                )
        );
        model.addAttribute("menuResponse", menuResponse);

        Page<ProductResponse> productResponsePage = productService.searchProduct(keyword, page - 1, slugCategory, min, max, sort, codeProvince, status)
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
        model.addAttribute("status", status);

        return "search_product_page";
    }

    @GetMapping("/blog")
    public String blog(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        Page<BlogResponse> blogResponsePage = blogService.getAllActive(page - 1)
                .map(BlogMapper::convertToBlogResponse);
        model.addAttribute("blogList", blogResponsePage.getContent());
        model.addAttribute("current_page", page);
        model.addAttribute("total_page", blogResponsePage.getTotalPages());
        return "blog";
    }

    @GetMapping("/blog-detail/{id}")
    public String blogDetail(Model model, @PathVariable(value = "id") long blogId) throws Throwable {
        BlogResponse blogResponse = BlogMapper.convertToBlogResponse(blogService.getDetail(blogId));
        model.addAttribute("blogResponse", blogResponse);
        return "blog_detail";
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
