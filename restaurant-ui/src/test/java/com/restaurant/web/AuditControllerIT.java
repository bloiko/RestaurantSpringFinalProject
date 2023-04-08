package com.restaurant.web;

import com.restaurant.RestaurantApplication;
import com.restaurant.database.dao.AuditRepository;
import com.restaurant.database.entity.ActionType;
import com.restaurant.database.entity.Audit;
import com.restaurant.database.entity.EntityType;
import com.restaurant.web.dto.AuditDto;
import com.restaurant.web.dto.AuditResponse;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = RestaurantApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql({"classpath:db-test/test-user-and-audit-data.sql"})
class AuditControllerIT {

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private AuditController auditController;

    @Test
    void addAudit() {
        final AuditDto auditDto = new AuditDto(1L, 1L, EntityType.USER, ActionType.CREATE_USER);

        Long auditId = auditController.addAudit(auditDto);

        Audit audit = auditRepository.getById(auditId);
        assertNotNull(audit);
        assertEquals(auditId, audit.getId());
        assertNotNull(audit.getAuditDate());
        assertEquals(1L, audit.getUserId());
        assertEquals(1L, audit.getEntityId());
        assertEquals(EntityType.USER, audit.getEntityType());
        assertEquals(ActionType.CREATE_USER, audit.getActionType());
    }

    @Test
    void getAuditsByEntityId() {
        List<AuditResponse> auditResponses = auditController.getAuditsByEntityId(EntityType.USER, 5L);

        assertTrue(auditResponses.size() > 3);
        auditResponses.forEach(auditResponse -> {
            assertNotNull(auditResponse.getAuditDate());
            assertNotNull(auditResponse.getUserId());
            assertNotNull(auditResponse.getUserName());
            assertNotNull(auditResponse.getActionType());
            assertNotNull(auditResponse.getId());
            assertEquals(5L, auditResponse.getEntityId());
            assertEquals(EntityType.USER, auditResponse.getEntityType());
        });
    }
}