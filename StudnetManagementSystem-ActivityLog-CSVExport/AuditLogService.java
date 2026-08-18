package com.student.StudentManagementAPI.service;

import com.student.StudentManagementAPI.entity.AuditLog;
import com.student.StudentManagementAPI.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String username,
                    String action,
                    String entityName,
                    Long entityId,
                    String description,
                    HttpServletRequest request) {

        String ipAddress = getClientIpAddress(request);

        AuditLog auditLog = new AuditLog(
                username,
                action,
                entityName,
                entityId,
                description,
                ipAddress
        );

        auditLogRepository.save(auditLog);
    }

    private String getClientIpAddress(HttpServletRequest request) {

        if (request == null) {
            return "UNKNOWN";
        }

        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }
}