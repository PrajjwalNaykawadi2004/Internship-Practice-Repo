package com.student.StudentManagementAPI.repository;

import com.student.StudentManagementAPI.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByUsernameContainingIgnoreCase(
            String username,
            Pageable pageable
    );

    Page<AuditLog> findByActionIgnoreCase(
            String action,
            Pageable pageable
    );

    Page<AuditLog> findByUsernameContainingIgnoreCaseAndActionIgnoreCase(
            String username,
            String action,
            Pageable pageable
    );
}
