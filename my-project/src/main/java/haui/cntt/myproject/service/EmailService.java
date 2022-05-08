package haui.cntt.myproject.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void send(String to, String subject, String text);
}
