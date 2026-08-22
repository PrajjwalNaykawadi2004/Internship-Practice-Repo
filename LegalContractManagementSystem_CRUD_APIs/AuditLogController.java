package com.legalcontract.legal_contract_management_system.controller;

import com.legalcontract.legal_contract_management_system.entity.AuditLog;
import com.legalcontract.legal_contract_management_system.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController
{

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService)
    {
        this.auditLogService = auditLogService;
    }

    @PostMapping
    public ResponseEntity<AuditLog> createAuditLog(
            @RequestBody AuditLog auditLog)
    {

        return ResponseEntity.ok(
                auditLogService.createAuditLog(auditLog));
    }

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllAuditLogs()
    {
        return ResponseEntity.ok(
                auditLogService.getAllAuditLogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLog> getAuditLogById(
            @PathVariable Long id)
    {

        return auditLogService.getAuditLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditLog> updateAuditLog(
            @PathVariable Long id,
            @RequestBody AuditLog auditLog)
    {

        return ResponseEntity.ok(
                auditLogService.updateAuditLog(id, auditLog));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuditLog(
            @PathVariable Long id)
    {

        auditLogService.deleteAuditLog(id);
        return ResponseEntity.noContent().build();
    }
}