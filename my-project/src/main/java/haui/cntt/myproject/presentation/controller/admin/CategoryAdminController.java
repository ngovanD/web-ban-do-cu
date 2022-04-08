package haui.cntt.myproject.presentation.controller.admin;

import haui.cntt.myproject.common.file.FileUploadUtil;
import haui.cntt.myproject.presentation.mapper.CategoryMapper;
import haui.cntt.myproject.presentation.request.CategoryRequest;
import haui.cntt.myproject.presentation.response.CategoryResponse;
import haui.cntt.myproject.service.Impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/category")
public class CategoryAdminController {

    @Autowired
    CategoryServiceImpl categoryService;

    String UPLOAD_DIR_CATEGORY = "src/main/resources/static/category/";

    @GetMapping("/get-all")
    String getAll(Model model){
        HashMap<CategoryResponse, List<CategoryResponse>> treeCategory = new HashMap<>();

        categoryService.getTreeCategory().forEach((k, v)->
                treeCategory.put(
                        CategoryMapper.convertToCategoryResponse(k)
                        , v.stream().map(CategoryMapper::convertToCategoryResponse).collect(Collectors.toList())
                )
        );
        model.addAttribute("treeCategory", treeCategory);
        return "admin_list_category";
    }

    @GetMapping("/create")
    public String createForm(Model model){

        model.addAttribute("categoryRequest", new CategoryRequest());
        return "admin_new_category";
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

    @GetMapping("/get-detail")
    String getDetail(){
        return "index";
    }

    @PutMapping("/get-all")
    String edit(){
        return "index";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") long categoryId) throws Throwable {
        categoryService.delete(categoryId);
        return ResponseEntity.ok().body("Xóa loại sản phẩm thành công !!!");
    }

    @GetMapping("/get-all-not-parent")
    public ResponseEntity getAllNotParent()
    {
        List<CategoryResponse> categoryResponseList = categoryService.getListCategoryHasNotParent()
                .stream()
                .map(CategoryMapper::convertToCategoryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(categoryResponseList);
    }
}
