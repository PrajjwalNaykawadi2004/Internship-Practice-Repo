package com.legalcontract.legal_contract_management_system.repository;

import com.legalcontract.legal_contract_management_system.entity.Modification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModificationRepository
        extends JpaRepository<Modification, Long>
{
    List<Modification> findByModifiedByUsername(String username);
}