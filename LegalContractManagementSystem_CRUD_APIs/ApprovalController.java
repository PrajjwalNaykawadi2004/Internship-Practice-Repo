package com.legalcontract.legal_contract_management_system.controller;

import com.legalcontract.legal_contract_management_system.entity.Approval;
import com.legalcontract.legal_contract_management_system.service.ApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController
{

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService)
    {
        this.approvalService = approvalService;
    }

    @PostMapping
    public ResponseEntity<Approval> createApproval(
            @RequestBody Approval approval)
    {

        return ResponseEntity.ok(
                approvalService.createApproval(approval));
    }

    @GetMapping
    public ResponseEntity<List<Approval>> getAllApprovals()
    {
        return ResponseEntity.ok(
                approvalService.getAllApprovals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Approval> getApprovalById(
            @PathVariable Long id)
    {

        return approvalService.getApprovalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Approval> updateApproval(
            @PathVariable Long id,
            @RequestBody Approval approval)
    {

        return ResponseEntity.ok(
                approvalService.updateApproval(id, approval));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApproval(
            @PathVariable Long id)
    {

        approvalService.deleteApproval(id);
        return ResponseEntity.noContent().build();
    }
}