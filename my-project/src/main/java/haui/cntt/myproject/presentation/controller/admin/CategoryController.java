package haui.cntt.myproject.presentation.controller.admin;

import haui.cntt.myproject.common.file.FileUploadUtil;
import haui.cntt.myproject.presentation.mapper.CategoryMapper;
import haui.cntt.myproject.presentation.request.CategoryRequest;
import haui.cntt.myproject.presentation.response.CategoryResponse;
import haui.cntt.myproject.service.Impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    CategoryServiceImpl categoryService;

    String UPLOAD_DIR_CATEGORY = "src/main/resources/static/category/";

    @GetMapping("/get-all")
    String getAll(Model model){
        List<CategoryResponse> categoryResponseList = categoryService.getListCategoryHasNotParent()
                .stream().map(CategoryMapper::convertToCategoryResponse).collect(Collectors.toList());
        model.addAttribute("list_category", categoryResponseList);
        return "admin_list_category";
    }

    @GetMapping("/get-detail")
    String getDetail(){
        return "index";
    }

    @GetMapping("/create")
    public String createForm(Model model){

        model.addAttribute("categoryRequest", new CategoryRequest());
        return "create_category";
    }

    @PostMapping("/create")
    public String create(CategoryRequest categoryRequest,
                                 @RequestParam("image") MultipartFile multipartFile) throws IOException {

        CategoryResponse categoryResponse =  CategoryMapper.convertToCategoryResponse(
                categoryService.create(CategoryMapper.convertToCategory(categoryRequest), multipartFile.getOriginalFilename())
        );

        String uploadDir = UPLOAD_DIR_CATEGORY + categoryResponse.getId();

        String fileName = categoryResponse.getApiGetImage().substring(categoryResponse.getApiGetImage().lastIndexOf("/")+1);

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return "redirect:/admin/category/get-all";
    }

    @PutMapping("/get-all")
    String edit(){
        return "index";
    }

    @DeleteMapping("/get-all")
    String delete(){
        return "index";
    }
}
