package com.restaurant.web;

import com.restaurant.database.entity.EntityType;
import com.restaurant.service.AuditService;
import com.restaurant.web.dto.AuditDto;
import com.restaurant.web.dto.AuditResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.Assert.notNull;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping
    public Long addAudit(@RequestBody AuditDto auditDto) {
        notNull(auditDto.getActionType(), "Entity type must be specified");
        notNull(auditDto.getActionType(), "Action type must be specified");

        return auditService.addAudit(auditDto);
    }

    @GetMapping("/{entityType}/{entityId}")
    public List<AuditResponse> getAuditsByEntityId(@PathVariable("entityType") EntityType entityType,
                                                   @PathVariable("entityId") String entityId) {
        notNull(entityType, "Entity type must be specified");
        notNull(entityId, "Entity Id must be specified");

        //TODO add time  sort and in response
        return auditService.getAuditsByEntityId(entityType, Long.valueOf(entityId));
    }
}
