package com.student.StudentManagementAPI.controller;

import com.student.StudentManagementAPI.entity.AuditLog;
import com.student.StudentManagementAPI.repository.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/activity-logs")
@CrossOrigin("*")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    public AuditLogController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    // Get Logs + Search + Filter + Pagination
    @GetMapping
    public Page<AuditLog> getActivityLogs(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 0) {
            page = 0;
        }

        if (size <= 0 || size > 100) {
            size = 10;
        }

        Pageable pageable = PageRequest.of(page, size);

        if (username != null && !username.trim().isEmpty()
                && action != null && !action.trim().isEmpty()) {

            return auditLogRepository
                    .findByUsernameContainingIgnoreCaseAndActionIgnoreCase(
                            username.trim(),
                            action.trim(),
                            pageable
                    );
        }

        if (username != null && !username.trim().isEmpty()) {

            return auditLogRepository
                    .findByUsernameContainingIgnoreCase(
                            username.trim(),
                            pageable
                    );
        }

        if (action != null && !action.trim().isEmpty()) {

            return auditLogRepository
                    .findByActionIgnoreCase(
                            action.trim(),
                            pageable
                    );
        }

        return auditLogRepository.findAll(pageable);
    }


    // Export Logs as CSV
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportLogs(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String action) {

        Pageable pageable = PageRequest.of(0, 10000);

        Page<AuditLog> logs;

        if (username != null && !username.trim().isEmpty()
                && action != null && !action.trim().isEmpty()) {

            logs = auditLogRepository
                    .findByUsernameContainingIgnoreCaseAndActionIgnoreCase(
                            username.trim(),
                            action.trim(),
                            pageable
                    );

        } else if (username != null && !username.trim().isEmpty()) {

            logs = auditLogRepository
                    .findByUsernameContainingIgnoreCase(
                            username.trim(),
                            pageable
                    );

        } else if (action != null && !action.trim().isEmpty()) {

            logs = auditLogRepository
                    .findByActionIgnoreCase(
                            action.trim(),
                            pageable
                    );

        } else {

            logs = auditLogRepository.findAll(pageable);
        }

        StringBuilder csv = new StringBuilder();

        csv.append("ID,Username,Action,Entity,Entity ID,Description,IP Address,Timestamp\n");

        for (AuditLog log : logs.getContent()) {

            csv.append(log.getId()).append(",");
            csv.append(csvValue(log.getUsername())).append(",");
            csv.append(csvValue(log.getAction())).append(",");
            csv.append(csvValue(log.getEntityName())).append(",");
            csv.append(log.getEntityId()).append(",");
            csv.append(csvValue(log.getDescription())).append(",");
            csv.append(csvValue(log.getIpAddress())).append(",");
            csv.append(csvValue(String.valueOf(log.getTimestamp()))).append("\n");
        }

        byte[] csvBytes = csv.toString()
                .getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=activity_logs.csv"
                )
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvBytes);
    }


    // Prevent commas/new lines from breaking CSV
    private String csvValue(String value) {

        if (value == null) {
            return "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}