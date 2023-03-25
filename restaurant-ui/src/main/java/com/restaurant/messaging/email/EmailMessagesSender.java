package com.restaurant.messaging.email;

import com.restaurant.web.dto.RegistrationRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EmailMessagesSender {
    private static final String QUEUE = "registration-email";

    private final JmsTemplate jmsTemplate;

    public EmailMessagesSender(@Qualifier("jmsTemplateCustom") JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Transactional
    public void send(RegistrationRequest registrationRequest){
        jmsTemplate.convertAndSend(QUEUE, registrationRequest);
    }
}
