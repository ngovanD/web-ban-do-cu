package haui.cntt.myproject.presentation.mapper;

import haui.cntt.myproject.persistance.entity.ImageProduct;
import haui.cntt.myproject.presentation.controller.basic.ImageController;
import haui.cntt.myproject.presentation.response.ImageProductResponse;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

public class ImageProductMapper {
    private ImageProductMapper() {
        super();
    }

    public static ImageProductResponse convertToImageProductResponse(ImageProduct imageProduct) {

        String apiImage = MvcUriComponentsBuilder.fromMethodName(ImageController.class, "readDetailFile"
                , "product", imageProduct.getProduct().getId().toString(), imageProduct.getFileName()).toUriString();

        return ImageProductResponse.builder()
                .id(imageProduct.getId())
                .apiGetImage(apiImage)
                .isMainImage(imageProduct.isMainImage())
                .build();
    }
}
