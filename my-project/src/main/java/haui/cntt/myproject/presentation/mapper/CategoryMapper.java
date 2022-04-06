package haui.cntt.myproject.presentation.mapper;

import haui.cntt.myproject.persistance.entity.Category;
import haui.cntt.myproject.presentation.controller.basic.ImageController;
import haui.cntt.myproject.presentation.request.CategoryRequest;
import haui.cntt.myproject.presentation.response.CategoryResponse;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

public class CategoryMapper {
    private CategoryMapper(){super();}

    public static Category convertToCategory(CategoryRequest categoryRequest)
    {

        return Category.builder()
                .name(categoryRequest.getName())
                .categoryParent(Category.builder().id(categoryRequest.getCategoryParentId()).build())
                .build();
    }

    public static CategoryResponse convertToCategoryResponse(Category category)
    {
        String apiImage = MvcUriComponentsBuilder.fromMethodName(ImageController.class,"readDetailFile"
                , category.getClass().getSimpleName().toLowerCase(), category.getId().toString(), category.getImage()).toUriString();

        if(category.getCategoryParent() != null)
        {
            return new CategoryResponse(
                    category.getId(), category.getName(), category.getSlug(), apiImage
                    , convertToCategoryResponse(category.getCategoryParent())
                    );
        }

        return new CategoryResponse(
                category.getId(), category.getName(), category.getSlug(), apiImage
                , null
        );
    }
}
