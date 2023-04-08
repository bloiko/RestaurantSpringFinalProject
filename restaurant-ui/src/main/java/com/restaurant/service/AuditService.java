package com.restaurant.service;

import com.restaurant.database.dao.AuditRepository;
import com.restaurant.database.dao.UserRepository;
import com.restaurant.database.entity.Audit;
import com.restaurant.database.entity.EntityType;
import com.restaurant.database.entity.User;
import com.restaurant.web.dto.AuditDto;
import com.restaurant.web.dto.AuditResponse;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;


@Service
public class AuditService {

    private final AuditRepository auditRepository;

    private final UserRepository userRepository;


    public AuditService(AuditRepository auditRepository, UserRepository userRepository) {
        this.auditRepository = auditRepository;
        this.userRepository = userRepository;
    }

    public Long addAudit(AuditDto auditDto) {
        Audit audit = new Audit(auditDto.getEntityId(), auditDto.getUserId(), auditDto.getEntityType(),
                                auditDto.getActionType(), new Timestamp(new Date().getTime()));

        return auditRepository.save(audit).getId();
    }

    public List<AuditResponse> getAuditsByEntityId(EntityType entityType, Long entityId) {
        List<Audit> auditList = auditRepository.findAllByEntityIdAndEntityTypeOrderByAuditDateAsc(entityId, entityType);

        List<Long> userIds = auditList.stream().map(Audit::getUserId).collect(Collectors.toList());

        List<User> users = userRepository.findAllById(userIds);

        Map<Long, String> idToUserName = users.stream()
                                              .collect(toMap(User::getId, User::getUserName));

        return auditList.stream()
                        .map(audit -> {
                            AuditResponse auditResponse = new AuditResponse(audit.getEntityId(), audit.getUserId(),
                                    idToUserName.getOrDefault(audit.getUserId(), ""),
                                    audit.getEntityType(), audit.getActionType(), audit.getAuditDate());
                            auditResponse.setId(audit.getId());
                            return auditResponse;
                        })
                        .collect(Collectors.toList());
    }
}
