package com.legalcontract.legal_contract_management_system.service;

import com.legalcontract.legal_contract_management_system.entity.AuditLog;
import com.legalcontract.legal_contract_management_system.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuditLogService
{

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository)
    {
        this.auditLogRepository = auditLogRepository;
    }

    // CREATE
    public AuditLog createAuditLog(AuditLog auditLog)
    {
        return auditLogRepository.save(auditLog);
    }

    // READ ALL
    public List<AuditLog> getAllAuditLogs()
    {
        return auditLogRepository.findAll();
    }

    // READ BY ID
    public Optional<AuditLog> getAuditLogById(Long id)
    {
        return auditLogRepository.findById(id);
    }

    // UPDATE
    public AuditLog updateAuditLog(Long id, AuditLog details)
    {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Audit log not found with id: " + id));

        auditLog.setAction(details.getAction());
        auditLog.setDescription(details.getDescription());
        auditLog.setIpAddress(details.getIpAddress());

        return auditLogRepository.save(auditLog);
    }

    // DELETE
    public void deleteAuditLog(Long id)
    {
        if (!auditLogRepository.existsById(id))
        {
            throw new RuntimeException("Audit log not found with id: " + id);
        }

        auditLogRepository.deleteById(id);
    }
}