package com.legalcontract.legal_contract_management_system.service;

import com.legalcontract.legal_contract_management_system.entity.AuditLog;
import com.legalcontract.legal_contract_management_system.entity.Clause;
import com.legalcontract.legal_contract_management_system.entity.Contract;
import com.legalcontract.legal_contract_management_system.entity.Modification;
import com.legalcontract.legal_contract_management_system.entity.User;
import com.legalcontract.legal_contract_management_system.entity.Version;
import com.legalcontract.legal_contract_management_system.repository.AuditLogRepository;
import com.legalcontract.legal_contract_management_system.repository.ClauseRepository;
import com.legalcontract.legal_contract_management_system.repository.ContractRepository;
import com.legalcontract.legal_contract_management_system.repository.ModificationRepository;
import com.legalcontract.legal_contract_management_system.repository.UserRepository;
import com.legalcontract.legal_contract_management_system.repository.VersionRepository;
import com.legalcontract.legal_contract_management_system.entity.ModificationStatus;
import com.legalcontract.legal_contract_management_system.repository.ApprovalRepository;
import com.legalcontract.legal_contract_management_system.entity.Approval;
import com.legalcontract.legal_contract_management_system.entity.ApprovalStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModificationService
{
    private final ModificationRepository modificationRepository;
    private final ClauseRepository clauseRepository;
    private final VersionRepository versionRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    private final ContractRepository contractRepository;
    private final ApprovalRepository approvalRepository;

    public ModificationService(
            ModificationRepository modificationRepository,
            ClauseRepository clauseRepository,
            VersionRepository versionRepository,
            UserRepository userRepository,
            AuditLogRepository auditLogRepository,
            ContractRepository contractRepository,
            ApprovalRepository approvalRepository)
    {
        this.modificationRepository = modificationRepository;
        this.clauseRepository = clauseRepository;
        this.versionRepository = versionRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
        this.contractRepository = contractRepository;
        this.approvalRepository = approvalRepository;
    }

    public Modification createModification(Modification modification)
    {
        if (modification.getContract() == null ||
                modification.getContract().getId() == null)
        {
            throw new RuntimeException("Contract is required");
        }

        if (modification.getVersion() == null ||
                modification.getVersion().getId() == null)
        {
            throw new RuntimeException("Version is required");
        }

        if (modification.getModifiedBy() == null ||
                modification.getModifiedBy().getUsername() == null)
        {
            throw new RuntimeException("Modified user is required");
        }

        Long contractId =
                modification.getContract().getId();

        Long versionId =
                modification.getVersion().getId();

        String username =
                modification.getModifiedBy().getUsername();

        Contract contract =
                contractRepository.findById(contractId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Contract not found with id: "
                                                + contractId));

        Version version =
                versionRepository.findById(versionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Version not found with id: "
                                                + versionId));

        User user =
                userRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found with username: "
                                                + username));

        modification.setContract(contract);
        modification.setVersion(version);
        modification.setModifiedBy(user);
        modification.setStatus(ModificationStatus.PENDING);

        if (modification.getClause() != null &&
                modification.getClause().getId() != null)
        {
            Long clauseId =
                    modification.getClause().getId();

            Clause clause =
                    clauseRepository.findById(clauseId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Clause not found with id: "
                                                    + clauseId));

            modification.setClause(clause);
        }
        else
        {
            modification.setClause(null);
        }

        Modification savedModification =
                modificationRepository.save(modification);

        Approval approval = new Approval();

        approval.setStatus(ApprovalStatus.PENDING);
        approval.setContract(contract);
        approval.setVersion(version);
        approval.setRequestedBy(user);
        approval.setModification(savedModification);

        System.out.println("DEBUG Modification ID = " +
                (approval.getModification() != null
                        ? approval.getModification().getId()
                        : null));

        System.out.println("DEBUG Approval Modification ID = "
                + approval.getModification().getId());

        approvalRepository.saveAndFlush(approval);

        AuditLog auditLog = new AuditLog();

        auditLog.setAction("MODIFICATION_CREATED");

        String description;

        if (modification.getClause() != null)
        {
            description =
                    "Modification created for Contract ID: "
                            + contract.getId()
                            + ", Clause ID: "
                            + modification.getClause().getId()
                            + ", Version ID: "
                            + version.getId();
        }
        else
        {
            description =
                    "Modification created for Contract ID: "
                            + contract.getId()
                            + ", entire Contract, Version ID: "
                            + version.getId();
        }

        auditLog.setDescription(description);

        auditLog.setUser(user);

        auditLog.setContract(contract);

        auditLogRepository.save(auditLog);

        return savedModification;
    }

    public List<Modification> getAllModifications(String username)
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with username: "
                                        + username));

        if (user.getRole() != null &&
                user.getRole().getName()
                        .equalsIgnoreCase("ContractManager"))
        {
            return modificationRepository.findAll();
        }

        return modificationRepository
                .findByModifiedByUsername(username);
    }

    public Optional<Modification> getModificationById(Long id)
    {
        return modificationRepository.findById(id);
    }

    public Modification updateModification(
            Long id,
            Modification modificationDetails)
    {
        Modification modification =
                modificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Modification not found with id: "
                                                + id));

        modification.setOldContent(
                modificationDetails.getOldContent());

        modification.setNewContent(
                modificationDetails.getNewContent());

        modification.setReason(
                modificationDetails.getReason());

        return modificationRepository.save(modification);
    }

    public void deleteModification(Long id)
    {
        if (!modificationRepository.existsById(id))
        {
            throw new RuntimeException(
                    "Modification not found with id: " + id);
        }

        modificationRepository.deleteById(id);
    }
}