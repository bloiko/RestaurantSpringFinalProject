package com.restaurant.web.dto;

import com.restaurant.database.entity.ActionType;
import com.restaurant.database.entity.EntityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditResponse {

    private Long id;

    private Long entityId;

    private Long userId;

    private String userName;

    private EntityType entityType;

    private ActionType actionType;

    private Timestamp auditDate;

    public AuditResponse(Long entityId, Long userId, String userName, EntityType entityType, ActionType actionType, Timestamp auditDate) {
        this.entityId = entityId;
        this.userId = userId;
        this.userName = userName;
        this.entityType = entityType;
        this.actionType = actionType;
        this.auditDate = auditDate;
    }
}
