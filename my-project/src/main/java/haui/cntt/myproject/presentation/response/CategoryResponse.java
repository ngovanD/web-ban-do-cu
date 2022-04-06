package haui.cntt.myproject.presentation.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private long id;

    private String name;

    private String slug;

    private String apiGetImage;

    private CategoryResponse categoryParent;
}
