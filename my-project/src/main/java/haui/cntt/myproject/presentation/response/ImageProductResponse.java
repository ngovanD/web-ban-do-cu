package haui.cntt.myproject.presentation.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageProductResponse {
    private long id;
    private String apiGetImage;
    private boolean isMainImage;
}
