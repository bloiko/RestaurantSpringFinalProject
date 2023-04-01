package com.restaurant.database.dao;

import com.restaurant.database.entity.Audit;
import com.restaurant.database.entity.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
    List<Audit> findAllByEntityIdAndEntityTypeOrderByAuditDateAsc(Long entityId, EntityType entityType);
}
