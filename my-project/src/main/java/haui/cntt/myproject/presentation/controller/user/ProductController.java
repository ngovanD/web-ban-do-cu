package haui.cntt.myproject.presentation.controller.user;

import haui.cntt.myproject.common.file.FileUploadUtil;
import haui.cntt.myproject.persistance.entity.ImageProduct;
import haui.cntt.myproject.presentation.mapper.CategoryMapper;
import haui.cntt.myproject.presentation.mapper.ProductMapper;
import haui.cntt.myproject.presentation.mapper.PropertyMapper;
import haui.cntt.myproject.presentation.mapper.UserMapper;
import haui.cntt.myproject.presentation.request.ProductPropertyRequest;
import haui.cntt.myproject.presentation.request.ProductRequest;
import haui.cntt.myproject.presentation.request.PropertyRequest;
import haui.cntt.myproject.presentation.response.CategoryResponse;
import haui.cntt.myproject.presentation.response.ProductResponse;
import haui.cntt.myproject.presentation.response.PropertyResponse;
import haui.cntt.myproject.presentation.response.UserResponse;
import haui.cntt.myproject.service.Impl.CategoryServiceImpl;
import haui.cntt.myproject.service.Impl.ImageProductServiceImpl;
import haui.cntt.myproject.service.Impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user/product")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private ImageProductServiceImpl imageProductService;

    static final String UPLOAD_DIR_IMAGE_PRODUCT = "src/main/resources/static/product/";

    @GetMapping("/create-product")
    public String getPageCreateProduct() {
        return "form_create_product";
    }

    @GetMapping("/get-form/{id}")
    public String getForm(Model model, @PathVariable(value = "id") long categoryId) throws Throwable {
        List<ProductPropertyRequest> propertyRequestList = categoryService.getListProperty(categoryId)
                .stream()
                .map(PropertyMapper::convertToPropertyRequest)
                .collect(Collectors.toList());

        ProductRequest productRequest = ProductRequest.builder().productPropertyRequestList(propertyRequestList).build();
        model.addAttribute("productRequest", productRequest);
        return "form_create_product_detail";
    }

    @PostMapping("/create")
    public ResponseEntity<Long> editAvatar(@RequestBody ProductRequest productRequest) throws Throwable {
        long productId = productService.create(ProductMapper.convertToProduct(productRequest)).getId();
        return ResponseEntity.ok().body(productId);
    }

    @PostMapping("/upload-image-product/{id}")
    public ResponseEntity<String> uploadImage(@PathVariable(value = "id") long productId
            , @RequestPart(value = "images") MultipartFile[] multipartFiles) throws IOException {
        productService.uploadImage(productId, multipartFiles);
        return ResponseEntity.ok().body("Cập nhật ảnh thành công !!!");
    }

    @GetMapping("/detail/{id}")
    public String getDetailProduct(Model model, @PathVariable(value = "id") long productId) throws Throwable {
        ProductResponse productResponse = ProductMapper.convertToProductResponse(productService.getProductResponse(productId));
        model.addAttribute("productResponse", productResponse);
        return "my_product_detail";
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<String> editProduct(@PathVariable(value = "id") long productId
            , @RequestBody ProductRequest productRequest) throws Throwable {
        productRequest.setId(productId);
        productService.edit(ProductMapper.convertToProduct(productRequest));
        return ResponseEntity.ok().body("Sửa thành công !!!");
    }

    @DeleteMapping("/delete-image/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable(value = "id") long imageProductId) throws Throwable {
        imageProductService.deleteImage(imageProductId);
        return ResponseEntity.ok().body("Xóa thành công !!!");
    }

    @PostMapping("/add-image/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable(value = "id") long productId
            , @RequestPart(value = "image") MultipartFile multipartFile) throws Throwable {
        imageProductService.addImage(productId, multipartFile);
        return ResponseEntity.ok().body("Thêm ảnh thành công !!!");
    }

    @PatchMapping("/change-main-image/{id}/{imageId}")
    public ResponseEntity<String> changeMainImage(@PathVariable(value = "id") long productId
            ,@PathVariable(value = "imageId") long imageProductId) throws Throwable {
        imageProductService.changeMainImage(productId, imageProductId);
        return ResponseEntity.ok().body("Sửa ảnh bìa ảnh thành công !!!");
    }

    @PatchMapping("/sold-out/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable(value = "id") long productId) throws Throwable {
        productService.updateStatus(productId);
        return ResponseEntity.ok().body("Cập nhật thành công !!!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") long productId) throws Throwable {
        productService.delete(productId);
        return ResponseEntity.ok().body("Xóa thành công !!!");
    }
}
