package haui.cntt.myproject.presentation.mapper;

import haui.cntt.myproject.persistance.entity.Blog;
import haui.cntt.myproject.presentation.controller.basic.ImageController;
import haui.cntt.myproject.presentation.request.BlogRequest;
import haui.cntt.myproject.presentation.response.BlogResponse;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

public class BlogMapper {
    private BlogMapper(){super();}

    public static BlogResponse convertToBlogResponse(Blog blog) {
        String apiImage = MvcUriComponentsBuilder.fromMethodName(ImageController.class, "readDetailFile"
                , blog.getClass().getSimpleName().toLowerCase(), "0", blog.getImage()).toUriString();
        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .dateTime(blog.getCreatedDate().toString())
                .apiGetImage(apiImage)
                .status(blog.getHiddenFlag())
                .build();
    }

    public static Blog convertToBlog(BlogRequest blogRequest) {
        return Blog.builder()
                .id(blogRequest.getId())
                .title(blogRequest.getTitle())
                .content(blogRequest.getContent())
                .hiddenFlag(blogRequest.isStatus())
                .build();
    }
}
