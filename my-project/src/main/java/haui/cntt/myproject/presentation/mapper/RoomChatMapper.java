package haui.cntt.myproject.presentation.mapper;

import haui.cntt.myproject.common.exception.BadRequestException;
import haui.cntt.myproject.persistance.entity.RoomChat;
import haui.cntt.myproject.presentation.response.RoomChatResponse;
import haui.cntt.myproject.presentation.response.UserResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.format.DateTimeFormatter;

public class RoomChatMapper {
    private RoomChatMapper() {
        super();
    }

    public static RoomChatResponse convertToResponse(RoomChat roomChat) {
        UserResponse receiverResponse = null;
        if (roomChat.getUsers() != null) {
            receiverResponse = UserMapper.convertToUserResponse(
                    roomChat.getUsers()
                            .stream()
                            .filter(u -> !u.getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
                            .findFirst()
                            .orElse(null)
            );
        }
        return RoomChatResponse.builder()
                .roomId(roomChat.getId())
                .receiver(receiverResponse)
                .updatedDate(roomChat.getUpdatedDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .build();
    }
}
