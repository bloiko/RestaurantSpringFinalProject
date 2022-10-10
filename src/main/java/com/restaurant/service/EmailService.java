package com.restaurant.service;

import com.restaurant.web.dto.RegistrationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {

    private final String emailFrom;

    private final String passwordFrom;

    public EmailService(@Value("${email.service.user.from.email}") String emailFrom,
                        @Value("${email.service.user.from.password}") String passwordFrom) {
        this.emailFrom = emailFrom;
        this.passwordFrom = passwordFrom;
    }

    public void sendSuccessfulRegistrationEmail(RegistrationRequest registrationRequest) throws MessagingException, IOException {
        final String emailOfReceiver = registrationRequest.getEmail();
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, passwordFrom);
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(emailOfReceiver, false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailOfReceiver));
        msg.setSubject("Registration was successful");
        msg.setContent("Registration was successful", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(formatContentByRequest(registrationRequest), "text/html");

        Transport.send(msg);
    }

    private static String formatContentByRequest(RegistrationRequest registrationRequest) {
        return String.format("Registration was successful username = %s, firstName = %s, lastName = %s",
                registrationRequest.getUsername(), registrationRequest.getFirstName(),
                registrationRequest.getLastName());
    }
}
