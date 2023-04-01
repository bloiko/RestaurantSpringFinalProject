package com.restaurant.messaging.email;


import com.restaurant.web.dto.RegistrationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;

@Slf4j
@Component
public class EmailMessagesReceiver {

    private final EmailService emailService;

    public EmailMessagesReceiver(EmailService emailService) {
        this.emailService = emailService;
    }

    @JmsListener(destination = "registration-email")
    public void receiveOrderMessage(RegistrationRequest registrationRequest) {
        log.info("Registration request was taken into handling: " + registrationRequest);
        try {
            emailService.sendSuccessfulRegistrationEmail(registrationRequest);
        } catch (Exception e) {
            log.error("Cannot send email to user with email = {}", registrationRequest.getEmail());
        }
    }

//        @JmsListener(destination = "order", selector = "type = 'LIQUIDS'")
//        public void receiveLiquidsMessage(Order order) {
//            System.out.println("Order with LIQUIDS was taken into handling: " + order);
//            orderHandler.handle(order);
//        }
//
//        @JmsListener(destination = "order", selector = "type = 'COUNTABLE_ITEM'")
//        public void receiveCountableItemMessage(Order order) {
//            System.out.println("Order with COUNTABLE_ITEM was taken into handling: " + order);
//            orderHandler.handle(order);
//        }

}
