package haui.cntt.myproject.presentation.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomChatResponse {
    private long roomId;
    private UserResponse receiver;
    private String updatedDate;
}
