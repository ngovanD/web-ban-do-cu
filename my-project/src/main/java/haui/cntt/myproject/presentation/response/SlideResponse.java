package haui.cntt.myproject.presentation.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlideResponse {
    private long id;

    private String title;

    private String titleStrong;

    private String briefDescription;

    private String apiGetImage;

    private String link;

    private boolean status;
}
