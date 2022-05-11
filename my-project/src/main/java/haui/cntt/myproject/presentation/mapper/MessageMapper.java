package haui.cntt.myproject.presentation.mapper;

import haui.cntt.myproject.persistance.entity.Message;
import haui.cntt.myproject.persistance.entity.RoomChat;
import haui.cntt.myproject.persistance.entity.User;
import haui.cntt.myproject.presentation.request.MessageRequest;
import haui.cntt.myproject.presentation.response.MessageResponse;

import java.time.format.DateTimeFormatter;

public class MessageMapper {
    private MessageMapper(){super();}

    public static MessageResponse convertToResponse(Message message){
        return MessageResponse.builder()
                .id(message.getId())
                .userSend(UserMapper.convertToUserResponse(message.getUserSend()))
                .value(message.getValue())
                .createdDate(message.getCreatedDate().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")))
                .build();
    }

    public static Message convertToEntity(MessageRequest messageRequest){
        return Message.builder()
                .roomChat(RoomChat.builder().id(messageRequest.getRoomId()).build())
                .userSend(User.builder().username(messageRequest.getUsername()).build())
                .value(messageRequest.getValue())
                .build();
    }
}
