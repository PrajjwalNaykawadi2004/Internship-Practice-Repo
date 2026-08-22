package com.legalcontract.legal_contract_management_system.service;

import com.legalcontract.legal_contract_management_system.entity.Approval;
import com.legalcontract.legal_contract_management_system.repository.ApprovalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApprovalService
{

    private final ApprovalRepository approvalRepository;

    public ApprovalService(ApprovalRepository approvalRepository)
    {
        this.approvalRepository = approvalRepository;
    }

    // CREATE
    public Approval createApproval(Approval approval)
    {
        return approvalRepository.save(approval);
    }

    // READ ALL
    public List<Approval> getAllApprovals()
    {
        return approvalRepository.findAll();
    }

    // READ BY ID
    public Optional<Approval> getApprovalById(Long id)
    {
        return approvalRepository.findById(id);
    }

    // UPDATE
    public Approval updateApproval(Long id, Approval details)
    {
        Approval approval = approvalRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Approval not found with id: " + id));

        approval.setStatus(details.getStatus());
        approval.setComments(details.getComments());
        approval.setReviewedAt(details.getReviewedAt());

        return approvalRepository.save(approval);
    }

    // DELETE
    public void deleteApproval(Long id)
    {
        if (!approvalRepository.existsById(id))
        {
            throw new RuntimeException("Approval not found with id: " + id);
        }

        approvalRepository.deleteById(id);
    }
}