package com.restaurant.messaging.email;

import com.restaurant.web.dto.RegistrationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class EmailMessagesSender {
    private static final String REGISTRATION_QUEUE = "registration-email";

    private static final String ORDER_QUEUE = "order-email";

    private final JmsTemplate jmsTemplate;

    public EmailMessagesSender(@Qualifier("jmsTemplateCustom") JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Transactional
    public void send(RegistrationRequest registrationRequest) {
        try {
            jmsTemplate.convertAndSend(REGISTRATION_QUEUE, registrationRequest);
        } catch (Exception e) {
            log.error("Cannot send email to user with email = {}", registrationRequest.getEmail());
        }
    }

    @Transactional
    public void send(OrderEmailDto orderEmailDto) {
        try {
            jmsTemplate.convertAndSend(ORDER_QUEUE, orderEmailDto);
        } catch (Exception e) {
            log.error("Cannot send email to user with email = {}", orderEmailDto.getEmail());
        }
    }
}
