package com.restaurant.service;

import com.restaurant.database.entity.ActionType;
import com.restaurant.web.AuditController;
import com.restaurant.web.dto.AuditDto;
import com.restaurant.database.entity.EntityType;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuditSender {

    private final AuditController auditController;

    private final UserRepository userRepository;

    public AuditSender(AuditController auditController, UserRepository userRepository) {
        this.auditController = auditController;
        this.userRepository = userRepository;
    }

    public void addAudit(Long entityId, EntityType entityType, ActionType actionType) {
        try {
            Authentication userDetails = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByUserName(userDetails.getName()).get();

            AuditDto auditDto = new AuditDto(entityId, user.getId(), entityType, actionType);

            auditController.addAudit(auditDto);
        } catch (Exception e) {
            log.error("Cannot send audit {} {} for entity id {}", entityType, actionType, entityId);
        }
    }
}
