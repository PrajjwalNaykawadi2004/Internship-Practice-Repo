package com.legalcontract.legal_contract_management_system.service;

import com.legalcontract.legal_contract_management_system.entity.Approval;
import com.legalcontract.legal_contract_management_system.entity.ApprovalStatus;
import com.legalcontract.legal_contract_management_system.repository.ApprovalRepository;
import org.springframework.stereotype.Service;
import com.legalcontract.legal_contract_management_system.entity.User;
import com.legalcontract.legal_contract_management_system.repository.UserRepository;
import com.legalcontract.legal_contract_management_system.entity.Modification;
import com.legalcontract.legal_contract_management_system.entity.ModificationStatus;
import com.legalcontract.legal_contract_management_system.entity.Version;
import com.legalcontract.legal_contract_management_system.repository.VersionRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ApprovalService
{
    private final ApprovalRepository approvalRepository;

    private final UserRepository userRepository;

    private final VersionRepository versionRepository;

    public ApprovalService(
            ApprovalRepository approvalRepository,
            UserRepository userRepository,
            VersionRepository versionRepository)
    {
        this.approvalRepository = approvalRepository;
        this.userRepository = userRepository;
        this.versionRepository = versionRepository;
    }

    public Approval createApproval(Approval approval)
    {
        if (approval.getStatus() == null)
        {
            approval.setStatus(ApprovalStatus.PENDING);
        }

        if (approval.getRequestedBy() != null &&
                approval.getRequestedBy().getUsername() != null)
        {
            String username =
                    approval.getRequestedBy().getUsername();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "User not found with username: " + username));

            approval.setRequestedBy(user);
        }

        return approvalRepository.save(approval);
    }

    public List<Approval> getAllApprovals(String username)
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with username: " + username));

        if (user.getRole().getName().equalsIgnoreCase("CONTRACTMANAGER"))
        {
            return approvalRepository.findAll();
        }

        return approvalRepository.findByRequestedByUsername(username);
    }

    public Optional<Approval> getApprovalById(Long id)
    {
        return approvalRepository.findById(id);
    }

    public Approval updateApproval(Long id, Approval approvalDetails)
    {
        Approval approval = approvalRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Approval not found with id: " + id));

        if (approvalDetails.getReviewedBy() == null ||
                approvalDetails.getReviewedBy().getUsername() == null)
        {
            throw new RuntimeException(
                    "Only CONTRACTMANAGER can review an approval");
        }

        String username =
                approvalDetails.getReviewedBy().getUsername();

        User reviewer = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with username: " + username));

        if (reviewer.getRole() == null ||
                !reviewer.getRole().getName().equalsIgnoreCase("CONTRACTMANAGER"))
        {
            throw new RuntimeException(
                    "Only CONTRACTMANAGER can approve or reject an approval");
        }

        // approval.setStatus(approvalDetails.getStatus());

        if (approval.getStatus() != ApprovalStatus.PENDING) {
            throw new RuntimeException("Only PENDING approvals can be reviewed");
        }

        if (approvalDetails.getStatus() == ApprovalStatus.REJECTED &&
                (approvalDetails.getComments() == null ||
                        approvalDetails.getComments().trim().isEmpty())) {

            throw new RuntimeException(
                    "Rejection reason is required"
            );
        }

        approval.setStatus(approvalDetails.getStatus());
        approval.setComments(approvalDetails.getComments());
        approval.setReviewedBy(reviewer);
        approval.setReviewedAt(LocalDateTime.now());

        if (approvalDetails.getStatus() == ApprovalStatus.APPROVED) {

            Version oldVersion = approval.getVersion();

            Version newVersion = new Version();

            newVersion.setContract(approval.getContract());
            newVersion.setCreatedBy(reviewer);
            newVersion.setVersionNumber(
                    oldVersion.getVersionNumber() + 1
            );
            newVersion.setChangeSummary(
                    "New version created after approval of Modification ID: "
                            + approval.getModification().getId()
            );
            newVersion.setStatus("ACTIVE");

            versionRepository.save(newVersion);
        }

        if (approval.getModification() != null) {
            approval.getModification().setStatus(
                    ModificationStatus.valueOf(
                            approvalDetails.getStatus().name()
                    )
            );
        }

        return approvalRepository.save(approval);
    }

    public void deleteApproval(Long id)
    {
        if (!approvalRepository.existsById(id))
        {
            throw new RuntimeException(
                    "Approval not found with id: " + id);
        }

        approvalRepository.deleteById(id);
    }
}