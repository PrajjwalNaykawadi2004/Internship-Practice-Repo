package com.legalcontract.legal_contract_management_system.repository;

import com.legalcontract.legal_contract_management_system.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long>
{
    List<Approval> findByRequestedByUsername(String username);
}
