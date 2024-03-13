package org.city_discover.service.impl;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.city_discover.properties.SenderProperties;
import org.city_discover.service.EmailService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final Properties emailConfig;
    private final SenderProperties sender;

    @Override
    public void sendMessage(String to, String code) {
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender.getUsername(), sender.getPassword());
            }
        };

        try {
            Session session = Session.getDefaultInstance(emailConfig, auth);
            String encodingOptions = "text/html; charset=UTF-8";
            InternetAddress[] toAddress = InternetAddress.parse(to, false);

            MimeMessage message = new MimeMessage(session);
            message.setSubject("[AUTH] CityDiscover");
            message.setText("Код для авторизации в приложении: " + code);
            message.setHeader("Content-Type", encodingOptions);
            message.setSentDate(new Date());
            message.setFrom(new InternetAddress(sender.getEmail(), "CityDiscover"));
            message.addRecipients(Message.RecipientType.TO, toAddress);

            Transport.send(message, message.getAllRecipients());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
