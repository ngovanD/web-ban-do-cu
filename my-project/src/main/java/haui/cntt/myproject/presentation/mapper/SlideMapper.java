package haui.cntt.myproject.presentation.mapper;

import haui.cntt.myproject.persistance.entity.Slide;
import haui.cntt.myproject.presentation.controller.basic.ImageController;
import haui.cntt.myproject.presentation.request.SlideRequest;
import haui.cntt.myproject.presentation.response.SlideResponse;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

public class SlideMapper {
    private SlideMapper() {
        super();
    }

    public static SlideResponse convertToSlideResponse(Slide slide) {
        String apiImage = MvcUriComponentsBuilder.fromMethodName(ImageController.class, "readDetailFile"
                , slide.getClass().getSimpleName().toLowerCase(), "0", slide.getImage()).toUriString();
        return SlideResponse.builder()
                .id(slide.getId())
                .title(slide.getTitle())
                .titleStrong(slide.getTitleStrong())
                .apiGetImage(apiImage)
                .link(slide.getLink())
                .status(slide.getHiddenFlag())
                .build();
    }

    public static Slide convertToSlide(SlideRequest slideRequest) {
        return Slide.builder()
                .id(slideRequest.getId())
                .title(slideRequest.getTitle())
                .titleStrong(slideRequest.getTitleStrong())
                .link(slideRequest.getLink())
                .hiddenFlag(slideRequest.isStatus())
                .build();
    }
}
