package haui.cntt.myproject.presentation.mapper;

import haui.cntt.myproject.persistance.entity.Category;
import haui.cntt.myproject.persistance.entity.Property;
import haui.cntt.myproject.presentation.controller.basic.ImageController;
import haui.cntt.myproject.presentation.request.CategoryRequest;
import haui.cntt.myproject.presentation.response.CategoryResponse;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    private CategoryMapper() {
        super();
    }

    public static Category convertToCategory(CategoryRequest categoryRequest) {
        List<Property> propertyList = new ArrayList<>();
        if (categoryRequest.getPropertyRequestList() != null) {
            for (Long i : categoryRequest.getPropertyRequestList()) {
                propertyList.add(Property.builder().id(i).build());
            }
        }

        return Category.builder()
                .id(categoryRequest.getId())
                .name(categoryRequest.getName())
                .categoryParent(Category.builder().id(categoryRequest.getCategoryParentId()).build())
                .properties(propertyList)
                .build();
    }

    public static CategoryResponse convertToCategoryResponse(Category category) {
        String apiImage = MvcUriComponentsBuilder.fromMethodName(ImageController.class, "readDetailFile"
                , category.getClass().getSimpleName().toLowerCase(), category.getId().toString(), category.getImage()).toUriString();

        if (category.getCategoryParent() != null) {
            return CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .slug(category.getSlug())
                    .propertyResponseList(
                            category.getProperties()
                                    .stream()
                                    .map(PropertyMapper::convertToPropertyResponse)
                                    .collect(Collectors.toList()))
                    .apiGetImage(apiImage)
                    .categoryParent(convertToCategoryResponse(category.getCategoryParent()))
                    .build();
        }

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .apiGetImage(apiImage)
                .build();
    }
}
