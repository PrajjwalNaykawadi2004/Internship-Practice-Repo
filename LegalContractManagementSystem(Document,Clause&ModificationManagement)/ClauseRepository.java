package com.legalcontract.legal_contract_management_system.repository;

import com.legalcontract.legal_contract_management_system.entity.Clause;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClauseRepository extends JpaRepository<Clause, Long>
{
    List<Clause> findByContractIdOrderByClauseNumberAsc(Long contractId);
}