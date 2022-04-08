package haui.cntt.myproject.presentation.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyRequest {
    private long id;
    private String name;
}
