package com.legalcontract.legal_contract_management_system.controller;

import com.legalcontract.legal_contract_management_system.entity.Contract;
import com.legalcontract.legal_contract_management_system.service.ContractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
public class ContractController
{

    private final ContractService contractService;

    public ContractController(ContractService contractService)
    {
        this.contractService = contractService;
    }

    @PostMapping
    public ResponseEntity<Contract> createContract(
            @RequestBody Contract contract)
    {
        return ResponseEntity.ok(contractService.createContract(contract));
    }

    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts()
    {
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(
            @PathVariable Long id)
    {

        return contractService.getContractById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contract> updateContract(
            @PathVariable Long id,
            @RequestBody Contract contract)
    {

        return ResponseEntity.ok(
                contractService.updateContract(id, contract));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(
            @PathVariable Long id)
    {

        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }
}