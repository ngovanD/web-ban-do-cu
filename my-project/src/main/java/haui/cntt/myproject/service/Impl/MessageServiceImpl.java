package haui.cntt.myproject.service.Impl;

import haui.cntt.myproject.common.exception.BadRequestException;
import haui.cntt.myproject.persistance.entity.Message;
import haui.cntt.myproject.persistance.entity.RoomChat;
import haui.cntt.myproject.persistance.entity.User;
import haui.cntt.myproject.persistance.repository.MessageRepository;
import haui.cntt.myproject.persistance.repository.RoomChatRepository;
import haui.cntt.myproject.persistance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class MessageServiceImpl {
    @Autowired
    private RoomChatRepository roomChatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Transactional
    public void saveMessage(Message convertToEntity) throws Throwable{
        RoomChat foundRoomChat = roomChatRepository.findById(convertToEntity.getRoomChat().getId()).orElseThrow(
                () -> {
                    throw new BadRequestException("Không tìm thấy nhóm chat !!!");
                }
        );

        User foundUser = userRepository.findByUsername(convertToEntity.getUserSend().getUsername()).orElseThrow(() -> {
                    throw new BadRequestException("Không tìm thấy user !!!");
                }
        );
        convertToEntity.setUserSend(foundUser);
        convertToEntity.setRoomChat(foundRoomChat);
        convertToEntity.getRoomChat().setUpdatedDate(LocalDateTime.now());
        messageRepository.save(convertToEntity);
    }
}
