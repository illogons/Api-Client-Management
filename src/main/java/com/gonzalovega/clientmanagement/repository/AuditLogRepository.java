package com.gonzalovega.clientmanagement.repository;

import com.gonzalovega.clientmanagement.models.AuditLogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogModel, Long> {

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    <S extends AuditLogModel> S save(S entity);
}
