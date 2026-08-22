package com.legalcontract.legal_contract_management_system.service;

import com.legalcontract.legal_contract_management_system.entity.Contract;
import com.legalcontract.legal_contract_management_system.repository.ContractRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContractService
{

    private final ContractRepository contractRepository;

    public ContractService(ContractRepository contractRepository)
    {
        this.contractRepository = contractRepository;
    }

    // CREATE
    public Contract createContract(Contract contract)
    {
        return contractRepository.save(contract);
    }

    // READ ALL
    public List<Contract> getAllContracts()
    {
        return contractRepository.findAll();
    }

    // READ BY ID
    public Optional<Contract> getContractById(Long id)
    {
        return contractRepository.findById(id);
    }

    // UPDATE
    public Contract updateContract(Long id, Contract details)
    {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Contract not found with id: " + id));

        contract.setTitle(details.getTitle());
        contract.setDescription(details.getDescription());
        contract.setStatus(details.getStatus());

        return contractRepository.save(contract);
    }

    // DELETE
    public void deleteContract(Long id)
    {
        if (!contractRepository.existsById(id)) {
            throw new RuntimeException("Contract not found with id: " + id);
        }

        contractRepository.deleteById(id);
    }
}