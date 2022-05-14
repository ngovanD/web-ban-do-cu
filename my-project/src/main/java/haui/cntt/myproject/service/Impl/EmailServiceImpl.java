package haui.cntt.myproject.service.Impl;

import haui.cntt.myproject.service.EmailService;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    public void send(String to, String subject, String text) throws MessagingException {
        Properties mailServerProperties;
		Session getMailSession;
		MimeMessage mailMessage;
	 
		// Step1: setup Mail Server
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		mailServerProperties.put("mail.debug", "true");

		// Step2: get Mail Session
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		mailMessage = new MimeMessage(getMailSession);
	 
		mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); //Thay abc bằng địa chỉ người nhận
	 
		// Bạn có thể chọn CC, BCC
	//    generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("cc@gmail.com")); //Địa chỉ cc gmail
	 
	 
		mailMessage.setSubject("Lấy lại mật khẩu Chợ cũ", "UTF-8");
        mailMessage.setContent(text, "text/plain; charset=UTF-8");
		// mailMessage.setText(text, "text/plain; charset=UTF-8");
	 
		// Step3: Send mail
		javax.mail.Transport transport = getMailSession.getTransport("smtp");
		// Thay your_gmail thành gmail của bạn, thay your_password thành mật khẩu gmail của bạn
		transport.connect("smtp.gmail.com", "ngovan1d@gmail.com", "ngodang123456"); 
		transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
		transport.close();
    }
}
