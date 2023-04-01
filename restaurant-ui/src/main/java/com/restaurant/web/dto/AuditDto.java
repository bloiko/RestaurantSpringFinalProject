package com.restaurant.web.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.restaurant.database.entity.ActionType;
import com.restaurant.database.entity.EntityType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuditDto {

    private Long entityId;

    private Long userId;

    @JsonValue
    private EntityType entityType;

    @JsonValue
    private ActionType actionType;

    public AuditDto(Long entityId, Long userId, EntityType entityType, ActionType actionType) {
        this.entityId = entityId;
        this.userId = userId;
        this.entityType = entityType;
        this.actionType = actionType;
    }
}
