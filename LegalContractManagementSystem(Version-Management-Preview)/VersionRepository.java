package com.legalcontract.legal_contract_management_system.repository;

import com.legalcontract.legal_contract_management_system.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepository extends JpaRepository<Version, Long>
{

}