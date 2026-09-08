package com.legalcontract.legal_contract_management_system.service;

import com.legalcontract.legal_contract_management_system.entity.Contract;
import com.legalcontract.legal_contract_management_system.entity.ContractStatus;
import com.legalcontract.legal_contract_management_system.entity.User;
import com.legalcontract.legal_contract_management_system.repository.ContractRepository;
import com.legalcontract.legal_contract_management_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContractService
{
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;

    public ContractService(
            ContractRepository contractRepository,
            UserRepository userRepository)
    {
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
    }

    public Contract createContract(Contract contract)
    {
        if (contract.getStatus() == null)
        {
            contract.setStatus(ContractStatus.DRAFT);
        }

        if (contract.getCreatedBy() == null ||
                contract.getCreatedBy().getUsername() == null)
        {
            throw new RuntimeException("Created user is required");
        }

        String username = contract.getCreatedBy().getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with username: " + username));

        contract.setCreatedBy(user);

        return contractRepository.save(contract);
    }

    public List<Contract> getAllContracts(String username)
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with username: " + username));

        if (user.getRole().getName().equalsIgnoreCase("ContractManager"))
        {
            return contractRepository.findAll();
        }

        return contractRepository.findByCreatedByUsername(username);
    }

    public Optional<Contract> getContractById(Long id)
    {
        return contractRepository.findById(id);
    }

    public Contract updateContract(Long id, Contract contractDetails)
    {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Contract not found with id: " + id));

        contract.setTitle(contractDetails.getTitle());
        contract.setDescription(contractDetails.getDescription());
        contract.setStatus(contractDetails.getStatus());

        return contractRepository.save(contract);
    }

    public void deleteContract(Long id)
    {
        if (!contractRepository.existsById(id))
        {
            throw new RuntimeException(
                    "Contract not found with id: " + id);
        }

        contractRepository.deleteById(id);
    }
}