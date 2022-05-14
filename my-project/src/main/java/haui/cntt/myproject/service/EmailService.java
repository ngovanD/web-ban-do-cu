package haui.cntt.myproject.service;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

@Service
public interface EmailService {
    void send(String to, String subject, String text) throws MessagingException;
}
