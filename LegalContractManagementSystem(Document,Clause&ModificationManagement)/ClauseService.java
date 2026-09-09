package com.legalcontract.legal_contract_management_system.service;

import com.legalcontract.legal_contract_management_system.entity.Clause;
import com.legalcontract.legal_contract_management_system.repository.ClauseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClauseService
{

    private final ClauseRepository clauseRepository;

    public ClauseService(ClauseRepository clauseRepository)
    {
        this.clauseRepository = clauseRepository;
    }

    // CREATE
    public Clause createClause(Clause clause)
    {
        return clauseRepository.save(clause);
    }

    // READ ALL
    public List<Clause> getAllClauses()
    {
        return clauseRepository.findAll();
    }

    // READ BY CONTRACT - ORDERED
    public List<Clause> getClausesByContract(Long contractId)
    {
        return clauseRepository
                .findByContractIdOrderByClauseNumberAsc(contractId);
    }

    // READ BY ID
    public Optional<Clause> getClauseById(Long id)
    {
        return clauseRepository.findById(id);
    }

    // UPDATE
    public Clause updateClause(Long id, Clause details)
    {
        Clause clause = clauseRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Clause not found with id: " + id));

        clause.setClauseNumber(details.getClauseNumber());
        clause.setClauseTitle(details.getClauseTitle());
        clause.setContent(details.getContent());

        return clauseRepository.save(clause);
    }

    // DELETE
    public void deleteClause(Long id)
    {
        if (!clauseRepository.existsById(id))
        {
            throw new RuntimeException("Clause not found with id: " + id);
        }

        clauseRepository.deleteById(id);
    }
}