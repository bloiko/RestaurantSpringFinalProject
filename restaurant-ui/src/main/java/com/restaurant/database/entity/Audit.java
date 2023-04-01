package com.restaurant.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "audit")
public class Audit {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "entity_type")
    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @Column(name = "action_type")
    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @Column(name = "audit_date")
    private Timestamp auditDate;

    public Audit(Long entityId, Long userId, EntityType entityType, ActionType actionType, Timestamp auditDate) {
        this.entityId = entityId;
        this.userId = userId;
        this.entityType = entityType;
        this.actionType = actionType;
        this.auditDate = auditDate;
    }
}
