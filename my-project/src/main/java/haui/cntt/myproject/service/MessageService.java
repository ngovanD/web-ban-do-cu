package haui.cntt.myproject.service;

import org.springframework.stereotype.Service;

import haui.cntt.myproject.persistance.entity.Message;

@Service
public interface MessageService {
    void saveMessage(Message convertToEntity) throws Throwable;
}
